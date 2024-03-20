/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_List_Test_ } from '../models/BaseResponse_List_Test_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { TestAddRequest } from '../models/TestAddRequest';
import type { TestJoinRequest } from '../models/TestJoinRequest';
import type { TestQueryRequest } from '../models/TestQueryRequest';
import type { TestSubmitAddRequest } from '../models/TestSubmitAddRequest';
import type { TestUpdateRequest } from '../models/TestUpdateRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class TestControllerService {

    /**
     * addTest
     * @param testAddRequest testAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTestUsingPost1(
testAddRequest: TestAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/add',
            body: testAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * addTest
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTestUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTest
     * @param testQueryRequest testQueryRequest
     * @returns BaseResponse_List_Test_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTestUsingPost(
testQueryRequest: TestQueryRequest,
): CancelablePromise<BaseResponse_List_Test_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/list',
            body: testQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTestMine
     * @returns BaseResponse_List_Test_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTestMineUsingPost(): CancelablePromise<BaseResponse_List_Test_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/list/mine',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTestSimple
     * @returns BaseResponse_List_Test_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTestSimpleUsingPost(): CancelablePromise<BaseResponse_List_Test_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/list/simple',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * JoinTest
     * @param testJoinRequest testJoinRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static joinTestUsingPost(
testJoinRequest: TestJoinRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/test/join',
            body: testJoinRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * doTrueOrFalseSubmit
     * @param testSubmitAddRequest testSubmitAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doTrueOrFalseSubmitUsingPost(
testSubmitAddRequest: TestSubmitAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/test_submit/do',
            body: testSubmitAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateTest
     * @param testUpdateRequest testUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTestUsingPost(
testUpdateRequest: TestUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/update',
            body: testUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}
