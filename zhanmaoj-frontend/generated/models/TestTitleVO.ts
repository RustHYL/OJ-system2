/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ChoiceQuestionTitleVO } from './ChoiceQuestionTitleVO';
import type { QuestionTitleVO } from './QuestionTitleVO';
import type { TrueOrFalseTitleVO } from './TrueOrFalseTitleVO';

export type TestTitleVO = {
    choiceQuestionTitleVOS?: Array<ChoiceQuestionTitleVO>;
    content?: string;
    id?: number;
    questionNum?: number;
    questionTitleVOS?: Array<QuestionTitleVO>;
    title?: string;
    totalScore?: number;
    trueOrFalseTitleVOS?: Array<TrueOrFalseTitleVO>;
};
