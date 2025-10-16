// 用户名 请求 商户
import baseService from "@/service/baseService";

export const queryMersByUserName = "/system/user/queryMersByUserName";

export const getPackageList = (params: any): Promise<any> =>
  baseService.get("/sys/merchantpackage/page", params);
export const getPackageDetail = (params: any): Promise<any> =>
  baseService.getById("/system/merchantpackage/detail", params);
