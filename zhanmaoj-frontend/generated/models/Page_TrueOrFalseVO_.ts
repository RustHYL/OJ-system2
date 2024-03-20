/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { OrderItem } from './OrderItem';
import type { TrueOrFalseVO } from './TrueOrFalseVO';

export type Page_TrueOrFalseVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<TrueOrFalseVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
