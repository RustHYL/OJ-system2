/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_Page_QuestionWrongVO_ } from '../models/BaseResponse_Page_QuestionWrongVO_';
import type { QuestionWrongQueryRequest } from '../models/QuestionWrongQueryRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class QuestionWrongControllerService {

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
