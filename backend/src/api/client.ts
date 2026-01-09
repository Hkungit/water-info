import type { ApiResponse } from '../types/auth';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '/api/v1';

export class ApiError extends Error {
  constructor(message: string, public readonly code?: number) {
    super(message);
  }
}

export async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers ?? {});
  headers.set('Content-Type', 'application/json');

  const token = localStorage.getItem('auth_token');
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });

  let data: ApiResponse<T> | null = null;
  try {
    data = (await response.json()) as ApiResponse<T>;
  } catch (error) {
    data = null;
  }

  if (!response.ok) {
    throw new ApiError(data?.message || '请求失败', data?.code ?? response.status);
  }

  if (!data || data.code !== 200) {
    throw new ApiError(data?.message || '请求失败', data?.code);
  }
  return data.data;
}
