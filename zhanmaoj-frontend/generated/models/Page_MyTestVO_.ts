/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { MyTestVO } from './MyTestVO';
import type { OrderItem } from './OrderItem';

export type Page_MyTestVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<MyTestVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};
