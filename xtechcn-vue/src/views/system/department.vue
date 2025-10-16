<script setup lang="ts">
import { onMounted, ref, h, computed } from "vue";
import baseService from "@/service/baseService";
import DepartmentModify from "@/views/system/department-modify.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { IObject } from "@/types/interface";
import DepartmentSelectUser from "@/views/system/department-select-user.vue";
import { Edit } from "@element-plus/icons-vue";
import RoleAuthorize from "@/views/system/role-authorize.vue";
import { NConfigProvider, NDropdown, NButton, NCheckbox } from "naive-ui";
import { themeSetting } from "@/constants/config";

const DEPT_ID = import.meta.env.VITE_DEPT_ID;
const deptList = ref([]);
const userList = ref([]);
const deptVisible = ref(false);
const userVisible = ref(false);

const getDeptList = async () => {
  const { data } = await baseService.get("/department/allTree");
  deptList.value = data;
};

const deptPayload = ref({});
const userPayload = ref({});

const currentDeptInfo = ref({});

// 编辑部门
const editHandler = (value: IObject) => {
  deptPayload.value = value;
  deptVisible.value = true;
};

// 部门添加员工
const addUserHandler = (value: IObject) => {
  userPayload.value = value;
  userVisible.value = true;
};

const userLoading = ref(false);
const roleLoading = ref(false);
// 部门查看员工
const showUserHandler = async (payload: IObject) => {
  currentDeptInfo.value = payload;
  currentRoleInfo.value = payload;
  try {
    userLoading.value = true;
    const { data } = await baseService.get("/department/users/" + payload.id);
    userLoading.value = false;
    userList.value = data;
    if (DEPT_ID === "2") {
      roleLoading.value = true;
      const { data: roleData } = await baseService.get("/role/dept/checked/list", { ofId: payload.id });
      roleLoading.value = false;
      roleList.value = roleData;
    }
  } catch (error) {
    userLoading.value = false;
    roleLoading.value = false;
    roleList.value = [];
    userList.value = [];
    console.log(error);
  }
};

// 添加部门
const addHandler = () => {
  deptPayload.value = {};
  deptVisible.value = true;
};

// 删除部门
const deleteHandler = (id: number) => {
  ElMessageBox.confirm("确定进行[删除]操作?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    try {
      await baseService.delete("/department/remove/" + id, {});
      ElMessage.success("删除成功");
      getDeptList();
    } catch (error) {
      console.log(error);
    }
  });
};

// 删除员工
const deleteUserHandler = (id: number) => {
  ElMessageBox.confirm("确定进行[删除]操作?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    try {
      await baseService.delete("/department/removeUser", { userId: id, removeOf: currentDeptInfo.value.id });
      ElMessage.success("删除成功");
      await showUserHandler(currentDeptInfo.value);
    } catch (error) {
      console.log(error);
    }
  });
};
interface ICurrentRoleInfo {
  userId?: number;
  subType?: number;
  id?: number;
}
const roleList = ref([]);
const currentRoleInfo = ref<ICurrentRoleInfo>({});

const showRoleByUserHandler = async (payload: IObject) => {
  currentRoleInfo.value = payload;
  try {
    roleLoading.value = true;
    const { data } = await baseService.get("/role/user/checked/list", { ofId: payload.userId });
    roleLoading.value = false;
    isModify.value = false;
    roleList.value = data;
  } catch (e) {
    roleLoading.value = false;
    roleList.value = [];
  }
};

const isModify = ref(false);

const roleChangeHandler = async (check?: boolean, value?: any) => {
  value.checked = check;

  console.log(check, value);
  try {
    await baseService.post("/role/grant", { subId: currentRoleInfo.value.id || currentRoleInfo.value.userId, subType: currentRoleInfo.value.subType, roleIds: roleList.value.filter((item: any) => item.checked).map((item: any) => item.roleId) });
    isModify.value = true;
    ElMessage.success("授权成功");
  } catch (e) {
    console.error(e);
  }
};

const themeOverrides = {
  common: {
    primaryColor: themeSetting.themeColor
  }
};
const showViewRole = ref<boolean>(false);
const rolePayload = ref<any>({});
const showRoleHandler = (value: any) => {
  showViewRole.value = true;
  rolePayload.value = { id: value.roleId, name: value.roleName };
};

const options = computed(() => {
  return roleList.value.map((item: any) => ({
    key: item.roleId,
    type: "render",
    render: () => h(NCheckbox, { checked: item.checked, disabled: item.disabled, style: { paddingLeft: "5px" }, onUpdateChecked: (checked) => roleChangeHandler(checked, item) }, () => item.roleName)
  }));
});

