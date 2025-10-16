<template>
  <el-cascader ref="cascaderRef" v-model="location" @change="regionChange" :options="regionList" clearable
               :props="cascaderProps" filterable />
</template>
<script lang="ts">
import { defineComponent, onMounted, ref, watch } from "vue";
import { getChinaRegionTree } from "@/api/upms/region";

export default defineComponent({
  name: "RegionSelect",
  props: {
    code: {
      type: String,
      default: null
    },
    name: {
      type: String,
      default: null
    }
  },
  emits: ["update:code", "update:name"],
  setup(props, { emit }) {
    const cascaderRef = ref(null);
    const regionList = ref([]);
    const cascaderProps = { label: "name", value: "code", expandTrigger: "hover" };
    const location = ref(null);

    const getRegionName = () => {
      const content = cascaderRef.value?.presentText;
      return content.replaceAll(" / ", "^");
    };

    const regionChange = (codeList) => {
      if (codeList) {
        emit("update:code", codeList.join("^"));
        emit("update:name", getRegionName());
      } else {
        emit("update:code", null);
        emit("update:name", null);
      }
    };

    const init = async () => {
      await getChinaRegionTree().then(res => {
        regionList.value = res.data;
      });
      if (props.code) {
        location.value = props.code.split("^");
      } else {
        location.value = null;
        cascaderRef.value.cascaderPanelRef.clearCheckedNodes();
      }
    };

    onMounted(() => {
      init();
    });

    watch(
      () => props.code,
      (newCode) => {
        if (newCode) {
          location.value = newCode.split("^");
        } else {
          location.value = null;
          cascaderRef.value.cascaderPanelRef.clearCheckedNodes();
        }
      }
    );

    return {
      regionChange,
      cascaderRef,
      regionList,
      cascaderProps,
      location
    };
  }
});
</script>
