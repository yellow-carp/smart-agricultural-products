import "@/assets/icons/iconfont/iconfont.js";
import DictSelect from "@/components/dict-select";
import DictShow from "@/components/dict-show";
import "element-plus/theme-chalk/display.css";
import "element-plus/theme-chalk/index.css";
import ElementPlus from "element-plus";
import locale from "element-plus/es/locale/lang/zh-cn";
import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import store from "@/store/plugin/index";
import axios from "axios";
import "virtual:svg-icons-register";
import "@/style/index.less";
import "@/style/theme.less";

// 引入i18n - 国际化
import i18n from "./locales/index.js";

import "element-plus/es/components/message/style/css";

// 添加类型声明文件或使用动态导入
// 方法一：添加类型声明文件
// 在项目根目录下创建 declarations.d.ts 并添加 declare module 'workflow-ui/src/components/Generator/node';

// import Node from "workflow-ui/src/components/Generator/node"; // 或者使用动态导入 const Node = import('workflow-ui/src/components/Generator/node');

const app = createApp(App);
import FileUpload from "@/components/FileUpload/index.vue";

Object.keys(ElementPlusIcons).forEach((iconName) => {
  app.component(iconName, ElementPlusIcons[iconName as keyof typeof ElementPlusIcons]);
});

app.component("FileUpload", FileUpload); // 初始化组件

app
  .use(store)
  .use(router)
  .use(DictSelect)
  .use(DictShow)
  .use(ElementPlus, { size: "default", locale: locale })
  .use(i18n)
  .mount("#app");

app.directive("focus", {
  mounted(el) {
    el.focus();
  }
});

window.axios = axios;
