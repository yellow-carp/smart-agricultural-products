<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.jobName" style="width: 270px; margin-right: 10px" clearable @clear="state.getDataList()">
          <template #prefix>任务名称:</template>
        </el-input>
        <el-button type="success" @click="state.getDataList()">搜索</el-button>
        <el-button v-if="state.hasPermission('job:task:w')" type="primary" @click="addHandler">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" height="calc(100vh - 250px)" show-overflow-tooltip row-key="id" border>
      <el-table-column prop="jobName" label="任务名称" header-align="center" align="center" min-width="150" />
      <el-table-column prop="jobGroup" label="任务分组" header-align="center" align="center" min-width="150" />
      <el-table-column prop="clazzName" label="任务类名" header-align="center" align="center" min-width="150" />
      <el-table-column prop="methodName" label="方法名" header-align="center" align="center" min-width="150" />
      <el-table-column prop="cronExpression" label="CORN表达式" header-align="center" min-width="150" />
      <el-table-column prop="misfirePolicy" label="错失策略" header-align="center" align="center" min-width="150">
        <template #default="scope">
          <dict-show v-model="scope.row.misfirePolicy" type="MISFIRE_POLICY" />
        </template>
      </el-table-column>
      <el-table-column prop="jobState" label="任务状态" header-align="center" align="center" min-width="150">
        <template #default="scope">
          <dict-show v-model="scope.row.jobState" type="TASK_STATE" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="备注" header-align="center" align="center" min-width="150" />
      <el-table-column prop="firstStart" label="首次执行时间" header-align="center" align="center" min-width="150" />
      <el-table-column prop="lastTime" label="上次执行时间" header-align="center" align="center" min-width="150" />
      <el-table-column prop="nextTime" label="下次执行时间" header-align="center" align="center" min-width="150" />
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="250">
        <template #default="scope">
          <el-button v-if="state.hasPermission('job:task:w')" type="warning" text size="small" @click="updateHandler(scope.row)">修改</el-button>
          <el-button v-if="state.hasPermission('job:task:w') && scope.row.jobState !== 3" type="primary" text size="small" @click="runHandler(scope.row.jobId)">运行</el-button>
          <el-button v-if="state.hasPermission('job:task:w')" type="primary" text size="small" @click="nowRunHandler(scope.row.jobId)">执行</el-button>
          <el-button v-if="state.hasPermission('job:task:w') && scope.row.jobState !== 4" type="danger" text size="small" @click="state.deleteHandle(scope.row.jobId)">移除</el-button>
          <el-button v-if="state.hasPermission('job:task:w') && scope.row.jobState === 3" type="danger" text size="small" @click="stopHandler(scope.row.jobId)">暂停</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle" />

    <!-- 新增/修改 -->
    <TaskModify v-if="visible" v-model="visible" :payload="payload" @refreshData="state.getDataList" />
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, toRefs, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";
import TaskModify from "@/views/system/task-modify.vue";
import DictShow from "@/components/dict-show/src/dict-show.vue";
import { IObject } from "@/types/interface";

const view = reactive({
  getDataListURL: "/task/page",
  deleteURL: "/task/remove",
  getDataListIsPage: true,
  dataList: [],
  dataForm: {
    jobName: "",
    traceId: "",
    level: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const payload = ref({});
const visible = ref(false);
const addHandler = () => {
  payload.value = {};
  visible.value = true;
};

const updateHandler = (row: IObject) => {
  payload.value = row;
  visible.value = true;
};

const runHandler = async (jobId: number) => {
  try {
    await baseService.put("/task/resume/" + jobId);
    ElMessage.success("运行成功");
    state.getDataList();
  } catch (error) {
    console.log(error);
  }
};

const stopHandler = async (jobId: number) => {
  try {
    await baseService.put("/task/pause/" + jobId);
    ElMessage.success("暂停成功");
    state.getDataList();
  } catch (error) {
    console.log(error);
  }
};

const nowRunHandler = async (jobId: number) => {
  ElMessageBox.confirm("确定进行[执行]操作?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    try {
      await baseService.post("/task/run/" + jobId);
      ElMessage.success("执行成功");
      state.getDataList();
    } catch (error) {
      console.log(error);
    }
  });
};
</script>
