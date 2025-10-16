<script lang="ts">
import { EMitt, ESidebarLayoutEnum, EThemeSetting } from "@/constants/enum";
import emits from "@/utils/emits";
import { getThemeConfigCacheByKey } from "@/utils/theme";
import { defineComponent, reactive } from "vue";
import { useAppStore } from "@/store";
import BaseSidebar from "../sidebar/base-sidebar.vue";
import Breadcrumb from "./breadcrumb.vue";
import CollapseSidebarBtn from "./collapse-sidebar-btn.vue";
import Expand from "./expand.vue";
import HeaderMixNavMenus from "./header-mix-nav-menus.vue";
import "@/assets/css/header.less";

/**
 * 顶部主区域
 */
export default defineComponent({
  name: "Header",
  components: { BaseSidebar, Breadcrumb, CollapseSidebarBtn, Expand, HeaderMixNavMenus },
  setup() {
    const store = useAppStore();
    const state = reactive({
      sidebarLayout: getThemeConfigCacheByKey(EThemeSetting.NavLayout)
    });
    emits.on(EMitt.OnSetNavLayout, (vl) => {
      state.sidebarLayout = vl;
    });
    const onRefresh = () => {
      emits.emit(EMitt.OnReloadTabPage);
    };
    return { store, state, onRefresh, ESidebarLayoutEnum };
  }
});
</script>
<template>
  <div class="rr-header-ctx">
    <div class="rr-header-ctx-logo hidden-xs-only">蔬菜批发·后台管理</div>
    <div class="rr-header-right">
      <div class="rr-header-right-left">
        <div class="rr-header-right-items rr-header-action" :style="`display:${state.sidebarLayout === ESidebarLayoutEnum.Top ? 'none' : ''}`">
          <collapse-sidebar-btn></collapse-sidebar-btn>
          <div @click="onRefresh" style="cursor: pointer">
            <div class="el-badge">
              <el-icon><refresh-right /></el-icon>
            </div>
          </div>
        </div>
        <div class="rr-header-right-left-br ele-scrollbar-hide hidden-xs-only">
          <base-sidebar v-if="state.sidebarLayout === ESidebarLayoutEnum.Top" mode="horizontal" :router="true"></base-sidebar>
          <header-mix-nav-menus v-else-if="state.sidebarLayout === ESidebarLayoutEnum.Mix"></header-mix-nav-menus>
          <breadcrumb v-else></breadcrumb>
        </div>
      </div>
      <div style="flex-shrink: 0">
        <expand :userName="store.state.user.realName || store.state.user.username"></expand>
      </div>
    </div>
  </div>
</template>
