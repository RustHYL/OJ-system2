/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_List_IdTitleVO_ } from '../models/BaseResponse_List_IdTitleVO_';
import type { BaseResponse_List_TrueOrFalse_ } from '../models/BaseResponse_List_TrueOrFalse_';
import type { BaseResponse_List_TrueOrFalseSubmit_ } from '../models/BaseResponse_List_TrueOrFalseSubmit_';
import type { BaseResponse_List_TrueOrFalseTestAdminVO_ } from '../models/BaseResponse_List_TrueOrFalseTestAdminVO_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_TrueOrFalse_ } from '../models/BaseResponse_Page_TrueOrFalse_';
import type { BaseResponse_Page_TrueOrFalseSubmitVO_ } from '../models/BaseResponse_Page_TrueOrFalseSubmitVO_';
import type { BaseResponse_Page_TrueOrFalseVO_ } from '../models/BaseResponse_Page_TrueOrFalseVO_';
import type { BaseResponse_TrueOrFalse_ } from '../models/BaseResponse_TrueOrFalse_';
import type { BaseResponse_TrueOrFalseVO_ } from '../models/BaseResponse_TrueOrFalseVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { QueryRequest } from '../models/QueryRequest';
import type { TestQuestionUpdateRequest } from '../models/TestQuestionUpdateRequest';
import type { TrueOrFalseAddAdminRequest } from '../models/TrueOrFalseAddAdminRequest';
import type { TrueOrFalseAddRequest } from '../models/TrueOrFalseAddRequest';
import type { TrueOrFalseEditRequest } from '../models/TrueOrFalseEditRequest';
import type { TrueOrFalseQueryAdminRequest } from '../models/TrueOrFalseQueryAdminRequest';
import type { TrueOrFalseQueryRequest } from '../models/TrueOrFalseQueryRequest';
import type { TrueOrFalseQueryTestAdminRequest } from '../models/TrueOrFalseQueryTestAdminRequest';
import type { TrueOrFalseSubmitAddRequest } from '../models/TrueOrFalseSubmitAddRequest';
import type { TrueOrFalseSubmitQueryAdminRequest } from '../models/TrueOrFalseSubmitQueryAdminRequest';
import type { TrueOrFalseSubmitQueryRequest } from '../models/TrueOrFalseSubmitQueryRequest';
import type { TrueOrFalseSubmitUpdateAdminRequest } from '../models/TrueOrFalseSubmitUpdateAdminRequest';
import type { TrueOrFalseUpdateAdminRequest } from '../models/TrueOrFalseUpdateAdminRequest';
import type { TrueOrFalseUpdateRequest } from '../models/TrueOrFalseUpdateRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class TrueOrFalseControllerService {

    /**
     * addTrueOrFalse
     * @param trueOrFalseAddRequest trueOrFalseAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTrueOrFalseUsingPost(
trueOrFalseAddRequest: TrueOrFalseAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/add',
            body: trueOrFalseAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * addTrueOrFalseBackend
     * @param trueOrFalseAddRequest trueOrFalseAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTrueOrFalseBackendUsingPost(
trueOrFalseAddRequest: TrueOrFalseAddAdminRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/add/backend',
            body: trueOrFalseAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteTrueOrFalse
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteTrueOrFalseUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * editTrueOrFalse
     * @param trueOrFalseEditRequest trueOrFalseEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editTrueOrFalseUsingPost(
trueOrFalseEditRequest: TrueOrFalseEditRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/edit',
            body: trueOrFalseEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTrueOrFalseById
     * @param id id
     * @returns BaseResponse_TrueOrFalse_ OK
     * @throws ApiError
     */
    public static getTrueOrFalseByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_TrueOrFalse_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trueOrFalse/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTrueOrFalseScoreVOById
     * @param id id
     * @returns BaseResponse_TrueOrFalseVO_ OK
     * @throws ApiError
     */
    public static getTrueOrFalseScoreVoByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_TrueOrFalseVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trueOrFalse/get/score/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTrueOrFalseVOById
     * @param id id
     * @returns BaseResponse_TrueOrFalseVO_ OK
     * @throws ApiError
     */
    public static getTrueOrFalseVoByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_TrueOrFalseVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trueOrFalse/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTestQuestion
     * @returns BaseResponse_List_IdTitleVO_ OK
     * @throws ApiError
     */
    public static getTestQuestionUsingGet2(): CancelablePromise<BaseResponse_List_IdTitleVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trueOrFalse/idList',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTrueOrFalseByList
     * @param trueOrFalseQueryRequest trueOrFalseQueryRequest
     * @returns BaseResponse_List_TrueOrFalse_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTrueOrFalseByListUsingPost(
trueOrFalseQueryRequest: TrueOrFalseQueryAdminRequest,
): CancelablePromise<BaseResponse_List_TrueOrFalse_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/list',
            body: trueOrFalseQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMyErrorTrueOrFalse
     * @returns BaseResponse_List_TrueOrFalse_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyErrorTrueOrFalseUsingPost(): CancelablePromise<BaseResponse_List_TrueOrFalse_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/list/my/error',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTrueOrFalseByPage
     * @param trueOrFalseQueryRequest trueOrFalseQueryRequest
     * @returns BaseResponse_Page_TrueOrFalse_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTrueOrFalseByPageUsingPost(
trueOrFalseQueryRequest: TrueOrFalseQueryRequest,
): CancelablePromise<BaseResponse_Page_TrueOrFalse_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/list/page',
            body: trueOrFalseQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTrueOrFalseVOByPage
     * @param trueOrFalseQueryRequest trueOrFalseQueryRequest
     * @returns BaseResponse_Page_TrueOrFalseVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTrueOrFalseVoByPageUsingPost(
trueOrFalseQueryRequest: TrueOrFalseQueryRequest,
): CancelablePromise<BaseResponse_Page_TrueOrFalseVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/list/page/vo',
            body: trueOrFalseQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMyTrueOrFalseVOByPage
     * @param trueOrFalseQueryRequest trueOrFalseQueryRequest
     * @returns BaseResponse_Page_TrueOrFalseVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyTrueOrFalseVoByPageUsingPost(
trueOrFalseQueryRequest: TrueOrFalseQueryRequest,
): CancelablePromise<BaseResponse_Page_TrueOrFalseVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/my/list/page/vo',
            body: trueOrFalseQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * addTestTrueOrFalse
     * @param queryRequest queryRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTestTrueOrFalseUsingPost(
queryRequest: QueryRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/test/add',
            body: queryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteTestTrueOrFalse
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteTestTrueOrFalseUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/test/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTestQuestion
     * @param testId testId
     * @returns BaseResponse_List_IdTitleVO_ OK
     * @throws ApiError
     */
    public static getTestQuestionUsingGet1(
testId?: number,
): CancelablePromise<BaseResponse_List_IdTitleVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trueOrFalse/test/idList',
            query: {
                'testId': testId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTrueOrFalseTestByList
     * @param trueOrFalseQueryRequest trueOrFalseQueryRequest
     * @returns BaseResponse_List_TrueOrFalseTestAdminVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTrueOrFalseTestByListUsingPost(
trueOrFalseQueryRequest: TrueOrFalseQueryTestAdminRequest,
): CancelablePromise<BaseResponse_List_TrueOrFalseTestAdminVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/test/list',
            body: trueOrFalseQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateTestTrueOrFalse
     * @param testQuestionUpdateRequest testQuestionUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTestTrueOrFalseUsingPost(
testQuestionUpdateRequest: TestQuestionUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/test/update',
            body: testQuestionUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteTrueOrFalseSubmit
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteTrueOrFalseSubmitUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/trueOrFalse_submit/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * doTrueOrFalseSubmit
     * @param trueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doTrueOrFalseSubmitUsingPost(
trueOrFalseSubmitAddRequest: TrueOrFalseSubmitAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/trueOrFalse_submit/do',
            body: trueOrFalseSubmitAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTrueOrFalseSubmit
     * @param trueOrFalseSubmitQueryRequest trueOrFalseSubmitQueryRequest
     * @returns BaseResponse_List_TrueOrFalseSubmit_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTrueOrFalseSubmitUsingPost(
trueOrFalseSubmitQueryRequest: TrueOrFalseSubmitQueryAdminRequest,
): CancelablePromise<BaseResponse_List_TrueOrFalseSubmit_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/trueOrFalse_submit/list',
            body: trueOrFalseSubmitQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTrueOrFalseSubmitByPage
     * @param trueOrFalseSubmitQueryRequest trueOrFalseSubmitQueryRequest
     * @returns BaseResponse_Page_TrueOrFalseSubmitVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTrueOrFalseSubmitByPageUsingPost(
trueOrFalseSubmitQueryRequest: TrueOrFalseSubmitQueryRequest,
): CancelablePromise<BaseResponse_Page_TrueOrFalseSubmitVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/trueOrFalse_submit/list/page',
            body: trueOrFalseSubmitQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateTrueOrFalseSubmitBackend
     * @param trueOrFalseSubmitUpdateRequest trueOrFalseSubmitUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTrueOrFalseSubmitBackendUsingPost(
trueOrFalseSubmitUpdateRequest: TrueOrFalseSubmitUpdateAdminRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/trueOrFalse_submit/update/backend',
            body: trueOrFalseSubmitUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateTrueOrFalse
     * @param trueOrFalseUpdateRequest trueOrFalseUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTrueOrFalseUsingPost(
trueOrFalseUpdateRequest: TrueOrFalseUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/update',
            body: trueOrFalseUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateTrueOrFalseBackend
     * @param trueOrFalseUpdateRequest trueOrFalseUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTrueOrFalseBackendUsingPost(
trueOrFalseUpdateRequest: TrueOrFalseUpdateAdminRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trueOrFalse/update/backend',
            body: trueOrFalseUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}
