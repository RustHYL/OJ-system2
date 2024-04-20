/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeInfo } from './JudgeInfo';

export type QuestionWrongVO = {
    code?: string;
    createTime?: string;
    current?: number;
    id?: number;
    judgeInfo?: JudgeInfo;
    language?: string;
    pageSize?: number;
    questionId?: number;
    sortField?: string;
    sortOrder?: string;
    submitId?: number;
    tags?: Array<string>;
    title?: string;
    updateTime?: string;
    userId?: number;
};
