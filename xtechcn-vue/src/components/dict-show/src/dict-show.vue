<script lang="ts">
import { useAppStore } from "@/store";
import { computed, defineComponent } from "vue";
import { IObject } from "@/types/interface";

export default defineComponent({
  name: "DictShow",
  props: {
    modelValue: [String, Number],
    type: String,
  },
  setup(props) {

    const appStore = useAppStore();

    const list = appStore.getDict(props.type);

    const info: IObject = computed(() => {
      return (
        list.find((item: any) => item.code == props.modelValue) || {
          name: "/",
          color: ""
        }
      );
    });
    return {
      info
    };
  }
});

</script>

<template>
  <span :style="{ color: info.color }" type="success">{{ info.name }}</span>
</template>
