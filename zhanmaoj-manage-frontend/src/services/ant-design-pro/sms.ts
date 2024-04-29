import request from "@/plugins/globalRequest";

export async function sendSMS(
    sms?: API.SMS,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<string>>('/api/sms/sendSMS', {
        method: 'POST',
        data: {
            ...sms
        },
        ...(options || {})
    });
}


export async function validateSMS(
    sms?: API.SMS,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/sms/validateSMS', {
        method: 'POST',
        data: {
            ...sms
        },
        ...(options || {})
    });
}
