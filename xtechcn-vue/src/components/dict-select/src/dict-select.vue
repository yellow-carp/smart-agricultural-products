<template>
  <el-select v-model="activeValue" @change="$emit('update:modelValue', $event)" :placeholder="placeholder" clearable>
    <el-option v-for="data in dataList" :key="data.code" :label="data.name" :value="data.code" />
  </el-select>
</template>
<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import { getDictDataList } from "@/utils/utils";
import { useAppStore } from "@/store";

export default defineComponent({
  name: "DictSelect",
  props: {
    modelValue: [Number, String, null],
    dictType: String,
    placeholder: String
  },
  setup(props) {
    const store = useAppStore();
    const activeValue = ref(null);
    const dataList = ref([])

    const init = () => {
      dataList.value = getDictDataList(store.state.dicts, props.dictType)
    }

    onMounted(() => {
      init()
    });

    watch(
      () => props.modelValue,
      (newCode) => {
        if (newCode) {
          activeValue.value = props.modelValue;
        }else {
          activeValue.value = null
        }
      }, {immediate: true}
    );

    return {
      activeValue,
      dataList,
    };
  }
});
</script>
