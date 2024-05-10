/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_Page_QuestionWrongVO_ } from '../models/BaseResponse_Page_QuestionWrongVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { QuestionWrongQueryRequest } from '../models/QuestionWrongQueryRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class QuestionWrongControllerService {

    /**
     * deleteQuestionWrong
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteQuestionWrongUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/question_wrong/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMyQuestionWrongVOByPage
     * @param questionWrongQueryRequest questionWrongQueryRequest
     * @returns BaseResponse_Page_QuestionWrongVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyQuestionWrongVoByPageUsingPost(
questionWrongQueryRequest: QuestionWrongQueryRequest,
): CancelablePromise<BaseResponse_Page_QuestionWrongVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/question_wrong/list/my/page/vo',
            body: questionWrongQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}
