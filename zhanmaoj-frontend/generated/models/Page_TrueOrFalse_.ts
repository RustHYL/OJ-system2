/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { OrderItem } from './OrderItem';
import type { TrueOrFalse } from './TrueOrFalse';

export type Page_TrueOrFalse_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<TrueOrFalse>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
