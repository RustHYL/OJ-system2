/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { QuestionTestAddBackendVO } from './QuestionTestAddBackendVO';

export type TestAddRequest = {
    beginTime?: string;
    choiceQuestionNum?: number;
    choiceQuestionPerScore?: number;
    content?: string;
    examTime?: number;
    expiredTime?: string;
    password?: string;
    questionList?: Array<QuestionTestAddBackendVO>;
    status?: number;
    title?: string;
    trueOrFalseNum?: number;
    trueOrFalsePerScore?: number;
};
