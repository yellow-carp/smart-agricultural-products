<template>
  <el-dialog v-model="visible" :title="!dataForm.userId ? '新增用户' : '修改用户'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="avatar" label="用户头像">
        <el-upload class="avatar-uploader" :http-request="uploadAvatar" :show-file-list="false" accept="image/*">
          <el-image v-if="dataForm.avatar" :src="dataForm.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="nickname" label="用户昵称">
        <el-input v-model="dataForm.nickname" placeholder="请输入用户昵称" />
      </el-form-item>
      <el-form-item v-if="!dataForm.userId" prop="username" label="用户名">
        <el-input v-model="dataForm.username" placeholder="长度必须为6到20位,只能包含字母数字和下划线，字母不区分大小写，只能字母开头" />
      </el-form-item>
      <el-form-item v-if="!dataForm.userId" prop="password" label="密码" :class="{ 'is-required': !dataForm.userId }">
        <el-input v-model="dataForm.password" :type="inputType" :show-password="inputType !== 'text'" placeholder="字母打头,8-32位长度,字母要求同时含大写小写，可选数字( _@. )" @click="inputType = 'password'" />
      </el-form-item>
      <el-form-item v-if="!dataForm.userId" prop="confirmPassword" label="确认密码" :class="{ 'is-required': !dataForm.userId }">
        <el-input v-model="dataForm.confirmPassword" :type="inputType" :show-password="inputType !== 'text'" placeholder="请确认密码" />
      </el-form-item>
      <el-form-item prop="phone" label="手机号">
        <el-input v-model="dataForm.phone" placeholder="手机号" show-word-limit maxlength="11" />
      </el-form-item>
      <el-form-item prop="deptName" label="所属部门">
        <el-tree-select v-model="dataForm.deptName" :data="deptList" multiple check-strictly :props="{ label: 'name', value: 'id' }" placeholder="所属部门" />
      </el-form-item>
      <el-form-item prop="roleName" label="角色配置" class="role-list">
        <el-select v-model="dataForm.roleName" multiple placeholder="角色配置">
          <el-option v-for="role in roleList" :key="role.id" :label="role.name" :value="role.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { isMobile } from "@/utils/utils";
import { IObject } from "@/types/interface";
import { ElMessage } from "element-plus";
import { encryption } from "@/utils/aes";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  payload: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(["refreshDataList", "update:modelValue"]);

const visible = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit("update:modelValue", val);
  }
});
const roleList = ref<any[]>([]);
const deptList = ref<any[]>([]);
const dataFormRef = ref();

const inputType = ref("text");

const getDeptList = async () => {
  const { data } = await baseService.get("/department/allTree");
  deptList.value = data;
};

const dataForm = reactive({
  userId: "",
  username: "",
  avatar: "",
  nickname: "",
  deptName: [],
  password: "",
  confirmPassword: "",
  phone: "",
  roleName: [] as IObject[]
});

const validatePassword = (rule: any, value: any, callback: any) => {
  if (value === "") return callback(new Error("必填项不能为空"));

  const reg = /^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z][a-zA-Z0-9_@.-]{6,30}$/;

  if (!reg.test(value)) return callback(new Error("密码格式错误"));

  callback();
};

const validateConfirmPassword = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (!dataForm.userId && !/\S/.test(value)) {
    return callback(new Error("必填项不能为空"));
  }
  if (dataForm.password !== value) {
    return callback(new Error("确认密码与密码输入不一致"));
  }
  callback();
};

const validateConfirmUsername = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (value === "") return callback();

  const reg = /^[a-zA-Z][a-zA-Z0-9]{5,19}$/;

  if (!reg.test(value)) return callback(new Error("用户名格式错误"));

  callback();
};

const validateMobile = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (value && !isMobile(value)) {
    return callback(new Error("手机格式错误"));
  }
  callback();
};

const rules = ref({
  username: [{ required: false, validator: validateConfirmUsername, trigger: "blur" }],
  nickname: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  password: [{ required: true, validator: validatePassword, trigger: "blur" }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: "blur" }],
  phone: [{ required: true, validator: validateMobile, trigger: "blur" }]
});

// 获取角色列表
const getRoleList = async () => {
  const { data } = await baseService.get("/role/list/all");
  roleList.value = data.filter((item: any) => item.id !== 1);
};
const loopDept = (data: any, list: any = [], result: any[] = []) => {
  list.forEach((item: any) => {
    if (data.includes(item.id)) {
      result.push({
        id: item.id,
        name: item.name
      });
    }
    if (item.children && item.children.length) {
      return loopDept(data, item.children, result);
    }
  });
  return result;
};
// 表单提交
const dataFormSubmitHandle = async () => {
  await dataFormRef.value.validate();
  try {
    const data: any = {
      ...dataForm,
      roleInfos: roleList.value
        .filter((item: any) => dataForm.roleName.includes(item.id))
        .map((item: any) => ({
          id: item.id,
          name: item.name
        })),
      deptInfos: loopDept(dataForm.deptName, deptList.value),
      password: encryption(dataForm.password)
    };

    delete data.confirmPassword;

    await (!dataForm.userId ? baseService.post : baseService.put)(!dataForm.userId ? "/user/create" : "/user/update", data);
    ElMessage.success({
      message: "成功",
      duration: 500,
      onClose: () => {
        visible.value = false;
        emit("refreshDataList");
      }
    });
  } catch (error) {
    console.log(error);
  }
};

// 上传头像
const uploadAvatar = async ({ file }: { file: any }) => {
  const formData = new FormData();
  formData.append("file", file);
  const { data } = await baseService.post("/file/upload", formData, { "Content-Type": "multipart/form-data" });
  dataForm.avatar = data.url;
  ElMessage.success("上传成功");
};

onMounted(async () => {
  await getDeptList();
  await getRoleList();

  if (props.payload.userId) {
    Object.assign(dataForm, props.payload);
    dataForm.roleName = props.payload.roleInfos.map((item: any) => item.id);
    dataForm.deptName = props.payload.deptInfos.map((item: any) => item.id);
  }
});
</script>

<style lang="less" scoped>
.mod-sys__user {
  .role-list {
    .el-select {
      width: 100%;
    }
  }
}
</style>

<style scoped>
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
}
</style>
