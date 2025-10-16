<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import baseService from "@/service/baseService";
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

const emits = defineEmits(["update:modelValue", "refreshDataList"]);

const show = computed({
  get() {
    return props.modelValue;
  },
  set(val: boolean) {
    emits("update:modelValue", val);
  }
});

const formData = reactive({
  id: null,
  name: "",
  description: "",
  code: "",
  add: false,
  update: false
});

const rules = {
  name: [{ required: true, message: "请输入角色名称", trigger: "blur" }]
};
const dataFormRef = ref();

const confirmHandler = async () => {
  try {
    await dataFormRef.value.validate();
    if (!formData.id) {
      formData.add = true;
    } else {
      formData.update = true;
    }
    await baseService.post("/role/upsert", formData);
    ElMessage.success(formData.id ? "修改成功" : "添加成功");
    show.value = false;
    emits("refreshDataList");
  } catch (error) {
    console.log(error);
  }
};

onMounted(() => {
  props.payload.id && Object.assign(formData, props.payload);
});
</script>

<template>
  <el-dialog v-model="show" :title="payload.id ? '角色修改' : '角色新增'" width="25vw" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form ref="dataFormRef" :model="formData" :rules="rules" @keyup.enter="confirmHandler" label-width="100px" label-suffix=":">
      <el-form-item prop="name" label="角色名称">
        <el-input v-model="formData.name" placeholder="角色名称" />
      </el-form-item>
      <el-form-item prop="code" label="权限标识">
        <el-input v-model="formData.code" placeholder="权限标识" />
      </el-form-item>
      <el-form-item prop="description" label="描述">
        <el-input type="textarea" v-model="formData.description" placeholder="描述" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="confirmHandler">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="less"></style>
