<script setup lang="ts">
import SvgIcon from "@/components/base/svg-icon/index";
</script>

<template>
  <div class="page-wrapper">
    <div class="page-title-wrapper">用户详情</div>
    <div class="details-wrapper">
      <div class="base-wrapper">
        <p class="title"><svg-icon name="four-pointed-star" style="color: #181818" />基础信息</p>
        <div class="info-wrapper">
          <div class="avatar-wrapper">
            <!--            <el-image v-if="details.avatar" style="width: 100px; height: 100px" :src="details.avatar" fit="cover" />-->
            <el-image style="width: 100px; height: 100px" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" fit="cover" />
          </div>
          <div class="desc-wrapper">
            <p class="info-item"><span>ID: </span>476238492734</p>
            <p class="info-item"><span>昵称: </span>张AA</p>
            <p class="info-item"><span>电话: </span>13800138000</p>
            <p class="info-item"><span>注册时间: </span>2025-08-26 15:59:10</p>
          </div>
        </div>
        <p class="guest-wrapper">新客户（已完成订单数0-5）</p>
        <p class="evaluate-text">综合评分：<span>98</span></p>
      </div>
      <div class="evaluate-wrapper">
        <p class="title"><svg-icon name="four-pointed-star" style="color: #181818" />相关评价(保洁评)</p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.details-wrapper {
  display: flex;
  gap: 50px;
  margin-top: 10px;

  .title {
    color: #181818;
    font-size: 18px;
    font-weight: 600;
    display: flex;
    gap: 3px;
    align-items: center;
  }
}

.base-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 0 20px;

  .info-wrapper {
    margin-top: 10px;
    display: flex;
    gap: 20px;
  }

  .avatar-wrapper {
    height: 100px;
    width: 100px;
    border-radius: 50%;
    padding: 5px;
  }

  .desc-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 10px;

    .info-item {
      display: flex;
      align-items: center;
      gap: 5px;
      font-size: 12px;
      font-weight: 500;
      color: #000;

      span {
        color: #919191;
      }
    }
  }

  .guest-wrapper {
    width: 100%;
    border: 1px solid #ccc;
    border-radius: 5px;
    text-align: center;
    padding: 5px;
    font-size: 14px;
  }

  .evaluate-text {
    font-size: 13px;
    text-align: center;

    > span {
      font-size: 25px;
      font-weight: 600;
      color: #ff2600;
    }
  }
}
.evaluate-wrapper {
  flex: 2;
}
</style>
