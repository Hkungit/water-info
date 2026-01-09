export type UserRole = 'admin' | 'operator' | 'viewer';

export interface AuthUser {
  id: number;
  username: string;
  realName: string;
  email?: string | null;
  phone?: string | null;
  role: UserRole;
  status: number;
  lastLoginAt?: string | null;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresIn: number;
  user: AuthUser;
}

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}
