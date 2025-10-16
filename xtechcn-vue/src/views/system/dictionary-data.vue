<template>
  <div class="mod-sys__dict">
    <el-button type="primary" style="margin-bottom: 5px" @click="addOrUpdateHandle()">新增</el-button>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="sort" label="排序" sortable="custom" header-align="center" align="center" />
      <el-table-column prop="value" label="字典名称" header-align="center" align="center" />
      <el-table-column prop="code" label="字典值" header-align="center" align="center" />
      <el-table-column prop="name" label="描述" header-align="center" align="center" />
      <el-table-column prop="color" label="颜色" header-align="center" align="center">
        <template #default="scope">
          <el-color-picker :model-value="scope.row.color" />
        </template>
      </el-table-column>
      <el-table-column prop="upsertTime" label="创建时间" sortable="custom" header-align="center" align="center" width="180" />
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button type="primary" link @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button type="danger" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <DictionaryModify v-if="visible" v-model="visible" :payload="payload" @refreshDataList="state.getDataList" />
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import DictionaryModify from "@/views/system/dictionary-item-modify.vue";
import { IObject } from "@/types/interface";

const props = defineProps({
  dictTypeId: {
    type: String,
    required: true
  }
});

const view = reactive({
  getDataListURL: "/dict/item/list/" + props.dictTypeId,
  getDataListIsPage: false,
  activatedIsNeed: false,
  deleteURL: "/dict/item/remove",
  dataList: []
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const visible = ref(false);
const payload = ref<IObject>({});
const addOrUpdateHandle = (value?: IObject) => {
  visible.value = true;
  payload.value = {
    dictId: props.dictTypeId,
    index: state.dataList.length + 1,
    ...value
  };
};
</script>
