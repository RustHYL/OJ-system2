/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ChoiceQuestionVO } from './ChoiceQuestionVO';
import type { UserVO } from './UserVO';

export type ChoiceQuestionSubmitVO = {
    answer?: number;
    choiceQuestionId?: number;
    choiceQuestionVO?: ChoiceQuestionVO;
    createTime?: string;
    current?: number;
    id?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    testId?: number;
    updateTime?: string;
    userId?: number;
    userVO?: UserVO;
};
