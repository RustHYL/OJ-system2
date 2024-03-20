/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ChoiceQuestion } from './ChoiceQuestion';
import type { OrderItem } from './OrderItem';

export type Page_ChoiceQuestion_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<ChoiceQuestion>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
