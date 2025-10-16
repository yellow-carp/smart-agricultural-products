<script lang="ts" setup>
import SvgIcon from "@/components/base/svg-icon";
import { useFullscreen } from "@vueuse/core";
import { useRouter } from "vue-router";
import userLogo from "@/assets/images/user.png";
import "@/assets/css/header.less";
import { ElMessageBox } from "element-plus";
import { useAppStore } from "@/store";
import PersonalPassword from "@/layout/header/personal-password.vue";
import { ref } from "vue";
import { getCache } from "@/utils/cache";
import { CacheUserInfo } from "@/constants/cacheKey";

interface IUserInfo {
  avatar: string;
  nickname: string;
}

interface IExpand {
  userName?: string;
}

const props = defineProps<IExpand>();

/**
 * 顶部右侧扩展区域
 */
const router = useRouter();
const { isFullscreen, toggle } = useFullscreen();

const store = useAppStore();
const user: IUserInfo = getCache(CacheUserInfo, { isSessionStorage: true }) || {};
const visible = ref(false);

const onClickUserMenus = (path: string) => {
  if (path === "/login") {
    ElMessageBox.confirm("确定进行[退出]操作", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
      .then(() => {
        store.logout();
        router.push("/login");
      })
      .catch(() => {
        //
      });
  } else {
    visible.value = true;
  }
};
</script>
<template>
  <div class="rr-header-right-items">
    <div>
      <!--      <el-tooltip content="待办" placement="top" effect="light">-->
      <!--        <svg-icon name="bell" />-->
      <!--      </el-tooltip>-->
    </div>
    <div @click="toggle" class="hidden-xs-only">
      <span>
        <svg-icon :name="isFullscreen ? 'tuichuquanping' : 'fullscreen2'"></svg-icon>
      </span>
    </div>
    <div style="display: flex; justify-content: center; align-items: center">
      <img v-if="user.avatar" :src="user.avatar" :alt="props.userName" style="width: 30px; height: 30px; border-radius: 50%; margin-top: 3px; margin-right: 5px" />
      <img v-else :src="userLogo" :alt="props.userName" style="width: 30px; height: 30px; border-radius: 50%; margin-top: 3px; margin-right: 5px" />
      <el-dropdown @command="onClickUserMenus" trigger="click">
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item icon="lock" command="/user/password"> 修改密码 </el-dropdown-item>
            <el-dropdown-item icon="switch-button" divided command="/login"> 退出登录 </el-dropdown-item>
          </el-dropdown-menu>
        </template>
        <span class="el-dropdown-link" style="display: flex">
          {{ props.userName }}
          <el-icon class="el-icon--right" style="font-size: 14px"><arrow-down /></el-icon>
        </span>
      </el-dropdown>
    </div>
  </div>

  <!-- 修改密码 -->
  <PersonalPassword v-if="visible" v-model="visible" />
</template>
