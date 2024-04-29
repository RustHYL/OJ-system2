import {Alert, Button, Input, List, message} from 'antd';
import React, {useEffect,  useState} from 'react';
import {queryCurrent} from "@/pages/User/settings/service";
import {sendSMS, validateSMS} from "@/services/ant-design-pro/sms";
import {updateMyPassword, updateMyPhone, verifyUserPassword} from "@/services/ant-design-pro/user";
type Unpacked<T> = T extends (infer U)[] ? U : T;

const passwordStrength = {
  strong: <span className="strong">强</span>,
  medium: <span className="medium">中</span>,
  weak: <span className="weak">弱 Weak</span>,
};


const SecurityView: React.FC = () => {
    const verifyTypes = ['password', 'phone', 'verify'];

    const [currentUser, setCurrentUser] = useState<API.CurrentUser>()


    const fetchCurrentUser = async () => {
        try {
            // 模拟从API获取数据
            const res = await queryCurrent();
            // @ts-ignore
            setCurrentUser(res.data);
        } catch (error) {
            console.error('Error fetching current user:', error);
        }
    };

// 在组件挂载后获取当前用户信息
    useEffect(() => {
        fetchCurrentUser().then(r => {});
    }, []); // 空数组表示这个effect只在组件挂载时运行一次

    const getData = () => {
        const actions = [];

        if (currentUser?.phone) {
            // 如果currentUser?.phone不是null，则添加修改按钮，并绑定openBox2函数
            actions.push(<a key="ModifyPhone" onClick={() => openBox("phone")}>修改</a>);
        } else {
            // 如果currentUser?.phone是null，则添加绑定按钮，并绑定openBox3函数
            actions.push(<a key="BindPhone" onClick={() => openBox("verify")}>绑定</a>);
        }

        return [
            {
                title: '账户密码',
                description: (
                    <>
                        当前密码强度：
                        {passwordStrength.strong}
                    </>
                ),
                actions: [<a key="ModifyPassword" onClick={() => openBox("password")}>修改</a>],
            },
            {
                title: '手机',
                description: (
                    <>
                        当前绑定手机：
                        {currentUser?.phone && (
                            <>
                                {currentUser.phone.slice(0, 3)}****{currentUser.phone.slice(-3)}
                            </>
                        )}
                        {!currentUser?.phone && '未绑定手机'}
                    </>
                ),
                // 使用之前构建的actions数组
                actions: actions,
            },
        ];
    };


  const openBox = (verifyType: string) => {
      setType(verifyType);
      setIsShow(true);
  };

    const openPhoneBox = () => {
        setIsShow(false);
        setIsVerifyPasswordShow(false)
        setIsVerifyPhoneShow(true)
    };

    const openPasswordBox = () => {
        setIsShow(false);
        setIsVerifyPhoneShow(false)
        setIsVerifyPasswordShow(true)
    };

    //倒计时
    const [countdown, setCountdown] = useState(0);
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);
    const [countdownEdit, setCountdownEdit] = useState(0);
    const [isButtonDisabledEdit, setIsButtonDisabledEdit] = useState(false);
    const [statusInfo, setStatusInfo] = useState('');

    // Function to handle sending verification code and starting countdown
    const sendVerifyCode = async () => {
        console.log("type:", type)
        if (type === "password") {
            setStatusInfo('MODIFYPASSWORD')
        } else if (type === "phone") {
            setStatusInfo('MODIFYPHONE')
        }
        try {
            // Send verification code
            const res = await sendSMS({
                phoneNumber: currentUser?.phone,
                status: statusInfo
            })
            if (res.code === 0) {
                // Start countdown from 180 seconds
                setCountdown(180);
                setIsButtonDisabled(true);
            } else {
                message.error("验证码生成失败，" + res.message);
            }
        } catch (error) {
            message.error("验证码生成失败，" + error.message);
        }

    };

    const sendVerifyCodeEdit = async () => {
        setStatusInfo('EDITPHONE')
        // Send verification code
        try {
            const res = await sendSMS({
                phoneNumber: phoneNumber,
                status: statusInfo
            })
            if (res.code === 0) {
                // Start countdown from 180 seconds
                setCountdownEdit(180);
                setIsButtonDisabledEdit(true);
            }
        } catch (error) {
            message.error("验证码生成失败，" + error);
        }

    };

    // Effect to handle the countdown timer
    useEffect(() => {
        let intervalId: NodeJS.Timeout;
        if (countdown > 0) {
            // Update countdown every second
            intervalId = setInterval(() => {
                setCountdown((countdown) => countdown - 1);
            }, 1000);
        } else if (countdown === 0) {
            // Reset button when countdown finishes
            setIsButtonDisabled(false);
        }
        // Clear interval on component unmount
        return () => clearInterval(intervalId);
    }, [countdown]);

    // Effect to handle the countdown timer
    useEffect(() => {
        let intervalId: NodeJS.Timeout;
        if (countdownEdit > 0) {
            // Update countdown every second
            intervalId = setInterval(() => {
                setCountdownEdit((countdownEdit) => countdownEdit - 1);
            }, 1000);
        } else if (countdownEdit === 0) {
            // Reset button when countdown finishes
            setIsButtonDisabledEdit(false);
        }
        // Clear interval on component unmount
        return () => clearInterval(intervalId);
    }, [countdownEdit]);


    const doPhoneSubmit = async () => {
        if (type == "phone"){
            setStatusInfo('MODIFYPHONE');
        } else if (type == "password"){
            setStatusInfo('MODIFYPASSWORD');
        }
        try {
            const res = await validateSMS({
                phoneNumber: currentUser?.phone,
                status: statusInfo,
                code: smsCode
            })
            if (res?.code === 0){
                if (res?.data){
                    setIsVerifyPhoneShow(false);
                    setIsVerifyPasswordShow(false);
                    if (type === "phone"){
                        setShowEditPhone(true);
                    }else {
                        setShowEditPassword(true);
                    }
                }else {
                    message.error("验证码不正确，请重试")
                }
            } else {
                message.error("系统错误:" + res.message)
            }
        } catch (error) {
            message.error("系统错误:" + error.message)
        }

    }

    const doPhoneSubmitEdit = async () => {
        setStatusInfo('EDITPHONE');
        try {
            const res = await updateMyPhone({
                phoneNumber: phoneNumber,
                status: statusInfo,
                code: smsCodeNew
            })
            if (res.code === 0){
                message.success("手机号修改成功");
                window.location.reload();
            } else {
                message.error(res.message)
            }
        } catch (error) {
            message.error("系统错误:" + error)
        }

    }

    const doPasswordSubmit = async () => {
        try {
            const res = await verifyUserPassword(oldPassword)
            if (res.code === 0){
                if (res.data){
                    setIsVerifyPhoneShow(false);
                    setIsVerifyPasswordShow(false);
                    if (type === "password"){
                        setShowEditPassword(true);
                    }else {
                        setShowEditPhone(true);
                    }
                }else {
                    message.error("密码错误，请重新输入")
                }
            } else {
                message.error("系统错误:" + res.message)
            }
        } catch (error) {
            message.error("系统错误:" + error.message)
        }

    }

    const doPasswordSubmitEdit = async () => {
        try {
            const res = await updateMyPassword({
                userPassword: newPassword,
                confirmPassword: confirmPassword
            })
            if (res.code === 0 && res.data){
                message.success("修改成功")
                window.location.reload()
            } else {
                message.error(res.message)
            }
        } catch (error) {
            message.error("系统错误:" + error)
        }

    }



  const data = getData();

  const [isShow, setIsShow] = useState(false);
    const [isVerifyPhoneShow, setIsVerifyPhoneShow] = useState(false);
    const [isVerifyPasswordShow, setIsVerifyPasswordShow] = useState(false);
    const [type, setType] = useState('');
    const [smsCode, setSmsCode] = useState('');
    const [smsCodeNew, setSmsCodeNew] = useState('');
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [showEditPhone, setShowEditPhone] = useState(false);
    const [showEditPassword, setShowEditPassword] = useState(false);

  return (
    <>
        <div style={{width: '100%', height: '100%', position: 'relative'}}>
            <List<Unpacked<typeof data>>
                itemLayout="horizontal"
                dataSource={data}
                renderItem={(item) => (
                    <List.Item actions={item.actions}>
                        <List.Item.Meta title={item.title} description={item.description} />
                    </List.Item>
                )}
            />
            {isShow &&(
                <div className="phone-verification-form" style={{border: '0.5px solid #000',borderRadius: '4px', padding: '50px', backgroundColor: 'white', position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)',zIndex: '10000',width: '640px',height: '470px'}}>
                    <div>
                        <h2>安全验证</h2>
                    </div>
                    <div className="body" style={{borderTop: '1px solid #eee',paddingTop: '20px'}}>
                        {currentUser?.phone && (
                            <>
                                <div className="doPhone" style={{display: 'flex',alignItems: 'center',marginBottom: '20px', cursor: 'pointer', border: '1px solid #d3d3d3', borderRadius: '4px', padding: '10px', width: '480px'}} onClick={openPhoneBox}>
                                    <div className="icon">
                                        <img src={require('../../../../../public/phone.png')} alt={"phone"} style={{marginRight: '10px',width: '40px',height: '40px'}}/>
                                    </div>
                                    <div className="detail">
                                        <p style={{color: '#333',fontWeight: 'bold'}}>手机号验证</p>
                                        <p style={{color: '#666', fontSize: '14px'}}>通过{currentUser?.phone?.slice(0, 3)}****{currentUser?.phone?.slice(-3)}发送短信验证</p>
                                    </div>
                                </div>
                            </>
                        )}
                        <div className="doPassword" style={{display: 'flex',alignItems: 'center',marginBottom: '20px', cursor: 'pointer', border: '1px solid #d3d3d3', borderRadius: '4px', padding: '10px', width: '480px'}} onClick={openPasswordBox}>
                            <div className="icon">
                                <img src={require('../../../../../public/password.png')} alt={"password"} style={{marginRight: '10px',width: '40px',height: '40px'}}/>
                            </div>
                            <div className="detail">
                                <p style={{color: '#333',fontWeight: 'bold'}}>密码验证</p>
                                <p style={{color: '#666', fontSize: '14px'}}>提高账号信息与个人资料的安全性</p>
                            </div>
                        </div>
                    </div>
                    <Alert
                        message="安全验证"
                        description="为了保护你的账号安全，需要验证你的身份，验证通过后即可正常使用。"
                        type="info"
                        showIcon
                    />
                </div>
            )}
            {isVerifyPhoneShow &&(
                <div className="phone-verification-form" style={{border: '0.5px solid #000',borderRadius: '4px', padding: '50px', backgroundColor: 'white', position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)',zIndex: '10000',width: '640px',height: '350px'}}>
                    <div>
                        <h2>安全验证</h2>
                    </div>
                    <div className="body" style={{borderTop: '1px solid #eee',paddingTop: '20px'}}>
                        <div className="card-body">
                            <h2>通过手机号{currentUser?.phone?.slice(0, 3)}****{currentUser?.phone?.slice(-3)}接收短信验证码</h2>
                            <div className="code" style={{display: 'flex', gap: '15px', marginTop: '20px'}}>
                                <Input placeholder="请输入验证码" allowClear size="large" value={smsCode} onChange={(e) => setSmsCode(e.target.value)}/>
                                <Button
                                    size="large"
                                    disabled={isButtonDisabled}
                                    onClick={sendVerifyCode}
                                >
                                    {isButtonDisabled ? `${countdown}秒后重试` : '发送验证码'}
                                </Button>
                            </div>
                            <Button block={true} type="primary" style={{marginTop: '20px'}} size="large" onClick={doPhoneSubmit}>确定</Button>
                            <div className="tip" style={{fontSize: '18px', color: '#36aafd', marginTop: '10px', display: 'flex', justifyContent: 'center'}} onClick={openPasswordBox}>手机号无法使用?试试密码辅助验证</div>
                        </div>
                    </div>
                </div>
            )}
            {isVerifyPasswordShow &&(
                <div className="phone-verification-form" style={{border: '0.5px solid #000',borderRadius: '4px', padding: '50px', backgroundColor: 'white', position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)',zIndex: '10000',width: '640px',height: '350px'}}>
                    <div>
                        <h2>安全验证</h2>
                    </div>
                    <div className="body" style={{borderTop: '1px solid #eee',paddingTop: '20px'}}>
                        <div className="card-body">
                            <h2>通过密码进行验证</h2>
                            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'flex-start', marginTop: '20px'}}>
                                <Input.Password placeholder="请输入原始密码" size="large" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)}/>
                            </div>
                            <Button block={true} type="primary" style={{marginTop: '20px'}} size="large" onClick={doPasswordSubmit}>确定</Button>
                            <div className="tip" style={{fontSize: '18px', color: '#36aafd', marginTop: '10px', display: 'flex', justifyContent: 'center'}} onClick={openPhoneBox}>忘记密码?试试手机辅助验证</div>
                        </div>
                    </div>
                </div>
            )}
            {showEditPhone &&(
                <div className="phone-verification-form" style={{border: '0.5px solid #000',borderRadius: '4px', padding: '50px', backgroundColor: 'white', position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)',zIndex: '10000',width: '640px',height: '350px'}}>
                    <div>
                        <h2>修改手机绑定</h2>
                    </div>
                    <div className="body" style={{borderTop: '1px solid #eee',paddingTop: '20px'}}>
                        <div className="card-body">
                            <div className="code" style={{display: 'flex', gap: '15px', marginTop: '20px'}}>
                                <Input placeholder="请输入新的手机号" allowClear size="large" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)}/>
                            </div>
                            <div className="code" style={{display: 'flex', gap: '15px', marginTop: '20px'}}>
                                <Input placeholder="请输入验证码" allowClear size="large" value={smsCodeNew} onChange={(e) => setSmsCodeNew(e.target.value)}/>
                                <Button
                                    size="large"
                                    disabled={isButtonDisabledEdit}
                                    onClick={sendVerifyCodeEdit}
                                >
                                    {isButtonDisabledEdit ? `${countdownEdit}秒后重试` : '发送验证码'}
                                </Button>
                            </div>
                            <Button block={true} type="primary" style={{marginTop: '20px'}} size="large" onClick={doPhoneSubmitEdit}>确定</Button>
                        </div>
                    </div>
                </div>
            )}
            {showEditPassword &&(
                <div className="phone-verification-form" style={{border: '0.5px solid #000',borderRadius: '4px', padding: '50px', backgroundColor: 'white', position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)',zIndex: '10000',width: '640px',height: '350px'}}>
                    <div>
                        <h2>修改密码</h2>
                    </div>
                    <div className="body" style={{borderTop: '1px solid #eee',paddingTop: '20px'}}>
                        <div className="card-body">
                            <h2>通过密码进行验证</h2>
                            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'flex-start', marginTop: '20px'}}>
                                <Input.Password placeholder="请输入新密码" size="large" value={newPassword} onChange={(e) => setNewPassword(e.target.value)}/>
                            </div>
                            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'flex-start', marginTop: '20px'}}>
                                <Input.Password placeholder="请再次输入密码" size="large" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}/>
                            </div>
                            <Button block={true} type="primary" style={{marginTop: '20px'}} size="large" onClick={doPasswordSubmitEdit}>确定</Button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    </>
  );
};

export default SecurityView;
