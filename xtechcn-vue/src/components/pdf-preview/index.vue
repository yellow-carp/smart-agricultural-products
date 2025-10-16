<script setup lang="ts">
import { computed, onMounted, reactive } from "vue";
import VuePdfEmbed from "vue-pdf-embed";
import { createLoadingTask } from "vue3-pdfjs";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  source: {
    type: String,
    default: ""
  }
});
const emit = defineEmits(["update:modelValue"]);

const show = computed({
  get() {
    return props.modelValue;
  },
  set(val: boolean) {
    emit("update:modelValue", val);
  }
});

const state = reactive({
  source: props.source,
  currentPage: 1,
  totalPages: 0
});

onMounted(() => {
  if (props.source) {
    const loadingTask = createLoadingTask(state.source);
    loadingTask.promise.then((pdf) => {
      state.totalPages = pdf.numPages;
    });
  }
});
</script>
<template>
  <el-dialog v-model="show" title="交易凭证查看" width="50%" lock-scroll>
    <vue-pdf-embed class="pdf-content" v-if="source" :source="source" :page="state.currentPage" />
    <div class="pdf-footer">
      <el-button @click="state.currentPage--" :disabled="state.currentPage === 1">上一页</el-button>
      <span>第 {{ state.currentPage }} / {{ state.totalPages }} 页</span>
      <el-button @click="state.currentPage++" :disabled="state.currentPage === state.totalPages">下一页</el-button>
    </div>
  </el-dialog>
</template>

<style lang="less" scoped>
.pdf-content {
  height: 500px;
  overflow-y: auto;
}
.pdf-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 10px;
}
</style>
