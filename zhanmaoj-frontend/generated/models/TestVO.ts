/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ChoiceQuestionTestDetailVO } from './ChoiceQuestionTestDetailVO';
import type { QuestionTestDetailVO } from './QuestionTestDetailVO';
import type { TrueOrFalseTestDetailVO } from './TrueOrFalseTestDetailVO';

export type TestVO = {
    choiceQuestionTestDetailVOS?: Array<ChoiceQuestionTestDetailVO>;
    content?: string;
    examTime?: number;
    id?: number;
    questionNum?: number;
    questionTestDetailVOS?: Array<QuestionTestDetailVO>;
    title?: string;
    totalScore?: number;
    trueOrFalseTestDetailVOS?: Array<TrueOrFalseTestDetailVO>;
};
