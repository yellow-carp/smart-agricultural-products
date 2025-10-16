<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  payload: {
    type: Object,
    default: () => ({})
  }
});
const emit = defineEmits(["update:modelValue", "refreshData"]);
const visible = computed({
  get: () => props.modelValue,
  set: (val: boolean) => emit("update:modelValue", val)
});

const allUserTable = ref();

const allUser = ref([]);
const selectList = ref([]);

// 获取所有用户
const getAllUserHandler = async () => {
  try {
    const { data } = await baseService.get("/user/page", { size: 500 });
    allUser.value = data.records;
  } catch (e) {
    console.error(e);
  }
};
// 获取当前部门下的用户
const getDeptUserHandler = async () => {
  try {
    const { data } = await baseService.get("/department/users/" + props.payload.id);
    selectList.value = data;
  } catch (e) {
    console.error(e);
  }
};

// 提交
const confirmHandler = async () => {
  try {
    await baseService.post("/department/addUsers", {
      userIds: selectList.value.map((item: any) => item.userId),
      joinTo: props.payload.id
    });
    visible.value = false;
    ElMessage.success("添加成功");
    emit("refreshData");
  } catch (e) {
    console.error(e);
  }
};

onMounted(async () => {
  await getAllUserHandler();
  await getDeptUserHandler();

  const list = allUser.value.filter((item: any) => {
    return selectList.value.some((selectItem: any) => {
      return selectItem.userId === item.userId;
    });
  });

  list.forEach((item: any) => {
    allUserTable.value?.toggleRowSelection(item);
  });
});
</script>

<template>
  <el-dialog v-model="visible" :title="`选择员工 - ${props.payload.name}`" width="50vw" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="content-box">
      <div class="item-wrapper">
        <div class="item-wrapper-header">
          <span>共{{ allUser.length }}项</span>
          <el-input size="small" placeholder="搜索" clearable @clear="getAllUserHandler" />
        </div>
        <div class="item-wrapper-body">
          <el-table ref="allUserTable" :data="allUser" row-key="userId" border @selection-change="selectList = $event">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="nickname" label="用户昵称" />
          </el-table>
        </div>
      </div>
      <div class="item-wrapper">
        <div class="item-wrapper-header">
          <span>共{{ selectList.length }}项</span>
          <el-input size="small" placeholder="搜索" clearable @clear="getAllUserHandler" />
        </div>
        <div class="item-wrapper-body">
          <el-table :data="selectList" border>
            <el-table-column prop="nickname" label="用户昵称" />
          </el-table>
        </div>
      </div>
    </div>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="confirmHandler">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="less">
.content-box {
  height: 500px;
  border: 1px solid #dcdfe6;
  border-radius: 10px;
  display: flex;

  .item-wrapper {
    flex: 1;
    padding: 10px;
    display: flex;
    flex-direction: column;

    &-header {
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      font-size: 12px;

      .el-input {
        flex: 1;
      }
    }
    &-body {
      flex: 1;
    }

    & + .item-wrapper {
      border-left: 1px solid #dcdfe6;
    }
  }
}
</style>
