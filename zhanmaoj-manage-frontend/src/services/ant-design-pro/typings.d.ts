// @ts-ignore
/* eslint-disable */


declare namespace API {
  type CurrentUser = {
    id?: string;
    userName?: string;
    userAccount?: string;
    userAvatar?: string;
    gender?: number;
    phone?: string;
    email?: string;
    userRole?: string;
    createTime?: Date;
  };

  type RegisterResult = number;

  /**
   * 管理页面用户信息
   */
  type UserAdminVo = {
    id?: string;
    userName?: string;
    userAccount?: string;
    userPassword?: string;
    userProfile?: string;
    userAvatar?: string;
    gender?: number;
    phone?: string;
    email?: string;
    userRole?: string;
    createTime?: Date;
    updateTime?: Date;
  };

  type JudgeCase = {
    input?: string;
    output?: string;
  }

  type JudgeConfig = {
    timeLimit?: number;
    memoryLimit?: number;
    stackLimit?: number;
  }


  /**
   * 管理页面题目信息
   */
  type QuestionAdminVo = {
    id?: string;
    title?: string;
    content?: string;
    tags?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    judgeCase?: string;
    judgeConfig?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
  };

  type QuestionTestAdminVo = {
    id?: string;
    questionId?: string;
    title?: string;
    tags?: string;
    answer?: string;
    judgeCase?: string;
    judgeConfig?: string;
    score?: number;
  };

  type QuestionTestSearchRequest = {
    testId?: string;
    current?: number;
    size?: number;
  };

  type QuestionTestUpdateRequest = {
    id?: string;
    score?: number;
  };

  type QuestionTestAddRequest = {
    id?: string;
    score?: number;
    testId?: string;
  };



