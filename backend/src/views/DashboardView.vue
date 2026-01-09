<template>
  <el-row :gutter="16">
    <el-col :span="12">
      <el-card>
        <template #header>
          <div class="card-title">当前用户</div>
        </template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户名">{{ user?.username }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ user?.realName }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{ roleLabel }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ user?.status === 1 ? '启用' : '停用' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card>
        <template #header>
          <div class="card-title">权限提示</div>
        </template>
        <ul class="tips">
          <li>管理员：管理用户、站点、报警及导出。</li>
          <li>运维人员：处理报警、维护监测数据。</li>
          <li>访客：只读查看数据与统计。</li>
        </ul>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useAuth } from '../composables/useAuth';

const { user } = useAuth();

const roleLabelMap: Record<string, string> = {
  admin: '管理员',
  operator: '运维人员',
  viewer: '访客',
};

const roleLabel = computed(() => roleLabelMap[user.value?.role ?? 'viewer']);
</script>

<style scoped>
.card-title {
  font-weight: 600;
}

.tips {
  padding-left: 18px;
  line-height: 1.8;
}
</style>
