<template>
  <el-dialog v-model="visible" width="600" :close-on-click-modal="false" :close-on-press-escape="false">
    <video v-if="payload.fileType === 3" autoplay :src="payload.url" controls style="width: 100%; height: 500px" />
    <audio v-else-if="payload.fileType === 4" :src="payload.url" controls />
    <img v-else :src="payload.url" alt="" />
  </el-dialog>
</template>

<script lang="ts" setup>
import { computed } from "vue";

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
</script>
