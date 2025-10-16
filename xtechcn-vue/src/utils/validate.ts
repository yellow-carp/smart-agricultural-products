import { isMobile } from "@/utils/utils";

// 手机号校验
export const validateMobile = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (!value) return callback(new Error("必填项不能为空"));
  if (value && !isMobile(value)) {
    return callback(new Error("手机格式错误"));
  }
  callback();
};
