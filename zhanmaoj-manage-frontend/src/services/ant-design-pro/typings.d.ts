// @ts-ignore
/* eslint-disable */


declare namespace API {
  type CurrentUser = {
    id?: number;
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
    id?: number;
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
    id?: number;
    title?: string;
    content?: string;
    tags?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    judgeCase?: string;
    judgeConfig?: string;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
  };

  type QuestionTestAdminVo = {
    id?: number;
    questionId?: number;
    title?: string;
    tags?: string;
    answer?: string;
    judgeCase?: string;
    judgeConfig?: string;
    score?: number;
  };

  type QuestionTestSearchRequest = {
    testId?: number;
    current?: number;
    size?: number;
  };

  type QuestionTestUpdateRequest = {
    id?: number;
    score?: number;
  };

  type QuestionTestAddRequest = {
    id?: string;
    score?: number;
    testId?: string;
  };



  type QuestionSearchRequest = {
    id?: number;
    title?: string;
    tags?: string;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type QuestionUpdateRequest = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    judgeCase?: string;
    judgeConfig?: string;
    userId?: number;
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
    id?: number;
    language?: string;
    code?: string;
    judgeInfo?: string;
    status?: number;
    questionId?: number;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
  };

  type QuestionSubmitSearchRequest = {
    id?: number;
    language?: string;
    status?: number;
    questionId?: number;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type QuestionSubmitUpdateRequest = {
    id?: number;
    judgeInfo?: string;
    status?: number;
  };

  /**
   * 判断题
   */
  type TrueOrFalseAdminVo = {
    id?: number;
    title?: string;
    tags?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
  };


  type TrueOrFalseSearchRequest = {
    id?: number;
    title?: string;
    tags?: string;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type TrueOrFalseUpdateRequest = {
    id?: number;
    title?: string;
    tags?: string;
    answer?: string;
  };


  type TrueOrFalseTestAdminVo = {
    id?: number;
    questionId?: number;
    title?: string;
    tags?: string;
    answer?: string;
    score?: number;
  };


  type TrueOrFalseTestSearchRequest = {
    testId?: number;
    current?: number;
    size?: number;
  };

  type TrueOrFalseTestUpdateRequest = {
    id?: number;
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
    id?: number;
    status?: number;
    answer?: number;
    questionId?: number;
    userId?: number;
    testId?: number;
    createTime?: Date;
    updateTime?: Date;
  };

  type TrueOrFalseSubmitSearchRequest = {
    id?: number;
    status?: number;
    questionId?: number;
    userId?: number;
    testId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type TrueOrFalseSubmitUpdateRequest = {
    id?: number;
    status?: number;
    answer?: number;
  };

  /**
   * 选择题
   */
  type ChoiceQuestionAdminVo = {
    id?: number;
    title?: string;
    tags?: string;
    optionA?: string;
    optionB?: string;
    optionC?: string;
    optionD?: string;
    answer?: string;
    submitNum?: number;
    acceptedNum?: number;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
  };


  type ChoiceQuestionSearchRequest = {
    id?: number;
    title?: string;
    tags?: string;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type ChoiceQuestionUpdateRequest = {
    id?: number;
    title?: string;
    optionA?: string;
    optionB?: string;
    optionC?: string;
    optionD?: string;
    tags?: string;
    answer?: string;
  };

  type ChoiceQuestionTestAdminVo = {
    id?: number;
    questionId?: number;
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
    testId?: number;
    current?: number;
    size?: number;
  };

  type ChoiceQuestionTestUpdateRequest = {
    id?: number;
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
    id?: number;
    answer?: number;
    status?: number;
    questionId?: number;
    userId?: number;
    testId?: number;
    createTime?: Date;
    updateTime?: Date;
  };

  type ChoiceQuestionSubmitSearchRequest = {
    id?: number;
    status?: number;
    questionId?: number;
    userId?: number;
    testId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type ChoiceQuestionSubmitUpdateRequest = {
    id?: number;
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
    id: number;
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
    id?: number;
    title?: string;
    status?: number;
    password?: string;
    content?: string;
    questionNum?: number;
    beginTime?: Date;
    expiredTime?: Date;
    examTime?: number;
    totalScore?: number;
    userId?: number;
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
    questionList?: string;
  };


  type TestSearchRequest = {
    id?: number;
    title?: string;
    status?: number;
    content?: string;
    userId?: number;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };


  type TestUpdateRequest = {
    id?: number;
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
    id?: number;
    status?: number;
    score?: number;
    userId?: number;
    testId?: number;
    beginTime?: Date;
    endTime?: Date;
    createTime?: Date;
    updateTime?: Date;
  };

  type TestSubmitSearchRequest = {
    id?: number;
    status?: number;
    userId?: number;
    testId?: number;
    beginTime?: Date;
    endTime?: Date;
    createTime?: Date;
    updateTime?: Date;
    current?: number;
    size?: number;
  };

  type TestSubmitUpdateRequest = {
    id?: number;
    status?: number;
    score?: number;
  };

  type IdTitleVO = {
    id?: number;
    title?: string;
  };
}
