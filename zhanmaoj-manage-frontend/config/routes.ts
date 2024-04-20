export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { name: '登录', path: '/user/login', component: './User/Login' },
      { name: '注册', path: '/user/register', component: './User/Register' },
    ],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  {
    path: '/admin',
    name: '用户管理',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/sub-page' },
      { path: '/admin/user-manage', name: '用户管理', icon: 'smile', component: './Admin/UserManage' },
      { path: '/admin/user/settings', component: './User/settings' },
    ],
  },
  {
    path: '/question',
    name: '题库管理',
    icon: 'table',
    access: 'canAdmin',
    routes: [
      { path: '/question/trueOrFalse', name: '判断题目管理', icon: 'smile', component: './Question/TrueOrFalseManage' },
      { path: '/question/trueOrFalse-submit-manage', name: '判断题目提交信息管理', icon: 'smile', component: './Question/TrueOrFalseSubmitManage' },
      { path: '/question/choiceQuestion-manage', name: '选择题目管理', icon: 'smile', component: './Question/ChoiceQuestionManage' },
      { path: '/question/choiceQuestion-submit-manage', name: '选择题目提交信息管理', icon: 'smile', component: './Question/ChoiceQuestionSubmitManage' },
      { path: '/question/question-manage', name: '编程题目管理', icon: 'smile', component: './Question/QuestionManageNew' },
      { path: '/question/question-submit-manage', name: '编程题目提交信息管理', icon: 'smile', component: './Question/QuestionSubmitManage' },
    ]
  },
  {
    path: '/test',
    name: '试卷管理',
    icon: 'table',
    access: 'canAdmin',
    routes: [
      { path: '/test/test-manage', name: '试卷管理', icon: 'smile', component: './Test/TestManage' },
      { path: '/test/test-submit-manage', name: '试卷提交信息管理', icon: 'smile', component: './Test/TestSubmitManage' },
      // { path: '/test/detail', icon: 'smile', component: './Test/TestDetailManage' },
      { path: '/test/detail/trueOrFalse/:id', icon: 'smile', component: './Question/TestTrueOrFalseManage' },
      { path: '/test/detail/choiceQuestion/:id', icon: 'smile', component: './Question/TestChoiceQuestionManage' },
      { path: '/test/detail/question/:id', icon: 'smile', component: './Question/TestQuestionManage' },
    ]
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
