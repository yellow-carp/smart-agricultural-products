<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :width="600" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="100px">
      <el-form-item prop="sort" label="排序">
        <el-input-number v-model="dataForm.sort" style="width: 100%" controls-position="right" placeholder="排序" />
      </el-form-item>
      <el-form-item prop="value" label="字典名称">
        <el-input v-model="dataForm.value" placeholder="字典名称" />
      </el-form-item>
      <el-form-item prop="code" label="字典值">
        <el-input v-model="dataForm.code" placeholder="字典值" />
      </el-form-item>
      <el-form-item prop="color" label="颜色">
        <el-color-picker v-model="dataForm.color" :predefine="predefineColors"> </el-color-picker>
      </el-form-item>
      <el-form-item prop="name" label="描述">
        <el-input v-model="dataForm.name" placeholder="描述" />
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import { IObject } from "@/types/interface";

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
const emit = defineEmits(["update:modelValue", "refreshDataList"]);

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
  dictId: null,
  sort: 0,
  name: "",
  code: "",
  color: "",
  value: ""
});
const dataForm = reactive(initData());

const predefineColors = ["#ff4500", "#ff8c00", "#ffd700", "#90ee90", "#00ced1", "#1e90ff", "#c71585"];

const rules = ref({
  value: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  code: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  sort: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

// 表单提交
const dataFormSubmitHandle = async () => {
  await dataFormRef.value.validate();
  baseService.post("/dict/item/upsert", dataForm).then(() => {
    ElMessage.success({
      message: "成功",
      duration: 500,
      onClose: () => {
        visible.value = false;
        emit("refreshDataList");
      }
    });
  });
};

onMounted(() => {
  Object.assign(dataForm, props.payload);

  if (!dataForm.id) {
    dataForm.sort = props.payload.index;
  }
});
</script>
