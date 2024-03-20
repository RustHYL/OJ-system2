/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_ChoiceQuestion_ } from '../models/BaseResponse_ChoiceQuestion_';
import type { BaseResponse_ChoiceQuestionVO_ } from '../models/BaseResponse_ChoiceQuestionVO_';
import type { BaseResponse_List_ChoiceQuestion_ } from '../models/BaseResponse_List_ChoiceQuestion_';
import type { BaseResponse_List_ChoiceQuestionSubmit_ } from '../models/BaseResponse_List_ChoiceQuestionSubmit_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_ChoiceQuestion_ } from '../models/BaseResponse_Page_ChoiceQuestion_';
import type { BaseResponse_Page_ChoiceQuestionSubmitVO_ } from '../models/BaseResponse_Page_ChoiceQuestionSubmitVO_';
import type { BaseResponse_Page_ChoiceQuestionVO_ } from '../models/BaseResponse_Page_ChoiceQuestionVO_';
import type { ChoiceQuestionAddAdminRequest } from '../models/ChoiceQuestionAddAdminRequest';
import type { ChoiceQuestionAddRequest } from '../models/ChoiceQuestionAddRequest';
import type { ChoiceQuestionEditRequest } from '../models/ChoiceQuestionEditRequest';
import type { ChoiceQuestionQueryRequest } from '../models/ChoiceQuestionQueryRequest';
import type { ChoiceQuestionSubmitAddRequest } from '../models/ChoiceQuestionSubmitAddRequest';
import type { ChoiceQuestionSubmitQueryAdminRequest } from '../models/ChoiceQuestionSubmitQueryAdminRequest';
import type { ChoiceQuestionSubmitQueryRequest } from '../models/ChoiceQuestionSubmitQueryRequest';
import type { ChoiceQuestionSubmitUpdateAdminRequest } from '../models/ChoiceQuestionSubmitUpdateAdminRequest';
import type { ChoiceQuestionUpdateAdminRequest } from '../models/ChoiceQuestionUpdateAdminRequest';
import type { ChoiceQuestionUpdateRequest } from '../models/ChoiceQuestionUpdateRequest';
import type { DeleteRequest } from '../models/DeleteRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class ChoiceQuestionControllerService {

    /**
     * addChoiceQuestion
     * @param choiceQuestionAddRequest choiceQuestionAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addChoiceQuestionUsingPost(
choiceQuestionAddRequest: ChoiceQuestionAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/add',
            body: choiceQuestionAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * addChoiceQuestionBackend
     * @param choiceQuestionAddRequest choiceQuestionAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addChoiceQuestionBackendUsingPost(
choiceQuestionAddRequest: ChoiceQuestionAddAdminRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/add/backend',
            body: choiceQuestionAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteChoiceQuestionSubmit
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteChoiceQuestionSubmitUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/choiceQuestion_submit/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * doChoiceQuestionSubmit
     * @param choiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doChoiceQuestionSubmitUsingPost(
choiceQuestionSubmitAddRequest: ChoiceQuestionSubmitAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/choiceQuestion_submit/do',
            body: choiceQuestionSubmitAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listChoiceQuestionSubmit
     * @param choiceQuestionSubmitQueryRequest choiceQuestionSubmitQueryRequest
     * @returns BaseResponse_List_ChoiceQuestionSubmit_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listChoiceQuestionSubmitUsingPost(
choiceQuestionSubmitQueryRequest: ChoiceQuestionSubmitQueryAdminRequest,
): CancelablePromise<BaseResponse_List_ChoiceQuestionSubmit_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/choiceQuestion_submit/list',
            body: choiceQuestionSubmitQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listChoiceQuestionSubmitByPage
     * @param choiceQuestionSubmitQueryRequest choiceQuestionSubmitQueryRequest
     * @returns BaseResponse_Page_ChoiceQuestionSubmitVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listChoiceQuestionSubmitByPageUsingPost(
choiceQuestionSubmitQueryRequest: ChoiceQuestionSubmitQueryRequest,
): CancelablePromise<BaseResponse_Page_ChoiceQuestionSubmitVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/choiceQuestion_submit/list/page',
            body: choiceQuestionSubmitQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateChoiceQuestionSubmitBackend
     * @param choiceQuestionSubmitUpdateRequest choiceQuestionSubmitUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateChoiceQuestionSubmitBackendUsingPost(
choiceQuestionSubmitUpdateRequest: ChoiceQuestionSubmitUpdateAdminRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/choiceQuestion_submit/update/backend',
            body: choiceQuestionSubmitUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteChoiceQuestion
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteChoiceQuestionUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * editChoiceQuestion
     * @param choiceQuestionEditRequest choiceQuestionEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editChoiceQuestionUsingPost(
choiceQuestionEditRequest: ChoiceQuestionEditRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/edit',
            body: choiceQuestionEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getChoiceQuestionById
     * @param id id
     * @returns BaseResponse_ChoiceQuestion_ OK
     * @throws ApiError
     */
    public static getChoiceQuestionByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_ChoiceQuestion_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/choiceQuestion/get',
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
     * getChoiceQuestionVOById
     * @param id id
     * @returns BaseResponse_ChoiceQuestionVO_ OK
     * @throws ApiError
     */
    public static getChoiceQuestionVoByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_ChoiceQuestionVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/choiceQuestion/get/vo',
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
     * listChoiceQuestionByList
     * @param choiceQuestionQueryRequest choiceQuestionQueryRequest
     * @returns BaseResponse_List_ChoiceQuestion_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listChoiceQuestionByListUsingPost(
choiceQuestionQueryRequest: ChoiceQuestionQueryRequest,
): CancelablePromise<BaseResponse_List_ChoiceQuestion_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/list',
            body: choiceQuestionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMyErrorChoiceQuestion
     * @returns BaseResponse_List_ChoiceQuestion_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyErrorChoiceQuestionUsingPost(): CancelablePromise<BaseResponse_List_ChoiceQuestion_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/list/my/error',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listChoiceQuestionByPage
     * @param choiceQuestionQueryRequest choiceQuestionQueryRequest
     * @returns BaseResponse_Page_ChoiceQuestion_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listChoiceQuestionByPageUsingPost(
choiceQuestionQueryRequest: ChoiceQuestionQueryRequest,
): CancelablePromise<BaseResponse_Page_ChoiceQuestion_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/list/page',
            body: choiceQuestionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listChoiceQuestionVOByPage
     * @param choiceQuestionQueryRequest choiceQuestionQueryRequest
     * @returns BaseResponse_Page_ChoiceQuestionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listChoiceQuestionVoByPageUsingPost(
choiceQuestionQueryRequest: ChoiceQuestionQueryRequest,
): CancelablePromise<BaseResponse_Page_ChoiceQuestionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/list/page/vo',
            body: choiceQuestionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMyChoiceQuestionVOByPage
     * @param choiceQuestionQueryRequest choiceQuestionQueryRequest
     * @returns BaseResponse_Page_ChoiceQuestionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyChoiceQuestionVoByPageUsingPost(
choiceQuestionQueryRequest: ChoiceQuestionQueryRequest,
): CancelablePromise<BaseResponse_Page_ChoiceQuestionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/my/list/page/vo',
            body: choiceQuestionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateChoiceQuestion
     * @param choiceQuestionUpdateRequest choiceQuestionUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateChoiceQuestionUsingPost(
choiceQuestionUpdateRequest: ChoiceQuestionUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/update',
            body: choiceQuestionUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateChoiceQuestionBackend
     * @param choiceQuestionUpdateRequest choiceQuestionUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateChoiceQuestionBackendUsingPost(
choiceQuestionUpdateRequest: ChoiceQuestionUpdateAdminRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/choiceQuestion/update/backend',
            body: choiceQuestionUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}
