import { computed, ref } from 'vue';
import type { AuthUser, LoginResponse } from '../types/auth';
import { request } from '../api/client';

const user = ref<AuthUser | null>(null);
const token = ref<string | null>(localStorage.getItem('auth_token'));
const initialized = ref(false);

export function useAuth() {
  const isAuthenticated = computed(() => Boolean(token.value && user.value));

  const initialize = async () => {
    if (initialized.value) {
      return;
    }
    initialized.value = true;
    if (token.value) {
      try {
        user.value = await request<AuthUser>('/auth/me');
      } catch (error) {
        clearAuth();
      }
    }
  };

  const login = async (username: string, password: string) => {
    const response = await request<LoginResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });
    token.value = response.token;
    localStorage.setItem('auth_token', response.token);
    user.value = response.user;
  };

  const logout = async () => {
    if (token.value) {
      try {
        await request<void>('/auth/logout', { method: 'POST' });
      } catch (error) {
        // ignore logout errors
      }
    }
    clearAuth();
  };

  const clearAuth = () => {
    token.value = null;
    user.value = null;
    localStorage.removeItem('auth_token');
  };

  return {
    user,
    token,
    isAuthenticated,
    initialize,
    login,
    logout,
  };
}
