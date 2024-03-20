/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { OrderItem } from './OrderItem';
import type { TrueOrFalseSubmitVO } from './TrueOrFalseSubmitVO';

export type Page_TrueOrFalseSubmitVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<TrueOrFalseSubmitVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
