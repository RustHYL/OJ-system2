/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ChoiceQuestionVO } from './ChoiceQuestionVO';
import type { OrderItem } from './OrderItem';

export type Page_ChoiceQuestionVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<ChoiceQuestionVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
