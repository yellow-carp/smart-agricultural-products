<script setup lang="ts">
import { computed, reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { encryption } from "@/utils/aes";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

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

const emit = defineEmits(["update:modelValue"]);

const show = computed({
  get() {
    return props.modelValue;
  },
  set(val: boolean) {
    emit("update:modelValue", val);
  }
});

const formData = reactive({
  originalPwd: "",
  password: "",
  confirmPassword: ""
});

const validateConfirmPassword = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (formData.password !== value) {
    return callback(new Error("确认密码与密码输入不一致"));
  }
  callback();
};

const rules = reactive({
  originalPwd: [{ required: true, message: "请输入旧密码", trigger: "blur" }],
  password: [{ required: true, message: "请输入新密码", trigger: "blur" }],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: "blur" }]
});

const formDataRef = ref();
const router = useRouter();
const confirmHandler = async () => {
  await formDataRef.value?.validate();
  const data = {
    originalPwd: encryption(formData.originalPwd),
    password: encryption(formData.password)
  };
  await baseService.put("/user/personal/password", data);
  ElMessage.success("修改成功,请重新登录");
  router.push("/login");
};
</script>

<template>
  <el-dialog v-model="show" width="30%" title="修改密码">
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="旧密码" prop="originalPwd">
        <el-input v-model="formData.originalPwd" type="password" show-password placeholder="旧密码" />
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input v-model="formData.password" type="password" show-password placeholder="新密码" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="formData.confirmPassword" type="password" show-password placeholder="确认密码" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="confirmHandler">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="less"></style>
