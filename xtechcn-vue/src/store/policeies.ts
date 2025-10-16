// store index 是框架自带 这里的 user 是一个扩展
import { defineStore } from "pinia";
import { ref } from "vue";
import { IObject } from "@/types/interface";
import baseService from "@/service/baseService";

export const usePoliciesStore = defineStore(
  "policies-store",
  () => {
    const policiesInfo = ref<IObject>({});

    const delPoliciesInfo = () => {
      policiesInfo.value = {};
    };

    const syncGetPolicyInfo = async () => {
      const { data } = await baseService.get("/file/policies/sts");
      policiesInfo.value = data;

      return policiesInfo.value;
    };

    return { policiesInfo, delPoliciesInfo, syncGetPolicyInfo };
  },
  {
    persist: {
      key: "policies",
      storage: sessionStorage
    }
  }
);
