<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <dict-select v-model="state.dataForm.fileType" dictType="FILE_TYPE" style="width: 270px; margin-right: 10px" clearable @clear="state.getDataList()" placeholder="文件类型" />
        <el-button type="success" @click="state.getDataList()">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table class="mark-table-wrapper" v-loading="state.dataListLoading" :data="state.dataList" height="calc(100vh - 250px)" show-overflow-tooltip row-key="id" border>
      <el-table-column prop="name" label="名称" header-align="center" />
      <el-table-column prop="name" label="类型" header-align="center" align="center">
        <template v-slot="scope"> <DictShow v-model="scope.row.fileType" type="FILE_TYPE" /></template>
      </el-table-column>
      <el-table-column prop="upsertTime" label="上传时间" header-align="center" align="center" />
      <el-table-column prop="action" label="操作" header-align="center" align="center" width="80">
        <template #default="scope">
          <el-button v-if="state.hasPermission('base:file:d')" text type="danger" size="small" @click="state.deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle" />
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, toRefs } from "vue";
import DictSelect from "@/components/dict-select/src/dict-select.vue";
import DictShow from "@/components/dict-show/src/dict-show.vue";

const view = reactive({
  getDataListURL: "/file/page",
  getDataListIsPage: true,
  deleteURL: "/file/remove",
  dataList: [],
  dataForm: {
    fileType: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });
</script>
