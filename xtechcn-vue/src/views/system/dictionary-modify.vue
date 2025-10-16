<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" width="600" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="name" label="字典名称">
        <el-input v-model="dataForm.name" placeholder="字典名称" />
      </el-form-item>
      <el-form-item prop="code" label="字典类型">
        <el-input v-model="dataForm.code" placeholder="字典类型" />
      </el-form-item>
      <el-form-item prop="remark" label="备注">
        <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import { IObject } from "@/types/interface";

const emit = defineEmits(["refreshDataList"]);
const visible = ref(false);
const dataFormRef = ref();

const initData = () => ({
  id: null,
  name: "",
  code: "",
  sort: 0,
  remark: ""
});

const dataForm = reactive(initData());
const init = (payload?: IObject) => {
  visible.value = true;
  dataForm.id = null;

  // 重置表单数据
  dataFormRef.value?.resetFields();

  Object.assign(dataForm, initData());

  if (payload) {
    Object.assign(dataForm, payload);
  }
};

const rules = ref({
  name: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  code: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  sort: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

// 表单提交
const dataFormSubmitHandle = async () => {
  await dataFormRef.value.validate();
  baseService.post("/dict/upsert", { ...dataForm, add: !dataForm.id }).then(() => {
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

defineExpose({
  init
});
</script>
