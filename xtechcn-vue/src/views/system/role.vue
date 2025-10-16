<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:role:w')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" height="calc(100vh - 200px)" show-overflow-tooltip row-key="id" border>
      <el-table-column prop="name" label="角色名称" header-align="center" align="center" min-width="150" />
      <el-table-column prop="description" label="描述" header-align="center" align="center" min-width="150" />
      <el-table-column prop="type" label="角色类型" header-align="center" align="center" min-width="150">
        <template #default="scope">
          <dict-show v-model="scope.row.type" type="ROLE_TYPE" />
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="170">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:role:gr')" :disabled="scope.row.type === 1" type="success" link @click="authorizeHandle(scope.row)">授权</el-button>
          <el-button v-if="state.hasPermission('sys:role:w')" :disabled="scope.row.type === 1 || scope.row.type === 2" type="primary" link @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button v-if="state.hasPermission('sys:role:d')" :disabled="scope.row.type === 1 || scope.row.type === 2" type="danger" link @click="state.deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗, 新增 / 修改 -->
    <RoleModify v-if="visible" v-model="visible" :payload="payload" @refreshDataList="state.getDataList" />
    <!-- 授权 -->
    <n-config-provider :theme-overrides="themeOverrides">
      <RoleAuthorize v-if="authorizeVisible" v-model="authorizeVisible" :payload="payload" @refreshDataList="state.getDataList" />
    </n-config-provider>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import DictShow from "@/components/dict-show/src/dict-show.vue";
import RoleModify from "@/views/system/role-modify.vue";
import { IObject } from "@/types/interface";
import RoleAuthorize from "@/views/system/role-authorize.vue";
import { NConfigProvider } from "naive-ui";
import { themeSetting } from "@/constants/config";

const view = reactive({
  getDataListURL: "/role/list/all",
  deleteURL: "/role/remove",
  dataList: []
});

const themeOverrides = {
  common: {
    primaryColor: themeSetting.themeColor
  }
};

const state = reactive({ ...useView(view), ...toRefs(view) });

const visible = ref(false);
const authorizeVisible = ref(false);
const payload = ref<IObject>({});
const addOrUpdateHandle = (value: IObject = {}) => {
  visible.value = true;
  payload.value = value;
};
const authorizeHandle = (value: IObject = {}) => {
  authorizeVisible.value = true;
  payload.value = value;
};
</script>
