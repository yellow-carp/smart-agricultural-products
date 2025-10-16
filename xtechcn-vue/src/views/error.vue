<script lang="ts">
import { IObject } from "@/types/interface";
import { defineComponent } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useI18n } from "vue-i18n";

interface ITip {
  title: string;
  message: string;
}

export default defineComponent({
  setup() {
    const route = useRoute();
    const router = useRouter();
    const { to } = route.query;
    const { t } = useI18n();
    const tips: IObject = {
      404: {
        title: "404",
        message: t("base.no_access_page")
      },
      error: {
        title: t("base.error"),
        message: t("base.access_error")
      }
    };
    const tip: ITip = tips[to?.toString() ?? "error"];
    const onBack = () => {
      router.back();
    };
    const onToHome = () => {
      router.replace("/");
    };
    return { tip, onBack, onToHome };
  }
});
</script>

<template>
  <div class="rr-error">
    <el-result>
      <template v-slot:icon>
        <el-icon style="font-size: 64px; color: #f5222d"><warning /></el-icon>
      </template>
      <template v-slot:title>
        <span style="font-size: 48px; font-weight: 800">{{ tip.title }}</span>
      </template>
      <template v-slot:subTitle>
        <span style="font-size: 36px">{{ tip.message }}</span>
      </template>
      <template v-slot:extra>
        <el-space :size="30">
          <el-button type="info" plain @click="onBack"> {{ $t("base.back") }} </el-button>
          <el-button type="primary" @click="onToHome"> {{ $t("base.home") }} </el-button>
        </el-space>
      </template>
    </el-result>
  </div>
</template>
