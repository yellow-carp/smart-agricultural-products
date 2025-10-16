import baseService from "@/service/baseService";

/**
 * 获取中国地理树
 * @param params 查询参数
 */
export const getChinaRegionTree = params => baseService.get('/region/tree/all', params)
