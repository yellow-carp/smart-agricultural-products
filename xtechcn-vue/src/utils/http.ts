import app from "@/constants/app";
import { IHttpResponse, IObject } from "@/types/interface";
import router from "@/router";
import axios, { AxiosRequestConfig } from "axios";
import qs from "qs";
import { getToken } from "./cache";
import { getValueByKeys } from "./utils";
import { ElMessage } from "element-plus";
import i18n from "@/locales";
import { initSignature } from "@/utils/autograph";

const http = axios.create({
  baseURL: app.api,
  timeout: app.requestTimeout
});

http.interceptors.request.use(
  function (config: any) {
    const token = getToken();

    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    } else {
      config.headers["Authorization"] = "";
    }

    if (config.method?.toUpperCase() === "GET") {
      for (const key in config.params) {
        if (
          config.params[key] === null ||
          config.params[key] === undefined ||
          config.params[key] === ""
        ) {
          delete config.params[key];
        }
      }
      config.params = { ...config.params };
    }
    if (Object.values(config.headers).includes("application/x-www-form-urlencoded")) {
      config.data = qs.stringify(config.data);
      0;
    }

    if (config.headers.signature) return config;

    // 添加签名
    const signature = initSignature(config.url);
    if (signature) {
      config.headers = {
        ...config.headers,
        ...signature
      };
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);
http.interceptors.response.use(
  (response: any) => {
    if (response.request.responseType === "blob") {
      if (response.headers["error"]) {
        ElMessage.error(window.decodeURI(response.headers["error"]));
        throw new Error(window.decodeURI(response.headers["error"]));
      } else {
        return response;
      }
    }
    // 响应成功
    if (response.data.code === "0") {
      return response;
    }

    ElMessage.error(response.data.message);

    // 错误提示

    if (response.data.code === 401) {
      //自定义业务状态码
      redirectLogin();
    }

    return Promise.reject(new Error(response.data.message || "Error"));
  },
  (error) => {
    const status = getValueByKeys(error, "response.status", 500);
    const httpCodeLabel: IObject<string> = {
      400: i18n.global.t("request.status_400"),
      401: i18n.global.t("request.status_401"),
      403: i18n.global.t("request.status_403"),
      404: `${i18n.global.t("request.status_404")}: ${getValueByKeys(
        error,
        "response.config.url",
        ""
      )}`,
      408: i18n.global.t("request.status_408"),
      500: i18n.global.t("request.status_500"),
      501: i18n.global.t("request.status_501"),
      502: i18n.global.t("request.status_502"),
      503: i18n.global.t("request.status_503"),
      504: i18n.global.t("request.status_504"),
      505: i18n.global.t("request.status_505")
    };
    if (error && error.response) {
      console.error(i18n.global.t("request.request_error"), error.response.data);
    }

    error.response.data.message && ElMessage.error(error.response.data.message);

    if (status === 426) {
      return redirectLogin();
    }
    return Promise.reject(
      new Error(httpCodeLabel[status] || i18n.global.t("request.interface_error"))
    );
  }
);

const redirectLogin = () => {
  ElMessage.error("请求失效，请重新登录");
  router.replace("/login");
  return;
};

export default (o: AxiosRequestConfig): Promise<IHttpResponse> => {
  return new Promise((resolve, reject) => {
    http(o)
      .then((res) => {
        return resolve(res.data);
      })
      .catch(reject);
  });
};
