<template>
  <div class="page-wrapper">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.nickname" style="width: 220px; margin-right: 10px" clearable>
          <template #prefix>用户昵称:</template>
        </el-input>
        <el-input v-model="state.dataForm.phone" style="width: 220px; margin-right: 10px" clearable>
          <template #prefix>联系电话:</template>
        </el-input>
        <el-button v-if="state.hasPermission('sys:user:w')" type="primary" @click="state.getDataList()">搜索</el-button>
        <el-button v-if="state.hasPermission('sys:user:w')" type="success" @click="resetHandler()">重置</el-button>
        <el-button v-if="state.hasPermission('sys:user:w')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table class="mark-table-wrapper" v-loading="state.dataListLoading" :data="state.dataList" height="calc(100vh - 250px)" show-overflow-tooltip row-key="id" border>
      <el-table-column prop="avatar" label="用户头像" header-align="center" align="center" min-width="150">
        <template #default="scope">
          <el-image :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" :z-index="99999" style="height: 50px; width: 50px" />
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="用户昵称" header-align="center" align="center" min-width="150" />
      <el-table-column prop="phone" label="联系电话" header-align="center" align="center" min-width="150" />
      <el-table-column prop="dept" label="部门" header-align="center" align="center" min-width="150" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.deptInfos.map((item: any) => item.name).join(", ") }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="角色权限" header-align="center" align="center" min-width="150" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.roleInfos.map((item: any) => item.name).join(", ") }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="enabled" label="状态" header-align="center" align="center" min-width="150">
        <template #default="scope">
          <el-tag v-if="scope.row.enabled">启用</el-tag>
          <el-tag v-else type="info">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:user:w')" type="primary" :disabled="scope.row.userId === '1'" link @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button v-if="state.hasPermission('sys:user:w') && scope.row.enabled" :disabled="scope.row.userId === '1'" type="danger" link @click="enabeldHandle(scope.row)">禁用</el-button>
          <el-button v-if="state.hasPermission('sys:user:w') && !scope.row.enabled" type="success" :disabled="scope.row.userId === '1'" link @click="enabeldHandle(scope.row)">启用</el-button>
          <el-button v-if="state.hasPermission('sys:user:w')" type="success" :disabled="scope.row.userId === '1'" link @click="resetUserHandler(scope.row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle" />
    <!-- 弹窗, 新增 / 修改 -->
    <UserModify v-if="visible" v-model="visible" :payload="payload" @refreshDataList="state.getDataList" />
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import UserModify from "@/views/system/user-modify.vue";
import { IObject } from "@/types/interface";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";
import { encryption } from "@/utils/aes";

const view = reactive({
  getDataListURL: "/user/page",
  getDataListIsPage: true,
  dataList: [],
  dataForm: {
    nickname: "",
    phone: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const visible = ref(false);
const payload: IObject = ref({});
const addOrUpdateHandle = (value: IObject = {}) => {
  visible.value = true;
  payload.value = value;
};

const enabeldHandle = (value: IObject) => {
  baseService
    .put("/user/disenable", {
      ofId: value.userId,
      enabled: !value.enabled
    })
    .then(() => {
      state.getDataList();
    });
};

const resetHandler = () => {
  state.dataForm = {
    nickname: "",
    phone: ""
  };
  state.getDataList();
};

const resetUserHandler = (value: IObject) => {
  ElMessageBox.prompt("是否进行[重置密码]操作?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPlaceholder: "请输入新密码",
    inputPattern: /^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z][a-zA-Z0-9_@.-]{6,30}$/,
    inputErrorMessage: "密码格式不正确",
    type: "warning"
  }).then(async (val: any) => {
    await baseService.put("/user/reset/password", {
      userId: value.userId,
      password: encryption(val.value)
    });
    ElMessage.success("重置成功");
  });
};
</script>
