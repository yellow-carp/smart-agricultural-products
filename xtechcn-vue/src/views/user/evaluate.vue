<script setup lang="ts">
import { reactive, toRefs } from "vue";
import { IObject } from "@/types/interface";
import { ElMessage, ElMessageBox } from "element-plus";
import useView from "@/hooks/useView";
import baseService from "@/service/baseService";

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

// 转为嘉宾
const deleteHandler = (row: IObject) => {
  ElMessageBox.confirm("确定删除?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    baseService.delete("/material/remove/" + row.id, {}).then(() => {
      ElMessage.success("删除成功");
      state.getDataList();
    });
  });
};
</script>

<template>
  <div class="page-wrapper">
    <div class="page-title-wrapper">
      <h1>评价管理</h1>
    </div>
    <el-form class="page-query-wrapper" :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-input v-model="state.dataForm.phone" placeholder="请输入用户ID" @clear="state.getDataList()">
        <template #prefix> 用户ID： </template>
      </el-input>
      <el-input v-model="state.dataForm.name" placeholder="请输入用户电话号码" @clear="state.getDataList()">
        <template #prefix> 用户电话号码： </template>
      </el-input>
      <el-input v-model="state.dataForm.name" placeholder="请输入保洁姓名" @clear="state.getDataList()">
        <template #prefix> 保洁姓名： </template>
      </el-input>
      <el-input v-model="state.dataForm.name" placeholder="请输入保洁电话号码" @clear="state.getDataList()">
        <template #prefix> 保洁电话号码： </template>
      </el-input>
      <div class="form-item-wrapper">
        <span>评分区间</span>
        <el-input-number v-model="state.dataForm.min" :controls="false" :precision="1" placeholder="最小值" />-<el-input-number v-model="state.dataForm.max" :controls="false" :precision="1" placeholder="最大值" />
      </div>
      <div>
        <el-button type="primary" @click="state.getDataList()">搜索</el-button>
        <el-button type="success" @click="resetHandler">重置</el-button>
      </div>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" height="calc(100vh - 280px)">
      <el-table-column prop="userId" label="ID" align="center" />
      <el-table-column prop="name" label="用户信息" align="center" />
      <el-table-column prop="phone" label="服务项目" align="center" />
      <el-table-column prop="phone" label="保洁信息" align="center" />
      <el-table-column prop="phone" label="综合评分" align="center" />
      <el-table-column prop="phone" label="评价时间" align="center" />
      <el-table-column prop="phone" label="评价内容" align="center" />
      <el-table-column prop="releaseDay" label="是否隐藏" align="center">
        <template #default="scope">
          <el-switch v-model="scope.row.releaseDay" />
        </template>
      </el-table-column>
      <el-table-column prop="action" label="操作" align="center" width="220">
        <template #default="scope">
          <el-button type="danger" text @click="deleteHandler(scope.row)">删除</el-button>
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
