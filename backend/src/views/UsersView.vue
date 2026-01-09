<template>
  <el-card>
    <template #header>
      <div class="header">
        <span class="title">权限管理</span>
        <el-button type="primary" @click="openCreateDialog">新增用户</el-button>
      </div>
    </template>
    <el-table :data="users" stripe>
      <el-table-column prop="username" label="用户名" width="140" />
      <el-table-column prop="realName" label="姓名" width="140" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="scope">
          <el-tag>{{ roleLabelMap[scope.row.role] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140">
        <template #default="scope">
          <el-button type="primary" text @click="openEditDialog(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px">
    <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
      <el-form-item label="用户名" prop="username" v-if="isCreate">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码" prop="password" v-if="isCreate">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>
      <el-form-item label="姓名" prop="realName">
        <el-input v-model="form.realName" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="form.role" placeholder="请选择角色">
          <el-option label="管理员" value="admin" />
          <el-option label="运维人员" value="operator" />
          <el-option label="访客" value="viewer" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { request } from '../api/client';
import type { AuthUser, PageResponse, UserRole } from '../types/auth';

interface UserForm {
  id?: number;
  username?: string;
  password?: string;
  realName: string;
  email?: string | null;
  phone?: string | null;
  role: UserRole;
  status: number;
}

const users = ref<AuthUser[]>([]);
const dialogVisible = ref(false);
const saving = ref(false);
const isCreate = ref(true);
const formRef = ref<FormInstance>();

const roleLabelMap: Record<UserRole, string> = {
  admin: '管理员',
  operator: '运维人员',
  viewer: '访客',
};

const form = reactive<UserForm>({
  username: '',
  password: '',
  realName: '',
  email: '',
  phone: '',
  role: 'viewer',
  status: 1,
});

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
};

const dialogTitle = ref('新增用户');

const fetchUsers = async () => {
  const data = await request<PageResponse<AuthUser>>('/users?page=1&size=20');
  users.value = data.content;
};

const resetForm = () => {
  form.id = undefined;
  form.username = '';
  form.password = '';
  form.realName = '';
  form.email = '';
  form.phone = '';
  form.role = 'viewer';
  form.status = 1;
};

const openCreateDialog = () => {
  resetForm();
  isCreate.value = true;
  dialogTitle.value = '新增用户';
  dialogVisible.value = true;
};

const openEditDialog = (user: AuthUser) => {
  resetForm();
  isCreate.value = false;
  dialogTitle.value = '编辑用户';
  form.id = user.id;
  form.realName = user.realName;
  form.email = user.email ?? '';
  form.phone = user.phone ?? '';
  form.role = user.role;
  form.status = user.status;
  dialogVisible.value = true;
};

const handleSave = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) {
      return;
    }
    saving.value = true;
    try {
      if (isCreate.value) {
        await request<AuthUser>('/users', {
          method: 'POST',
          body: JSON.stringify({
            username: form.username,
            password: form.password,
            realName: form.realName,
            email: form.email,
            phone: form.phone,
            role: form.role,
          }),
        });
      } else if (form.id) {
        await request<void>(`/users/${form.id}`, {
          method: 'PUT',
          body: JSON.stringify({
            realName: form.realName,
            email: form.email,
            phone: form.phone,
            role: form.role,
            status: form.status,
          }),
        });
      }
      dialogVisible.value = false;
      await fetchUsers();
    } finally {
      saving.value = false;
    }
  });
};

onMounted(fetchUsers);
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-weight: 600;
}
</style>
