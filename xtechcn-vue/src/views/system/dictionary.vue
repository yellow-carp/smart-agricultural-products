<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.name" style="width: 180px" placeholder="字典名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="state.dataForm.code" style="width: 180px" placeholder="字典类型" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="state.getDataList()">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:dict:w')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" height="calc(100vh - 250px)" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="name" label="字典名称" header-align="center" align="center" />
      <el-table-column prop="code" label="字典类型" header-align="center" align="center">
        <template v-slot="scope">
          <el-button type="primary" link @click="showTypeList(scope.row)">{{ scope.row.code }}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" header-align="center" align="center"></el-table-column>
      <el-table-column prop="upsertTime" label="创建时间" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="180">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:dict:w')" type="primary" link @click="showTypeList(scope.row)">字典配置</el-button>
          <el-button v-if="state.hasPermission('sys:dict:w')" type="primary" link @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button v-if="state.hasPermission('sys:dict:d')" type="danger" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <DictionaryModify ref="addOrUpdateRef" @refreshDataList="state.getDataList" />
    <!-- 字典类型数据 -->
    <el-drawer v-if="dictDataVisible" v-model="dictDataVisible" :title="state.focusDictTypeTitle" :size="800" :close-on-press-escape="false" class="rr-drawer"> <DictTypeList :dictTypeId="state.focusDictTypeId" /></el-drawer>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import DictTypeList from "./dictionary-data.vue";
import { IObject } from "@/types/interface";
import { useRoute } from "vue-router";
import DictionaryModify from "./dictionary-modify.vue";
const route = useRoute();

const dictDataVisible = ref(false);

const view = reactive({
  getDataListURL: "/dict/page",
  getDataListIsPage: true,
  deleteURL: "/dict/remove",
  dataList: [],
  dataForm: {
    name: "",
    code: ""
  },
  focusDictTypeId: "",
  focusDictTypeTitle: ""
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const showTypeList = (row: IObject) => {
  dictDataVisible.value = true;
  state.focusDictTypeId = row.id;
  state.focusDictTypeTitle = `${route.meta.title} - ${row.code}`;
};

const addOrUpdateRef = ref();
const addOrUpdateHandle = (value?: IObject) => {
  addOrUpdateRef.value.init(value);
};
</script>
