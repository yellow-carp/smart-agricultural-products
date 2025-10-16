<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

const DEPT_ID = import.meta.env.VITE_DEPT_ID;

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
const show = computed({
  get: () => props.modelValue,
  set: (val: boolean) => emit("update:modelValue", val)
});

const menuList = ref<any[]>([]);

const deptFormData = reactive({
  id: null,
  name: "",
  pid: 0
});

// 获取部门菜单
const getDeptList = async () => {
  const res = await baseService.get("/department/allTree");

  if (DEPT_ID === "1") {
    menuList.value = res.data;
  } else {
    menuList.value = [
      {
        id: 0,
        name: "顶级部门",
        children: res.data
      }
    ];
  }
};

const confirmHandler = async () => {
  try {
    await (deptFormData.id ? baseService.put : baseService.post)(deptFormData.id ? "/department/update" : "/department/add", deptFormData);
    ElMessage.success(deptFormData.id ? "修改成功" : "添加成功");
    show.value = false;
    emit("refreshData");
  } catch (error) {
    console.log(error);
  }
};

const clearHandler = () => {
  deptFormData.pid = 0;
};

const rules = {
  name: [{ required: true, message: "请输入部门名称", trigger: "blur" }],
  pid: [{ required: true, message: "请选择上级部门", trigger: "change" }]
};

const pName = ref({});
onMounted(async () => {
  await getDeptList();

  pName.value = menuList.value[0];
  deptFormData.pid = menuList.value[0].id;
  if (props.payload?.id) {
    Object.assign(deptFormData, props.payload);
  }
});
</script>

<template>
  <el-dialog v-model="show" :title="props.payload?.id ? '编辑部门' : '新增部门'" width="30%">
    <el-form :model="deptFormData" label-width="100px" ref="deptFormRef" :rules="rules">
      <el-form-item label="部门名称" prop="name">
        <el-input v-model="deptFormData.name" placeholder="请输入部门名称" clearable />
      </el-form-item>
      <el-form-item label="上级部门" prop="pid" v-if="!payload.id || payload?.pid === pName.id">
        <el-input v-if="DEPT_ID === '1'" v-model="pName.name" placeholder="请输入部门名称" readonly />
        <el-tree-select v-if="DEPT_ID === '2'" v-model="deptFormData.pid" check-strictly :data="menuList" :props="{ label: 'name', value: 'id' }" :render-after-expand="false" style="width: 100%" clearable @clear="clearHandler" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="show = false">取消</el-button>
        <el-button type="primary" @click="confirmHandler">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped lang="less"></style>
