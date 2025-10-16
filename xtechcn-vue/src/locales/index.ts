import { createI18n } from "vue-i18n";
import zh from "./zh";
import en from "./en";

const i18n = createI18n({
  legacy: false, // 默认使用 composition API，不启用旧版的 API 兼容模式，是 vue-i18n v9 引入的，为了更好地与 Vue 3 的设计理念保持一致。使用 Composition API 可以提供更灵活的代码组织和更好的类型支持。
  locale: "zh", // 当前使用的语言
  fallbackLocale: "zh", // 找不到对应的语言时，使用 fallbackLocale对应的语言
  messages: {
    zh, // 提供的中文语言
    en // 提供的英文语言
  }
});

export default i18n; // 导出
