<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.reqUri" style="width: 270px; margin-right: 10px" clearable @clear="state.getDataList()">
          <template #prefix>接口地址:</template>
        </el-input>
        <el-input v-model="state.dataForm.traceId" style="width: 270px; margin-right: 10px" clearable @clear="() => state.getDataList()">
          <template #prefix>链路ID:</template>
        </el-input>
        <el-select v-model="state.dataForm.level" style="width: 270px; margin-right: 10px" clearable @clear="state.getDataList()" placeholder="日志级别">
          <el-option v-for="item in levelList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-button type="success" @click="state.getDataList()">搜索</el-button>
        <el-button v-if="state.hasPermission('sys:log:cl')" type="info" @click="clearHandler">清理</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" height="calc(100vh - 250px)" :data="state.dataList" show-overflow-tooltip row-key="id" border>
      <el-table-column prop="reqUri" label="接口地址" header-align="center" min-width="150" />
      <el-table-column prop="notes" label="描述" header-align="center" min-width="150" />
      <el-table-column prop="remoteAddr" label="客户端IP" header-align="center" align="center" min-width="150" />
      <el-table-column prop="traceId" label="链路ID" header-align="center" align="center" min-width="150" />
      <el-table-column prop="createBy" label="操作人员" header-align="center" align="center" min-width="150" />
      <el-table-column prop="params" label="接口参数" header-align="center" min-width="150" />
      <el-table-column prop="exceptionClazz" label="异常类" header-align="center" min-width="150" />
      <el-table-column prop="message" label="错误信息" header-align="center" min-width="150" />
      <el-table-column prop="level" label="日志级别" header-align="center" align="center" min-width="150" />
      <el-table-column prop="userAgent" label="设备信息" header-align="center" align="center" min-width="150" />
      <el-table-column prop="createTime" label="执行时间" header-align="center" align="center" min-width="150" />
    </el-table>

    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle" />
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, toRefs } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

const view = reactive({
  getDataListURL: "/logs/page",
  getDataListIsPage: true,
  dataList: [],
  dataForm: {
    reqUri: "",
    traceId: "",
    level: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const levelList = reactive([
  {
    value: "ERROR",
    label: "ERROR"
  },
  {
    value: "INFO",
    label: "INFO"
  },
  {
    value: "WARN",
    label: "WARN"
  }
]);

const clearHandler = async () => {
  await baseService.delete("/logs/clear/level", { level: state.dataForm.level });
  ElMessage.success("清理成功");
  state.getDataList();
};
</script>
