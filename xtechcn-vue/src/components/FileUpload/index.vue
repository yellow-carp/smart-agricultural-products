<script lang="ts" setup>
import { computed, onMounted, ref, watch } from "vue";
import { ElMessage, UploadUserFile } from "element-plus";
import { getToken } from "@/utils/cache";
import app from "@/constants/app";
import { UploadFilled } from "@element-plus/icons-vue";

const URL = `${app.api}/sys/oss/upload`;

// 定义支持的文件格式
const SUPPORTED_FORMATS = {
  office: ["doc", "xls", "docx", "xlsx", "ppt", "pptx"],
  image: ["jpg", "jpeg", "png"],
  pdf: ["pdf"]
};

const prop = defineProps({
  value: {
    type: Array,
    default: () => []
  },
  tip: {
    type: String,
    default: "只能上传doc、docx、xls、xlsx、ppt、pptx、pdf、jpg、jpeg、png等格式的文件"
  },
  limit: {
    type: Number,
    default: 99
  },
  accept: {
    type: String,
    default: ".doc,.xls,.docx,.xlsx,.ppt,.pptx,.pdf,.jpg,.png,.jpeg,.ofd"
  },
  disabled: {
    type: Boolean,
    default: false
  },
  isTip: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(["update:value"]);

const fileList = ref<UploadUserFile[]>([]);

const headers = computed(() => ({
  token: getToken()
}));

const handleExceed = () => {
  ElMessage.error(`当前最多只能上传${prop.limit}份文件`);
};

const errorHandler = () => {
  ElMessage.error("文件上传失败，请联系管理员！");
};

watch(
  () => fileList.value,
  () => {
    emit("update:value", fileList.value);
  }
);

onMounted(() => {
  fileList.value = JSON.parse(JSON.stringify(prop.value));
});
</script>

<template>
  <el-upload ref="upload" style="width: 100%" multiple v-model:file-list="fileList" :action="URL" :accept="accept" :headers="headers" :on-error="errorHandler" :limit="limit" :on-exceed="handleExceed" :disabled="disabled">
    <el-button size="small" type="primary" :disabled="disabled" plain>
      <UploadFilled />
      点击上传
    </el-button>
    <div v-if="isTip" style="margin-left: 10px" class="el-upload__tip">{{ tip }}</div>
  </el-upload>
</template>
