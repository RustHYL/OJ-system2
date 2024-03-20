/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ChoiceQuestionSubmitVO } from './ChoiceQuestionSubmitVO';
import type { OrderItem } from './OrderItem';

export type Page_ChoiceQuestionSubmitVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<ChoiceQuestionSubmitVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
