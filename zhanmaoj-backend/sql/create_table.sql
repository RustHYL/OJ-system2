# 数据库初始化

-- 创建库
create database if not exists zhanmaoj;

-- 切换库
use zhanmaoj;

-- 用户表
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    phone        varchar(128)                           null comment '手机号码',
    email        varchar(255)                           null comment '用户邮箱',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户' collate = utf8mb4_unicode_ci;

create index idx_unionId
    on user (unionId);



-- 题目表
create table question
(
    id          bigint auto_increment comment 'id'
        primary key,
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
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '题目' collate = utf8mb4_unicode_ci;

create index idx_userId
    on question (userId);



-- 题目提交表
create table question_submit
(
    id         bigint auto_increment comment 'id'
        primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    testId     bigint                             null comment '试卷id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '题目提交';

create index idx_questionId
    on question_submit (questionId);

create index idx_userId
    on question_submit (userId);



-- 试卷
create table test
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       varchar(512)                       null comment '标题',
    status      tinyint  default 0                 not null comment '状态 0-表示公开，1--表示加密',
    password    varchar(128)                       null comment '密码',
    content     text                               null comment '内容',
    questionNum int      default 0                 not null comment '题目数量',
    beginTime   datetime                           null comment '开始时间',
    expiredTime datetime                           null comment '过期时间',
    examTime    bigint                             null comment '做题时间',
    totalScore  int      default 100               not null comment '总分',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '试卷' collate = utf8mb4_unicode_ci;

create index idx_userId
    on test (userId);



-- 判断题目表
create table true_or_false
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       text                               null comment '题目',
    answer      tinyint  default 0                 null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '判断题目' collate = utf8mb4_unicode_ci;

create index idx_userId
    on true_or_false (userId);



-- 选择题目表
create table choice_question
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    optionA     varchar(512)                       null comment '选项 A',
    optionB     varchar(512)                       null comment '选项 B',
    optionC     varchar(512)                       null comment '选项 C',
    optionD     varchar(512)                       null comment '选项 D',
    answer      tinyint  default 0                 not null comment '题目答案 0 未填写 1 错误 2 正确',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '选择题目' collate = utf8mb4_unicode_ci;

create index idx_userId
    on choice_question (userId);

-- 试卷题目关联表
create table test_question
(
    id         bigint auto_increment comment 'id'
        primary key,
    testId     bigint                             not null comment '试卷 id',
    questionId bigint                             not null comment '题目 id',
    score      int      default 0                 not null comment '题目分数',
    type       tinyint  default 0                 not null comment '类型 0-判断题，1-选择题，2-编程题',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '题目题目关联' collate = utf8mb4_unicode_ci;

create index idx_questionId
    on test_question (questionId);

create index idx_testId
    on test_question (testId);



-- 试卷提交表
create table test_submit
(
    id         bigint auto_increment comment 'id'
        primary key,
    status     tinyint  default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 -成功、3-失败）',
    score      int      default 0                 not null comment '得分',
    testId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    beginTime  datetime                           null comment '开始做卷时间',
    endTime    datetime                           null comment '结束做卷时间',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '试卷提交';

create index idx_questionId
    on test_submit (testId);

create index idx_userId
    on test_submit (userId);



-- 判断题题目提交表
create table true_or_false_submit
(
    id         bigint auto_increment comment 'id'
        primary key,
    answer     tinyint  default 0                 null comment '答案 0 没有填写 1错误 2正确',
    status     int      default 0                 not null comment '判题状态 0-未判题 1-错误 2-成功',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    testId     bigint                             null comment '试卷 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '判断题提交信息';

create index idx_questionId
    on true_or_false_submit (questionId);

create index idx_testId
    on true_or_false_submit (testId);

create index idx_userId
    on true_or_false_submit (userId);



-- 选择题题目提交表
create table choice_question_submit
(
    id         bigint auto_increment comment 'id'
        primary key,
    answer     tinyint                            null comment '答案 （0 - 未填写、1 - A、2 - B、3 - C、4 - D）',
    status     tinyint  default 0                 not null comment '判题状态 0-未判题 1-错误 2-成功',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    testId     bigint                             null comment '试卷 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '选择题提交信息';

create index idx_questionId
    on choice_question_submit (questionId);

create index idx_testId
    on choice_question_submit (testId);

create index idx_userId
    on choice_question_submit (userId);



-- 试卷用户关联表
create table test_user
(
    id         bigint auto_increment comment 'id'
        primary key,
    testId     bigint                             not null comment '试卷 id',
    userId     bigint                             not null comment '用户 id',
    score      int      default 0                 not null comment '分数',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '试卷题目关联' collate = utf8mb4_unicode_ci;

create index idx_questionId
    on test_user (userId);

create index idx_testId
    on test_user (testId);




-- 错题表
create table question_wrong
(
    id         bigint auto_increment comment 'id'
        primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    submitId   bigint                             not null comment '提交id',
    title      varchar(255)                       not null comment '题目',
    tags       varchar(255)                       null comment '标签'
)
    comment '错题表';

create index idx_questionId
    on question_wrong (questionId);

create index idx_userId
    on question_wrong (userId);
