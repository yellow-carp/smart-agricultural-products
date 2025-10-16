<script setup lang="ts">
import { reactive, ref, computed, onMounted } from "vue";
import VueCron from "@/components/vue-cron/index.vue";
import DictSelect from "@/components/dict-select/src/dict-select.vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
// import { noVue3Cron } from "no-vue3-cron";
// import "no-vue3-cron/lib/noVue3Cron.css";

const props = defineProps<{
  modelValue: boolean;
  payload: any;
}>();

const emit = defineEmits(["update:modelValue", "refreshData"]);

const show = computed({
  get: () => props.modelValue,
  set: (val: boolean) => emit("update:modelValue", val)
});

const formData = reactive({
  jobId: "",
  jobName: "",
  clazzName: "",
  methodName: "",
  parameters: "",
  cronExpression: "",
  misfirePolicy: "",
  description: ""
});

const title = ref("");
const formDataRef = ref();
const popoverVisible = ref(false);

const rules = {
  jobName: [{ required: true, message: "请输入任务名称", trigger: "blur" }],
  clazzName: [{ required: true, message: "请输入任务类名", trigger: "blur" }],
  methodName: [{ required: true, message: "请输入任务方法名", trigger: "blur" }],
  parameters: [{ required: true, message: "请输入任务参数", trigger: "blur" }],
  cronExpression: [{ required: true, message: "请输入任务表达式", trigger: "blur" }],
  misfirePolicy: [{ required: true, message: "请输入任务策略", trigger: "blur" }]
};

const changeCron = (cron: string) => {
  formData.cronExpression = cron;
  popoverVisible.value = false;
};

const confirmHandler = async () => {
  await formDataRef.value?.validate();
  try {
    await baseService.post("/task/upsert", formData);
    ElMessage.success(formData.jobId ? "修改成功" : "添加成功");
    show.value = false;
    emit("refreshData");
  } catch (error) {
    console.log(error);
  }
};

onMounted(() => {
  title.value = (props.payload.jobName && "任务修改 - " + (props.payload.jobName || "")) || "任务添加";
  props.payload.jobId && Object.assign(formData, props.payload);
});
</script>

<template>
  <el-dialog v-model="show" width="50%" :title="title">
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-width="120px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="jobName">
            <el-input v-model="formData.jobName" placeholder="任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务类名" prop="clazzName">
            <el-input v-model="formData.clazzName" placeholder="任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方法名" prop="methodName">
            <el-input v-model="formData.methodName" placeholder="方法名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行参数">
            <el-input v-model="formData.parameters" placeholder="执行参数" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="CRON表达式" prop="cronExpression">
            <el-input v-model="formData.cronExpression" placeholder="CRON表达式">
              <template #append>
                <el-button type="primary" @click="popoverVisible = true">选择</el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="错失执行策略" prop="misfirePolicy">
            <dict-select v-model="formData.misfirePolicy" dictType="MISFIRE_POLICY" placeholder="请选择错失执行策略" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="description">
            <el-input v-model="formData.description" type="textarea" placeholder="任务名称" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="confirmHandler">确定</el-button>
    </template>

    <el-dialog v-model="popoverVisible" placement="top-start" trigger="click" :width="750" :close-on-click-modal="false">
      <VueCron @resultChange="changeCron" />
    </el-dialog>
  </el-dialog>
</template>

<style lang="less">
.popover-pop {
  max-height: 420px !important;
}
</style>
