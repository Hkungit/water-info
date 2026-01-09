<template>
  <el-container class="layout">
    <el-aside width="220px" class="sidebar">
      <div class="brand">Water Info</div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/">监测概览</el-menu-item>
        <el-menu-item v-if="isAdmin" index="/users">权限管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-right">
          <el-tag type="info">{{ roleLabel }}</el-tag>
          <span class="username">{{ user?.realName || user?.username }}</span>
          <el-button type="primary" text @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuth } from '../composables/useAuth';

const route = useRoute();
const router = useRouter();
const { user, logout } = useAuth();

const roleLabelMap: Record<string, string> = {
  admin: '管理员',
  operator: '运维人员',
  viewer: '访客',
};

const roleLabel = computed(() => roleLabelMap[user.value?.role ?? 'viewer']);
const isAdmin = computed(() => user.value?.role === 'admin');
const activeMenu = computed(() => route.path);

const handleLogout = async () => {
  await logout();
  router.push({ name: 'login' });
};
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.sidebar {
  background: #001529;
  color: #fff;
}

.brand {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
  color: #fff;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-weight: 500;
}

.content {
  padding: 24px;
}
</style>
