# 数据库初始化
# @author <a href="https://github.com/lihyl">程序员鱼皮</a>
# @from <a href="https://hyl.icu">编程导航知识星球</a>

-- 创建库
create database if not exists zhanmaoj;

-- 切换库
use zhanmaoj;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json 数组）',
    judgeConfig text                               null comment '判题配置（json 对象）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交';


-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

create table if not exists test
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    status      tinyint  default 0                 not null comment '状态 0-表示公开，1-表示私有，2-表示加密',
    password    varchar(128)                       null comment '密码',
    content     text                               null comment '内容',
    questionNum int      default 0                 not null comment '题目数量',
    beginTime   datetime                           null comment '开始时间',
    expiredTime datetime                           null comment '过期时间',
    examTime    bigint                             null comment '做题时间',
    totalScore  int      default 100               not null comment '总分',
    maxNum      int      default 9999              not null comment '最大人数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '试卷' collate = utf8mb4_unicode_ci;

-- 判断题目表
create table if not exists true_or_false
(
    id          bigint auto_increment comment 'id' primary key,
    content     text                               null comment '内容',
    answer      tinyint  default 0                 not null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '判断题目' collate = utf8mb4_unicode_ci;

-- 选择题目表
create table if not exists choice_question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    optionA     varchar(512)                       null comment '选项 A',
    optionB     varchar(512)                       null comment '选项 B',
    optionC     varchar(512)                       null comment '选项 C',
    optionD     varchar(512)                       null comment '选项 D',
    answer      tinyint  default 0                 null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '选择题目' collate = utf8mb4_unicode_ci;

-- 试卷题目关联表
create table if not exists test_question
(
    id         bigint auto_increment comment 'id' primary key,
    testId     bigint                             not null comment '试卷 id',
    questionId bigint                             not null comment '题目 id',
    score      int      default 0                 not null comment '题目分数',
    type       tinyint  default 0                 not null comment '类型 0-判断题，1-选择题，2-编程题',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_testId (testId),
    index idx_questionId (questionId)
) comment '题目题目关联' collate = utf8mb4_unicode_ci;

-- 试卷提交表
create table if not exists test_submit
(
    id         bigint auto_increment comment 'id' primary key,
    status     tinyint  default 0                 not null comment '提交状态 0-表示未提交，1-表示正在提交，2-表示提交成功，3-表示提交失败',
    score      int      default 0                 not null comment '得分',
    wrongQuestions text                           null comment '错误题目（json对象 List questionId type）',
    testId     bigint                             not null comment '试卷 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (testId),
    index idx_userId (userId)
) comment '题目提交';

-- 判断题题目提交表
create table if not exists true_or_false_submit
(
    id         bigint auto_increment comment 'id' primary key,
    answer     tinyint  default 0                 not null comment '答案 0 未填写 1 错误 2正确',
    status     int      default 0                 not null comment '判题状态（0 - 未判题、1 - 错误、2 - 正确）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    testId     bigint                             null comment '试卷 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId),
    index idx_testId (testId)
) comment '判断题提交信息';

-- 选择题题目提交表
create table if not exists choice_question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    answer     tinyint  default 0                 not null comment '答案 0 未填写 1 A 2B 3C 4D',
    status     int      default 0                 not null comment '判题状态（0 - 未判题、1 - 错误、2 - 正确）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    testId     bigint                             null comment '试卷 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId),
    index idx_testId (testId)
) comment '选择题提交信息';

-- 试卷提交表
create table if not exists test_submit
(
    id         bigint auto_increment comment 'id' primary key,
    trueOrFalseAnswers  text                      null comment '判断题答案（json 对象）',
    choiceQuestionAnswers  text                   null comment '选择题答案（json 对象）',
    questionAnswers     text                      null comment '编程题答案（json 对象）',
    status     tinyint      default 0             not null comment '判题状态（0 - 待判题、1 - 判题中、2 -已判题）',
    score      int          default 0             not null comment '得分',
    testId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (testId),
    index idx_userId (userId)
) comment '试卷提交';


-- 试卷用户关联表
create table if not exists test_user
(
    id         bigint auto_increment comment 'id' primary key,
    testId     bigint                             not null comment '试卷 id',
    userId     bigint                             not null comment '用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_testId (testId),
    index idx_questionId (userId)
) comment '试卷题目关联' collate = utf8mb4_unicode_ci;


-- 错题表
create table if not exists question_wrong
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '错题表';


-- 标签表
create table if not exists tag
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(512)                       null comment '标题',
    pid         bigint                             null comment '父标签id',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '标签' collate = utf8mb4_unicode_ci;