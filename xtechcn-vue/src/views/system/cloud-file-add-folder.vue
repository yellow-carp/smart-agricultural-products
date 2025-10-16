<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" width="600" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="name" label="文件名称">
        <el-input v-model="dataForm.name" placeholder="字典类型" />
      </el-form-item>
      <el-form-item prop="notes" label="备注">
        <el-input v-model="dataForm.notes" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref, computed, onMounted } from "vue";
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

const emit = defineEmits(["refreshDataList", "update:modelValue"]);
const visible = computed({
  get() {
    return props.modelValue;
  },
  set(val: boolean) {
    emit("update:modelValue", val);
  }
});

const dataFormRef = ref();
const initData = () => ({
  id: null,
  pid: 0,
  name: "",
  notes: ""
});
const dataForm = reactive(initData());
const rules = ref({
  pid: [{ required: true, message: "请选择上级目录", trigger: "blur" }],
  name: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

const loading = ref(false);
const dataFormSubmitHandle = async () => {
  try {
    loading.value = true;
    await baseService.post("/cfs/mkdir", dataForm);
    loading.value = false;
    ElMessage.success("成功");
    visible.value = false;
    emit("refreshDataList");
  } catch (error) {
    loading.value = false;
    console.error(error);
  }
};

onMounted(() => {
  if (props.payload?.id) {
    Object.assign(dataForm, props.payload);
  }
});
</script>
