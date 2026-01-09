<template>
  <el-result
    icon="warning"
    title="无权限访问"
    sub-title="请联系管理员提升权限，或返回首页。"
  >
    <template #extra>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </template>
  </el-result>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push({ name: 'dashboard' });
};
</script>
