import axios from 'axios';

export function userUploadApi(
    data: FormData,
    config: {
        controller: AbortController;
        onUploadProgress?: (progressEvent: any) => void;
    }
) {
    // const controller = new AbortController();
    return axios.post('/api/user/upload', data, config);
}