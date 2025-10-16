<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
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

const show = computed({
  get: () => props.modelValue,
  set: (val: boolean) => emit("update:modelValue", val)
});

const title = ref("");
const dataFormRef = ref();

const formData = reactive({
  id: null,
  name: "",
  notes: "",
  type: "",
  add: false
});

const confirmHandler = async () => {
  await dataFormRef.value?.validate();
  formData.id ? (formData.add = false) : (formData.add = true);
  await baseService.post("/material/group/upsert", formData);
  ElMessage.success(formData.id ? "修改成功" : "添加成功");
  show.value = false;
  emit("refreshData");
};

onMounted(() => {
  title.value = props.payload.id ? "修改素材组 - " + props.payload.name : "新增素材组";

  Object.assign(formData, props.payload);
});
</script>

<template>
  <el-dialog v-model="show" :title="title" width="600px">
    <el-form ref="dataFormRef" :model="formData" label-width="100px">
      <el-form-item label="素材组名称" prop="name" :rules="{ required: true, message: '请输入名称', trigger: 'blur' }">
        <el-input v-model="formData.name" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="素材组类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择类型">
          <el-option label="图片" value="1" />
          <el-option label="视频" value="2" />
          <el-option label="音频" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="描述" prop="notes">
        <el-input v-model="formData.notes" type="textarea" show-word-limit maxlength="100" placeholder="请输入描述" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="confirmHandler">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="less"></style>
