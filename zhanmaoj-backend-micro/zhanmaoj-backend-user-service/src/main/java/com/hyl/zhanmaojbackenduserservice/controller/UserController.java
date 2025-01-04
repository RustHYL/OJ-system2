package com.hyl.zhanmaojbackenduserservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.zhanmaojbackendcommon.annotation.AuthCheck;
import com.hyl.zhanmaojbackendcommon.common.BaseResponse;
import com.hyl.zhanmaojbackendcommon.common.DeleteRequest;
import com.hyl.zhanmaojbackendcommon.common.ErrorCode;
import com.hyl.zhanmaojbackendcommon.common.ResultUtils;
import com.hyl.zhanmaojbackendcommon.constant.UserConstant;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.exception.ThrowUtils;
import com.hyl.zhanmaojbackendcommon.utils.SMSUtil;
import com.hyl.zhanmaojbackendmodel.model.dto.user.*;
import com.hyl.zhanmaojbackendmodel.model.entity.SMS;
import com.hyl.zhanmaojbackendmodel.model.entity.User;
import com.hyl.zhanmaojbackendmodel.model.vo.LoginUserVO;
import com.hyl.zhanmaojbackendmodel.model.vo.PageVO;
import com.hyl.zhanmaojbackendmodel.model.vo.UserAdminVO;
import com.hyl.zhanmaojbackendmodel.model.vo.UserVO;
import com.hyl.zhanmaojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    private static final String SALT = "hyl";

    private static final String PASSWORD = "12345678";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SMSUtil smsUtil;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户后台登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login/admin")
    public BaseResponse<LoginUserVO> userAdminLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userAdminLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        user.setGender(0);
        user.setUserProfile("这个人很懒什么都没有~");
        user.setUserName(user.getUserAccount());
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.SUPER_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.SUPER_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserRole())) {
            User loginUser = userService.getLoginUser(request);
            if (!loginUser.getUserRole().equals(UserConstant.SUPER_ROLE)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户权限不足");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 管理员用户查询(排除登陆本人)
     * @param userSearchAdminRequest
     * @param request
     * @return
     */
    @PostMapping("/list/admin/vo")
    public BaseResponse<List<UserAdminVO>> listUserVO(@RequestBody UserSearchAdminRequest userSearchAdminRequest,
                                                      HttpServletRequest request) {
        if (userSearchAdminRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PageVO pageVO = userSearchAdminRequest.getPageVO();
        long size = pageVO.getSize();
        long current = pageVO.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        UserAdminVO userAdminVO = userSearchAdminRequest.getUserAdminVO();
        if (userAdminVO == null) {
            userAdminVO = new UserAdminVO();
        }
        userAdminVO.setPageSize(size);
        userAdminVO.setCurrent(current);
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        BeanUtils.copyProperties(userAdminVO, userQueryRequest);
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        List<User> userList = userService.list(userService.getQueryWrapper(userQueryRequest));
        //过滤自己的信息
        List<User> userListFinal = userList.stream().filter(user -> !user.getId().equals(loginUser.getId()))
                .collect(Collectors.toList());
        //如果是管理员过滤超级管理员信息
        if (UserConstant.ADMIN_ROLE.equals(userRole)) {
            userListFinal = userListFinal.stream().filter(user -> !user.isSuper()).collect(Collectors.toList());
        }
        List<UserAdminVO> userAdminVOList = userService.getUserAdminVO(userListFinal);
        return ResultUtils.success(userAdminVOList);
    }


    /**
     *   更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     *   更新个人密码
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my/password")
    public BaseResponse<Boolean> updateMyPassword(@RequestBody UserUpdateMyPasswordRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        String userPassword = userUpdateMyRequest.getUserPassword();
        String confirmPassword = userUpdateMyRequest.getConfirmPassword();
        if (userPassword == null || confirmPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码或确认密码不能为空");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码必须超过8位");
        }
        if (!userPassword.equals(confirmPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码需和确认密码相同");
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes()));
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/reset/password")
    @AuthCheck(mustRole = UserConstant.SUPER_ROLE)
    public BaseResponse<Boolean> resetUserPassword(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + PASSWORD).getBytes());
        Long id = deleteRequest.getId();
        User user = new User();
        user.setId(id);
        user.setUserPassword(encryptPassword);
        boolean reset = userService.updateById(user);
        if (!reset) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/verify/password")
    public BaseResponse<Boolean> verifyUserPassword(@RequestBody String password, HttpServletRequest request) {
        if (StringUtils.isBlank(password) || password.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入密码格式错误");
        }
        User loginUser = userService.getLoginUser(request);
        String oldPassword = loginUser.getUserPassword();
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        if (StringUtils.equals(oldPassword, encryptPassword)){
            return ResultUtils.success(true);
        }
        return ResultUtils.success(false);
    }

    /**
     *   更新个人手机
     *
     * @param sms
     * @param request
     * @return
     */
    @PostMapping("/update/my/phone")
    public BaseResponse<Boolean> updateMyPhone(@RequestBody SMS sms,
                                               HttpServletRequest request) {
        //校验是否登录
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = loginUser.getId();
        //获取验证码
        String redisStr = userId + ":" + sms.getStatus() + ":" + sms.getPhoneNumber();
        boolean b = smsUtil.validateSMS(redisStr, sms.getCode());
        if (b){
            redisTemplate.delete(redisStr);
            User user = new User();
            user.setPhone(sms.getPhoneNumber());
            user.setId(loginUser.getId());
            boolean result = userService.updateById(user);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        return ResultUtils.success(true);
    }


    @PostMapping("/update/my/avatarUrl")
    public BaseResponse<Boolean> updateMyAvatarUrl(@RequestBody String avatarUrl, HttpServletRequest request) {
        //校验是否登录
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = loginUser.getId();
        User user = new User();
        user.setId(userId);
        user.setUserAvatar(avatarUrl);
        boolean b = userService.updateById(user);
        if (!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return ResultUtils.success(true);
    }
}
