<script setup lang="ts">
import { getCache } from "@/utils/cache";
import { CacheUserInfo } from "@/constants/cacheKey";

interface IUserInfo {
  avatar: string;
  nickname: string;
}

const user: IUserInfo = getCache(CacheUserInfo, { isSessionStorage: true }) || {};
</script>

<template>
  <div class="home-wrapper">
    <h1 class="title-wrapper">蔬菜批发·后台管理系统</h1>

    <div class="info-wrapper">
      <div class="avatar-wrapper">
        <el-image v-if="user.avatar" :src="user.avatar" />
      </div>
      <p>{{ user.nickname }}</p>
    </div>
  </div>
</template>

<style scoped>
.home-wrapper {
  position: relative;
}

.title-wrapper {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 10px;
}

.info-wrapper {
  width: 200px;
  padding: 20px;
  border-radius: 10px;
  background: #f3e8e6;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  justify-content: center;

  p {
    font-size: 18px;
  }
}

.avatar-wrapper {
  height: 100px;
  width: 100px;
  padding: 10px;
  background: #fff;
  border-radius: 50%;

  .el-image {
    border-radius: 50%;
    height: 100%;
    width: 100%;
  }
}
</style>
