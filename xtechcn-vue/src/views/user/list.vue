<script setup lang="ts">
import { reactive, toRefs, ref } from "vue";
import { IObject } from "@/types/interface";
import useView from "@/hooks/useView";
import { useRouter } from "vue-router";

const view = reactive({
  getDataListURL: "/guest/pc/page",
  getDataListIsPage: true,
  dataList: [],
  dataForm: {
    identity: 1
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

// 重置
const resetHandler = () => {
  state.dataForm = {
    identity: 1
  };
  state.getDataList();
};

const router = useRouter();

// 跳转详情
const toDetailsHandler = (row: IObject) => {
  router.push({
    path: "/user/details",
    query: {
      id: row.id
    }
  });
};
</script>

<template>
  <div class="page-wrapper">
    <div class="page-title-wrapper">
      <h1>用户列表</h1>
    </div>
    <el-form class="page-query-wrapper" :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-input v-model="state.dataForm.phone" placeholder="请输入用户ID" @clear="state.getDataList()">
        <template #prefix> 用户编码： </template>
      </el-input>
      <el-input v-model="state.dataForm.name" placeholder="请输入用户电话号码" @clear="state.getDataList()">
        <template #prefix> 用户电话号码： </template>
      </el-input>
      <div>
        <el-button type="primary" @click="state.getDataList()">搜索</el-button>
        <el-button type="success" @click="resetHandler">重置</el-button>
      </div>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="[{}]" height="calc(100vh - 280px)">
      <el-table-column prop="userId" label="用户ID" align="center" />
      <el-table-column prop="name" label="头像" align="center" />
      <el-table-column prop="phone" label="昵称" align="center" />
      <el-table-column prop="phone" label="电话号码" align="center" />
      <el-table-column prop="phone" label="注册时间" align="center" />
      <el-table-column prop="phone" label="已完成订单数" align="center" />
      <el-table-column prop="action" label="操作" align="center" width="220">
        <template #default="scope">
          <el-button type="primary" text @click="toDetailsHandler(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle" />
  </div>
</template>

<style scoped lang="less">
.page-query-wrapper {
  margin-top: 10px;
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;

  > div {
    width: calc((100% - 50px) / 6);
  }
}
.form-item-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  white-space: nowrap;

  > span {
    font-size: 14px;
  }
}
</style>
