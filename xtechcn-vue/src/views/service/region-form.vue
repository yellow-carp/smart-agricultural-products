<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '编辑'" width="600" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="name" label="区域名称">
        <el-input v-model="dataForm.name" placeholder="请输入区域名称" />
      </el-form-item>
      <el-form-item prop="type" label="区域类型">
        <dict-select v-model="dataForm.type" dictType="SERVICE_REGION_TYPE" placeholder="请选择区域类型" />
      </el-form-item>
      <el-form-item prop="mode" label="物流模式">
        <dict-select v-model="dataForm.mode" dictType="SERVICE_LOGISTICS_MODE" placeholder="请选择物流模式" />
      </el-form-item>
      <el-form-item prop="location" label="行政区划">
        <region-select v-model:code="dataForm.regionCode" v-model:name="dataForm.regionName"></region-select>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import { IObject } from "@/types/interface";
import DictSelect from "@/components/dict-select/src/dict-select.vue";
import RegionSelect from "@/components/region-select/src/region-select.vue";

const emit = defineEmits(["refreshDataList"]);
const visible = ref(false);
const dataFormRef = ref();

const initData = () => ({
  id: null,
  name: null,
  type: null,
  mode: null,
  regionCode: null,
  regionName: null,
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
  type: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  mode: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

// 表单提交
const dataFormSubmitHandle = async () => {
  console.log(dataForm);
  // await dataFormRef.value.validate();

  // baseService.post("/dict/upsert", { ...dataForm, add: !dataForm.id }).then(() => {
  //   ElMessage.success({
  //     message: "成功",
  //     duration: 500,
  //     onClose: () => {
  //       visible.value = false;
  //       emit("refreshDataList");
  //     }
  //   });
  // });
};

defineExpose({
  init
});

</script>

<style scoped lang="less">

</style>
