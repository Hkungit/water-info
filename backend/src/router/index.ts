import { createRouter, createWebHistory } from 'vue-router';
import { useAuth } from '../composables/useAuth';
import AppLayout from '../components/AppLayout.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/forbidden',
      name: 'forbidden',
      component: () => import('../views/ForbiddenView.vue'),
    },
    {
      path: '/',
      component: AppLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('../views/DashboardView.vue'),
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('../views/UsersView.vue'),
          meta: { roles: ['admin'] },
        },
      ],
    },
  ],
});

router.beforeEach(async (to) => {
  const { initialize, isAuthenticated, user } = useAuth();
  await initialize();

  if (to.meta.requiresAuth && !isAuthenticated.value) {
    return { name: 'login' };
  }

  const roles = to.meta.roles as string[] | undefined;
  if (roles && (!user.value || !roles.includes(user.value.role))) {
    return { name: 'forbidden' };
  }

  if (to.name === 'login' && isAuthenticated.value) {
    return { name: 'dashboard' };
  }

  return true;
});

export default router;
