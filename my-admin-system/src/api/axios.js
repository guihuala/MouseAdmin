import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // 携带 cookie
});

// 设置请求拦截器
axiosInstance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('jwt_token');
        if (token) {
            config.headers['Authorization'] = token;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 设置响应拦截器
axiosInstance.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        // 统一处理错误
        if (error.response) {
            console.error('请求失败:', error.response);
        } else {
            console.error('请求失败:', error.message);
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
