/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_List_ChoiceQuestionTestDetailVO_ } from '../models/BaseResponse_List_ChoiceQuestionTestDetailVO_';
import type { BaseResponse_List_MyTestVO_ } from '../models/BaseResponse_List_MyTestVO_';
import type { BaseResponse_List_QuestionTestDetailVO_ } from '../models/BaseResponse_List_QuestionTestDetailVO_';
import type { BaseResponse_List_Test_ } from '../models/BaseResponse_List_Test_';
import type { BaseResponse_List_TestSubmit_ } from '../models/BaseResponse_List_TestSubmit_';
import type { BaseResponse_List_TestTitleIdVO_ } from '../models/BaseResponse_List_TestTitleIdVO_';
import type { BaseResponse_List_TrueOrFalseTestDetailVO_ } from '../models/BaseResponse_List_TrueOrFalseTestDetailVO_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_MyTestVO_ } from '../models/BaseResponse_Page_MyTestVO_';
import type { BaseResponse_Test_ } from '../models/BaseResponse_Test_';
import type { BaseResponse_TestSubmitFinalDetailVO_ } from '../models/BaseResponse_TestSubmitFinalDetailVO_';
import type { BaseResponse_TestTitleVO_ } from '../models/BaseResponse_TestTitleVO_';
import type { BaseResponse_TestVO_ } from '../models/BaseResponse_TestVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { MyTestQueryRequest } from '../models/MyTestQueryRequest';
import type { TestAddRequest } from '../models/TestAddRequest';
import type { TestJoinRequest } from '../models/TestJoinRequest';
import type { TestQueryRequest } from '../models/TestQueryRequest';
import type { TestSubmitAddRequest } from '../models/TestSubmitAddRequest';
import type { TestSubmitQueryRequest } from '../models/TestSubmitQueryRequest';
import type { TestSubmitUpdateRequest } from '../models/TestSubmitUpdateRequest';
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
     * getTestSubmitById
     * @param testId testId
     * @returns BaseResponse_Test_ OK
     * @throws ApiError
     */
    public static getTestSubmitByIdUsingGet(
testId?: string,
): CancelablePromise<BaseResponse_Test_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/get',
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
     * GetChoiceQuestionDetail
     * @param testId testId
     * @returns BaseResponse_List_ChoiceQuestionTestDetailVO_ OK
     * @throws ApiError
     */
    public static getChoiceQuestionDetailUsingGet(
testId?: string,
): CancelablePromise<BaseResponse_List_ChoiceQuestionTestDetailVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/get/choiceQuestion',
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
     * GetTestDetail
     * @param testId testId
     * @returns BaseResponse_TestVO_ OK
     * @throws ApiError
     */
    public static getTestDetailUsingGet(
testId?: string,
): CancelablePromise<BaseResponse_TestVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/get/detail',
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
     * GetQuestionDetail
     * @param testId testId
     * @returns BaseResponse_List_QuestionTestDetailVO_ OK
     * @throws ApiError
     */
    public static getQuestionDetailUsingGet(
testId?: string,
): CancelablePromise<BaseResponse_List_QuestionTestDetailVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/get/question',
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
     * GetTestTrueOeFalseDetail
     * @param testId testId
     * @returns BaseResponse_List_TrueOrFalseTestDetailVO_ OK
     * @throws ApiError
     */
    public static getTestTrueOeFalseDetailUsingGet(
testId?: string,
): CancelablePromise<BaseResponse_List_TrueOrFalseTestDetailVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/get/trueOrFalse',
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
     * GetWrongTestDetail
     * @param testId testId
     * @returns BaseResponse_TestVO_ OK
     * @throws ApiError
     */
    public static getWrongTestDetailUsingGet(
testId?: string,
): CancelablePromise<BaseResponse_TestVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/get/wrong/detail',
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
            url: '/api/test/join',
            body: testJoinRequest,
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
     * listTestVOMine
     * @returns BaseResponse_List_MyTestVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTestVoMineUsingPost(): CancelablePromise<BaseResponse_List_MyTestVO_ | any> {
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
     * listTestVOMinePage
     * @param myTestQueryRequest myTestQueryRequest
     * @returns BaseResponse_Page_MyTestVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTestVoMinePageUsingPost(
myTestQueryRequest: MyTestQueryRequest,
): CancelablePromise<BaseResponse_Page_MyTestVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/list/mine/page/vo',
            body: myTestQueryRequest,
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
     * GetTestTitle
     * @param testId testId
     * @returns BaseResponse_TestTitleVO_ OK
     * @throws ApiError
     */
    public static getTestTitleUsingGet(
testId?: number,
): CancelablePromise<BaseResponse_TestTitleVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/test/title',
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
     * deleteTestSubmit
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteTestSubmitUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/test_submit/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * doTestSubmit
     * @param testSubmitAddRequest testSubmitAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doTestSubmitUsingPost(
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
     * getFinalDetail
     * @param testSubmitId testSubmitId
     * @returns BaseResponse_TestSubmitFinalDetailVO_ OK
     * @throws ApiError
     */
    public static getFinalDetailUsingGet(
testSubmitId?: number,
): CancelablePromise<BaseResponse_TestSubmitFinalDetailVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/test/test_submit/get/final_detail',
            query: {
                'testSubmitId': testSubmitId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listTestSubmit
     * @param testSubmitQueryRequest testSubmitQueryRequest
     * @returns BaseResponse_List_TestSubmit_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTestSubmitUsingPost(
testSubmitQueryRequest: TestSubmitQueryRequest,
): CancelablePromise<BaseResponse_List_TestSubmit_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/test_submit/list',
            body: testSubmitQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateTestSubmitBackend
     * @param testSubmitUpdateRequest testSubmitUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTestSubmitBackendUsingPost(
testSubmitUpdateRequest: TestSubmitUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/test_submit/update/backend',
            body: testSubmitUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTestTitleIdList
     * @returns BaseResponse_List_TestTitleIdVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static getTestTitleIdListUsingPost(): CancelablePromise<BaseResponse_List_TestTitleIdVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/test/title/id/list',
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
