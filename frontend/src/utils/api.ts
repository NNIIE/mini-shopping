const API_BASE_URL = 'http://localhost:8082';

interface RequestOptions extends RequestInit {
  skipAuth?: boolean;
}

class ApiClient {
  private baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  private async request<T>(endpoint: string, options: RequestOptions = {}): Promise<T> {
    const { skipAuth = false, ...fetchOptions } = options;
    
    const config: RequestInit = {
      ...fetchOptions,
      headers: {
        'Content-Type': 'application/json',
        ...fetchOptions.headers,
      },
    };

    // 토큰 자동 추가 (skipAuth가 false인 경우)
    if (!skipAuth) {
      const accessToken = localStorage.getItem('accessToken');
      if (accessToken) {
        config.headers = {
          ...config.headers,
          'Authorization': `Bearer ${accessToken}`,
        };
      }
    }

    const response = await fetch(`${this.baseURL}${endpoint}`, config);

    // 401 에러 처리 (토큰 만료)
    if (response.status === 401) {
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken && !endpoint.includes('reissueToken')) {
        // 토큰 재발급 시도
        const refreshResponse = await fetch(`${this.baseURL}/user/reissueToken`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ refreshToken }),
        });

        if (refreshResponse.ok) {
          const newAccessToken = await refreshResponse.text();
          localStorage.setItem('accessToken', newAccessToken);
          
          // 원래 요청 재시도
          config.headers = {
            ...config.headers,
            'Authorization': `Bearer ${newAccessToken}`,
          };
          const retryResponse = await fetch(`${this.baseURL}${endpoint}`, config);
          
          if (!retryResponse.ok) {
            throw new Error('Request failed after token refresh');
          }
          
          return retryResponse.json();
        } else {
          // 리프레시 토큰도 만료된 경우 로그아웃
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          localStorage.removeItem('user');
          window.location.href = '/login';
          throw new Error('Session expired. Please login again.');
        }
      } else {
        // 토큰이 없거나 이미 재발급 요청인 경우
        if (!endpoint.includes('signIn') && !endpoint.includes('signUp')) {
          window.location.href = '/login';
        }
        throw new Error('Authentication required');
      }
    }

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'Request failed' }));
      throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    return response.json();
  }

  // GET 요청
  async get<T>(endpoint: string, options?: RequestOptions): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'GET',
    });
  }

  // POST 요청
  async post<T>(endpoint: string, data?: any, options?: RequestOptions): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  // PUT 요청
  async put<T>(endpoint: string, data?: any, options?: RequestOptions): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  // DELETE 요청
  async delete<T>(endpoint: string, options?: RequestOptions): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'DELETE',
    });
  }
}

// API 클라이언트 인스턴스 생성
const apiClient = new ApiClient(API_BASE_URL);

export default apiClient;

// 사용 예시:
// import apiClient from '@/utils/api';
// 
// // 인증이 필요한 요청 (토큰 자동 포함)
// const products = await apiClient.get('/api/products');
// 
// // 인증이 필요없는 요청
// const result = await apiClient.post('/user/signIn', { email, password }, { skipAuth: true });