  type QuestionSearchRequest = {
    id?: string;
    title?: string;
    tags?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type QuestionUpdateRequest = {
    id?: string;
    title?: string;
    content?: string;
    tags?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    judgeCase?: string;
    judgeConfig?: string;
    userId?: string;
  };

  type JudgeInfo = {
    message?: string;
    memory?: number;
    time?: number;
  }

  /**
   * 管理页面题目提交信息
   */
  type QuestionSubmitAdminVo = {
    id?: string;
    language?: string;
    code?: string;
    judgeInfo?: string;
    status?: number;
    questionId?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
  };

  type QuestionSubmitSearchRequest = {
    id?: string;
    language?: string;
    status?: number;
    questionId?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type QuestionSubmitUpdateRequest = {
    id?: string;
    judgeInfo?: string;
    status?: number;
  };

  /**
   * 判断题
   */
  type TrueOrFalseAdminVo = {
    id?: string;
    title?: string;
    tags?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
  };


  type TrueOrFalseSearchRequest = {
    id?: string;
    title?: string;
    tags?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type TrueOrFalseUpdateRequest = {
    id?: string;
    title?: string;
    tags?: string;
    answer?: string;
  };


  type TrueOrFalseTestAdminVo = {
    id?: string;
    questionId?: string;
    title?: string;
    tags?: string;
    answer?: string;
    score?: number;
  };


  type TrueOrFalseTestSearchRequest = {
    testId?: string;
    current?: number;
    size?: number;
  };

  type TrueOrFalseTestUpdateRequest = {
    id?: string;
    score?: number;
  };

  type TrueOrFalseTestAddRequest = {
    id?: string;
    score?: number;
    testId?: string;
  };


  /**
   * 判断题提交
   */
  type TrueOrFalseSubmitAdminVo = {
    id?: string;
    status?: number;
    answer?: number;
    questionId?: string;
    userId?: string;
    testId?: string;
    createTime?: Date;
    updateTime?: Date;
  };

  type TrueOrFalseSubmitSearchRequest = {
    id?: stringr;
    status?: number;
    questionId?: string;
    userId?: string;
    testId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type TrueOrFalseSubmitUpdateRequest = {
    id?: string;
    status?: number;
    answer?: number;
  };

  /**
   * 选择题
   */
  type ChoiceQuestionAdminVo = {
    id?: string;
    title?: string;
    tags?: string;
    optionA?: string;
    optionB?: string;
    optionC?: string;
    optionD?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
  };


  type ChoiceQuestionSearchRequest = {
    id?: string;
    title?: string;
    tags?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type ChoiceQuestionUpdateRequest = {
    id?: string;
    title?: string;
    optionA?: string;
    optionB?: string;
    optionC?: string;
    optionD?: string;
    tags?: string;
    answer?: string;
  };

  type ChoiceQuestionTestAdminVo = {
    id?: string;
    questionId?: string;
    title?: string;
    tags?: string;
    answer?: string;
    score?: number;
    optionA?: string;
    optionB?: string;
    optionC?: string;
    optionD?: string;
  };


  type ChoiceQuestionTestSearchRequest = {
    testId?: string;
    current?: number;
    size?: number;
  };

  type ChoiceQuestionTestUpdateRequest = {
    id?: string;
    score?: number;
  };

  type ChoiceQuestionTestAddRequest = {
    id?: string;
    score?: number;
    testId?: string;
  };

  /**
   * 选择题提交
   */
  type ChoiceQuestionSubmitAdminVo = {
    id?: string;
    answer?: number;
    status?: number;
    questionId?: string;
    userId?: string;
    testId?: string;
    createTime?: Date;
    updateTime?: Date;
  };

  type ChoiceQuestionSubmitSearchRequest = {
    id?: string;
    status?: number;
    questionId?: string;
    userId?: string;
    testId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type ChoiceQuestionSubmitUpdateRequest = {
    id?: string;
    status?: number;
    answer?: number;
  };


  /**
   * 用户条件搜索
   */
  type UserInfoBySearchVO = {
    userSearchVO: UserAdminVo;
    pageVO: PageVO;
  };

  /**
   * 用于对接后端的通用返回类
   */
  type BaseResponse<T> = {
    code: number,
    data: T,
    message: string,
  }

  type UserLoginResVo = {
    id: string;
    userName: string;
    userProfile: string;
    userAvatar: string;
    gender: number;
    phone: string;
    email: string;
    userRole: string;
    createTime: Date;
  };

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: number;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type RuleList = {
    data?: RuleListItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };

  type LoginParams = {
    userAccount?: string;
    userPassword?: string;
    autoLogin?: boolean;
    type?: string;
  };


  type PageVO = {
    current?: number;
    size?: number;
  };


  type RegisterParams = {
    userAccount?: string;
    userPassword?: string;
    checkPassword?: string;
    type?: string;
  };

  /**
   * 试卷
   */
  type TestAdminVo = {
    id?: string;
    title?: string;
    status?: number;
    password?: string;
    content?: string;
    questionNum?: number;
    beginTime?: Date;
    expiredTime?: Date;
    examTime?: number;
    totalScore?: number;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
  };

  type TestAddRequest = {
    title?: string;
    status?: number;
    password?: string;
    content?: string;
    beginTime?: Date;
    expiredTime?: Date;
    examTime?: number;
    trueOrFalseNum?: number;
    trueOrFalsePerScore?: number;
    choiceQuestionNum?: number;
    choicePerScore?: number;
    questionList?: ProgramQuestion[];
  };


  type ProgramQuestion = {
    id?: string;
    score?: string;
  };


  type TestSearchRequest = {
    id?: string;
    title?: string;
    status?: number;
    content?: string;
    userId?: string;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type TestUpdateRequest = {
    id?: string;
    title?: string;
    status?: number;
    password?: string;
    content?: string;
    beginTime?: Date;
    expiredTime?: Date;
    examTime?: number;
  };
  /**
   * 试卷提交
   */
  type TestSubmitAdminVo = {
    id?: string;
    status?: number;
    score?: number;
    userId?: string;
    testId?: string;
    beginTime?: Date;
    endTime?: Date;
    createTime?: Date;
    updateTime?: Date;
  };

  type TestSubmitSearchRequest = {
    id?: string;
    status?: number;
    userId?: string;
    testId?: string;
    beginTime?: Date;
    endTime?: Date;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type TestSubmitUpdateRequest = {
    id?: string;
    status?: number;
    score?: number;
  };

  type IdTitleVO = {
    id?: string;
    title?: string;
  };

  type SMS = {
    status?: string;
    phoneNumber?: string;
    code?: string;
    min?: number;
  };

  type UserUpdateMyPasswordRequest = {
    userPassword?: string;
    confirmPassword?: string;
  };

}
