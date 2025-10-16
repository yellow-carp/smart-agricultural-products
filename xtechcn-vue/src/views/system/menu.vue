<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:menu:w')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" show-overflow-tooltip row-key="id" height="calc(100vh - 200px)" border>
      <el-table-column prop="name" label="名称" header-align="center" min-width="150" />
      <el-table-column prop="icon" label="图标" header-align="center" align="center">
        <template v-slot="scope">
          <svg class="iconfont">
            <use :xlink:href="`#${scope.row.icon}`"></use>
          </svg>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.type === 1">菜单</el-tag>
          <el-tag v-else type="info">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="openStyle" label="是否隐藏" header-align="center" align="center">
        <template v-slot="scope">
          <span v-if="scope.row.type === 0"></span>
          <el-tag v-else-if="scope.row.isHidden">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" header-align="center" align="center" />
      <el-table-column prop="path" label="路由" header-align="center" align="center" width="150" />
      <el-table-column prop="permission" label="授权标识" header-align="center" align="center" width="150" />
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="170">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:menu:w')" type="primary" link @click="addHandle(scope.row)">新增</el-button>
          <el-button v-if="state.hasPermission('sys:menu:w')" type="primary" link @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button v-if="state.hasPermission('sys:menu:d')" type="danger" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <MenuModify ref="addOrUpdateRef" @refreshDataList="state.getDataList" />
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import MenuModify from "@/views/system/menu-modify.vue";

const view = reactive({
  getDataListURL: "/menu/tree",
  deleteURL: "/menu/remove",
  dataList: []
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const addHandle = (row: any) => {
  addOrUpdateRef.value.init2(row);
};
</script>
