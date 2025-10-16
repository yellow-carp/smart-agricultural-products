import { IHttpResponse, IObject } from "@/types/interface";
import http from "../utils/http";
import { ElMessage } from "element-plus";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import saveAs from "save-as";

/**
 * 常用CRUD
 */
export default {
  /**
   * 删除
   * @param path
   * @param params
   * @returns
   */
  delete(path: string, params: IObject): Promise<IHttpResponse> {
    return http({
      url: path,
      data: params,
      method: "DELETE"
    });
  },
  get(path: string, params?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return new Promise((resolve, reject) => {
      http({
        url: path,
        params,
        headers,
        method: "GET"
      })
        .then(resolve)
        .catch((error) => {
          if (error !== "-999") {
            reject(error);
          }
        });
    });
  },
  getById(
    path: string,
    urlExtension: string,
    params?: IObject,
    headers?: IObject
  ): Promise<IHttpResponse> {
    return new Promise((resolve, reject) => {
      http({
        url: path + "/" + urlExtension,
        params,
        headers,
        method: "GET"
      })
        .then(resolve)
        .catch((error) => {
          if (error !== "-999") {
            reject(error);
          }
        });
    });
  },
  put(path: string, params?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return http({
      url: path,
      data: params,
      headers: {
        "Content-Type": "application/json;charset=UTF-8",
        ...headers
      },
      method: "PUT"
    });
  },

  /**
   * 通用post方法
   * @param path
   * @param body
   * @param headers
   * @returns
   */
  post(path: string, body?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return http({
      url: path,
      method: "post",
      headers: {
        "Content-Type": "application/json;charset=UTF-8",
        ...headers
      },
      data: body
    });
  },

  getExcel(path: string, params?: IObject, headers?: IObject, fileName?: string): any {
    http({
      url: path,
      params,
      headers,
      method: "GET",
      responseType: "blob"
    }).then((value: any) => {
      if (value.message) return ElMessage.error(value.message);
      const blob = new Blob([value], { type: "application/vnd.ms-excel" });
      saveAs(blob, fileName?.endsWith("xlsx") ? fileName : `${fileName}${Date.now()}.xlsx`);
      ElMessage.success("正在导出, 请稍后...");
    });
  },

  postExcel(path: string, data?: IObject, headers?: IObject, fileName?: string): any {
    http({
      url: path,
      data,
      headers,
      method: "POST",
      responseType: "blob"
    }).then((value: any) => {
      const blob = new Blob([value], { type: "application/vnd.ms-excel" });
      saveAs(blob, fileName?.endsWith("xlsx") ? fileName : `${fileName}${Date.now()}.xlsx`);
      ElMessage.success("正在导出, 请稍后...");
    });
  }
};
