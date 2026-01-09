<template>
  <div class="login-page">
    <el-card class="login-card">
      <template #header>
        <div class="login-title">登录水文监测平台</div>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width: 100%" @click="handleSubmit">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <el-alert
        v-if="errorMessage"
        type="error"
        :closable="false"
        show-icon
        :title="errorMessage"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import type { FormInstance, FormRules } from 'element-plus';
import { useAuth } from '../composables/useAuth';

const router = useRouter();
const { login } = useAuth();

const formRef = ref<FormInstance>();
const loading = ref(false);
const errorMessage = ref('');

const form = ref({
  username: '',
  password: '',
});

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const handleSubmit = async () => {
  errorMessage.value = '';
  await formRef.value?.validate(async (valid) => {
    if (!valid) {
      return;
    }
    loading.value = true;
    try {
      await login(form.value.username, form.value.password);
      router.push({ name: 'dashboard' });
    } catch (error: any) {
      errorMessage.value = error?.message || '登录失败';
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #4b6cb7, #182848);
}

.login-card {
  width: 360px;
}

.login-title {
  font-size: 18px;
  font-weight: 600;
  text-align: center;
}
</style>