const dropdownHandler = () => {
  if (isModify.value) {
    showUserHandler(currentDeptInfo.value);
  }
};

onMounted(() => {
  getDeptList();
});
</script>

<template>
  <div class="page-wrapper">
    <div class="item-wrapper">
      <div class="item-wrapper-header">
        <el-tag>部门信息列表</el-tag>
        <el-button type="primary" size="small" @click="addHandler">添加</el-button>
      </div>
      <div class="item-wrapper-body">
        <el-table :data="deptList" row-key="id" default-expand-all border @row-click="showUserHandler">
          <el-table-column prop="name" label="部门名称">
            <template #default="scope">
              <el-link type="primary">{{ scope.row.name }}</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="action" label="操作" header-align="center" align="center" width="100px">
            <template #default="scope">
              <el-button size="small" link type="primary" @click.stop="editHandler(scope.row)">编辑</el-button>
              <el-button size="small" link type="danger" @click.stop="deleteHandler(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="item-wrapper">
      <div class="item-wrapper-header">
        <el-tag v-if="currentDeptInfo.name" type="primary">{{ currentDeptInfo?.name && `${currentDeptInfo.name} - ` }}员工</el-tag>
        <el-button size="small" type="primary" @click="addUserHandler(currentDeptInfo)">添加</el-button>
      </div>
      <div class="item-wrapper-body">
        <el-table v-loading="userLoading" :data="userList" border @row-click="showRoleByUserHandler">
          <el-table-column prop="nickname" label="用户昵称">
            <template #default="scope">
              <el-link type="primary">{{ scope.row.nickname }}</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="联系方式" />
          <el-table-column prop="role" label="角色" align="center">
            <template #default="scope">
              <div style="display: flex; flex-direction: column">
                <el-tag v-for="item in scope.row.roleInfos" :key="item.id">{{ item.roleName }}</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="action" label="操作" header-align="center" align="center" width="100">
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <NDropdown trigger="click" :options="options" @clickoutside="dropdownHandler">
                  <NButton type="primary" size="small" text>编辑</NButton>
                </NDropdown>
                <el-button size="small" link type="danger" @click.stop="deleteUserHandler(scope.row.userId)">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div v-if="DEPT_ID === '2'" class="item-wrapper">
      <div class="item-wrapper-header">
        <el-tag type="primary">{{ (currentRoleInfo?.nickname || currentRoleInfo?.name) && `${currentRoleInfo?.nickname || currentRoleInfo?.name} - ` }}权限</el-tag>
      </div>
      <div class="item-wrapper-body">
        <div v-loading="roleLoading" class="checkbox-wrapper">
          <el-checkbox v-for="item in roleList" :disabled="item.disabled" v-model="item.checked" :key="item.id" @change="roleChangeHandler" @mouseenter="item.showView = true" @mouseleave="item.showView = false">
            {{ item.roleName }}
            <el-icon v-if="item.showView" style="margin-left: 5px" @click.prevent="showRoleHandler(item)"> <Edit /> </el-icon>
          </el-checkbox>
        </div>
      </div>
    </div>

    <!-- 部门添加 -->
    <DepartmentModify v-if="deptVisible" v-model="deptVisible" :payload="deptPayload" @refreshData="getDeptList" />

    <!-- 选择员工 -->
    <DepartmentSelectUser v-if="userVisible" v-model="userVisible" :payload="userPayload" @refreshData="showUserHandler(currentDeptInfo)" />

    <!--  查看授权  -->
    <!-- 授权 -->
    <n-config-provider :theme-overrides="themeOverrides">
      <RoleAuthorize v-if="showViewRole" v-model="showViewRole" :payload="rolePayload" />
    </n-config-provider>
  </div>
</template>

<style lang="less">
.checkbox-wrapper {
  .el-checkbox__label {
    display: flex;
    align-items: center;
  }
}
</style>

<style lang="less" scoped>
.page-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  gap: 20px;

  .item-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 20px;

    > div {
      border: 1px solid #dcdfe6;
      border-radius: 10px;
    }

    &-header {
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;

      > .el-button {
        position: absolute;
        right: 10px;
      }
    }
    &-body {
      flex: 1;
      padding: 10px;
    }

    .checkbox-wrapper {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;

      .el-checkbox {
        width: calc((100% - 20px) / 2);
        margin-right: 0;
        display: flex;
        align-items: center;
      }
    }
  }

  .el-button + .el-button {
    margin-left: 0;
  }
}
</style>
