<script setup lang="ts">
import { computed } from "vue";
import MaterialStore from "@/views/material/components/materialStore.vue";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(["update:modelValue", "confirm"]);

const show = computed({
  get() {
    return props.modelValue;
  },
  set(val: boolean) {
    emit("update:modelValue", val);
  }
});

const selectHandler = (val: any) => {
  show.value = false;
  emit("confirm", val);
};
</script>

<template>
  <el-dialog v-model="show" width="90%" title="选择素材" top="5vh" lock-scroll :close-on-click-modal="false" :close-on-press-escape="false">
    <MaterialStore height="calc(100vh - 200px)" :showSelected="true" @confirm="selectHandler" />
  </el-dialog>
</template>

<style scoped lang="less">
.main-wrapper {
  display: flex;
  gap: 10px;
}
</style>
