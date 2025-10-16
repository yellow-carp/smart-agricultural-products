<template>
  <div class="login-wrapper">
    <div class="login-content-wrapper">
      <h2>蔬菜批发</h2>
      <h1>后台管理系统</h1>
      <el-form ref="formRef" label-width="80px" :status-icon="true" :model="login" :rules="rules" @keyup.enter="onLogin">
        <el-form-item label-width="0" prop="username">
          <el-input v-model="login.username" placeholder="用户名或手机号码" prefix-icon="user" autocomplete="off" />
        </el-form-item>
        <el-form-item label-width="0" prop="password">
          <el-input placeholder="密码" v-model="login.password" prefix-icon="lock" autocomplete="off" show-password />
        </el-form-item>
        <el-form-item label-width="0">
          <el-button type="primary" style="width: 100%; height: 32px; border-radius: 10px" :disabled="state.loading" @click="onLogin">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { CacheUserInfo } from "@/constants/cacheKey";
import baseService from "@/service/baseService";
import { setCache } from "@/utils/cache";
import { ElMessage } from "element-plus";
import { useAppStore } from "@/store";
import { useRouter } from "vue-router";
import { encryption } from "@/utils/aes";
import { usePoliciesStore } from "@/store/policeies";

const store = useAppStore();
const policiesStore = usePoliciesStore();

const router = useRouter();

const state = reactive({
  loading: false,
  year: new Date().getFullYear()
});

const login = reactive({ username: "admin", password: "Asia8888" });

onMounted(() => {
  //清理数据
  store.logout();
  policiesStore.delPoliciesInfo();
});
const formRef = ref();

const validatePassword = (rule: any, value: any, callback: any) => {
  if (value === "") return callback(new Error("请输入密码"));

  const reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;

  if (!reg.test(value)) return callback(new Error("密码格式错误"));

  callback();
};

const rules = ref({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, validator: validatePassword, trigger: "blur" }]
});

/**
 * 登录
 */
const onLogin = async () => {
  try {
    await formRef.value.validate();
    state.loading = true;
    const formData = new FormData();
    formData.append("username", login.username);
    formData.append("password", encryption(login.password));
    formData.append("grant_type", "password");
    const res: any = await baseService.post("/auth/login", formData, { "Content-Type": "multipart/form-data" });
    state.loading = false;
    await setCache(CacheUserInfo, res.data, true);
    // 图片上传
    // await policiesStore.syncGetPolicyInfo();
    ElMessage.success("登录成功");
    await router.push("/");
  } catch (e) {
    state.loading = false;
  }
};
</script>

<style lang="less" scoped>
@import url("@/assets/theme/base.less");
.login-wrapper {
  width: 100%;
  height: 100vh;
  background: url("@/assets/images/login_bg.png") center center / 101% 101% no-repeat;
  position: relative;

  .login-content-wrapper {
    position: absolute;
    width: 400px;
    background: #ffffff;
    top: 30%;
    right: 20%;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 10px 1px 10px 1px rgba(0, 0, 0, 0.1);

    h2 {
      font-size: 22px;
      margin-bottom: 5px;
    }

    h1 {
      font-size: 30px;
      font-weight: 600;
      margin-bottom: 20px;
    }
  }
}
</style>
