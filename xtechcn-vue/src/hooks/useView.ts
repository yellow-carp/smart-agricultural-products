import app from "@/constants/app";
import { EMitt, EThemeSetting } from "@/constants/enum";
import { IObject, IViewHooks, IViewHooksOptions } from "@/types/interface";
import baseService from "@/service/baseService";
import { getToken } from "@/utils/cache";
import emits from "@/utils/emits";
import { getThemeConfigCacheByKey } from "@/utils/theme";
import { checkPermission, getDictLabel } from "@/utils/utils";
import qs from "qs";
import { onActivated, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAppStore } from "@/store";
import { ElMessage, ElMessageBox } from "element-plus";

/**
 * 通用视图业务逻辑（列表/增删改查基本业务）
 * @param props 自定义通用业务state
 * @returns 返回响应式自定义state和通用方法
 */
const useView = (props: IViewHooksOptions | IObject): IViewHooks => {
  const router = useRouter();
  const store = useAppStore();
  const defaultOptions: IViewHooksOptions = {
    createdIsNeed: true,
    activatedIsNeed: true,
    getDataListURL: "",
    getDataListIsPage: false,
    deleteURL: "",
    deleteIsBatch: false,
    deleteIsBatchKey: "id",
    exportURL: "",
    dataForm: {},
    dataList: [],
    order: "",
    orderField: "",
    current: 1,
    size: 20,
    total: 0,
    dataListLoading: false,
    dataListSelections: [],
    elTable: {}
  };
  const mergeDefaultStateToPageState = (options: IObject, props: IObject): IViewHooksOptions => {
    for (const key in options) {
      if (!Object.getOwnPropertyDescriptor(props, key)) {
        props[key] = options[key];
      }
    }
    return props;
  };
  const state = mergeDefaultStateToPageState(defaultOptions, props);
  onMounted(() => {
    if (state.createdIsNeed && !state.activatedIsNeed) {
      viewFns.query();
    }
  });
  onActivated(() => {
    if (store.state.closedTabs.includes(store.state.activeTabName)) {
      //如果当前打开的tab页面是之前已经关闭过的会存在keep-alive缓存
      //这里采用临时刷新页面解决方案
      //待vue官方开放缓存策略后再行实现 https://github.com/vuejs/vue-next/pull/4339   https://github.com/vuejs/rfcs/pull/284

      const closedTabs = store.state.closedTabs;
      store.updateState({
        closedTabs: closedTabs.filter((x: string) => x !== store.state.activeTabName)
      });
      emits.emit(EMitt.OnReloadTabPage);
    }

    if (state.activatedIsNeed) {
      viewFns.query();
    }
  });

  //
  const rejectFns = {
    hasPermission(key: string) {
      return checkPermission(store.state.permissions as string[], key);
    },
    getDictLabel(dictType: string, dictValue: number) {
      return getDictLabel(store.state.dicts, dictType, dictValue);
    }
  };

  //
  const viewFns = {
    // 获取数据列表
    query() {
      if (!state.getDataListURL) {
        return;
      }
      state.dataListLoading = true;
      baseService
        .get(state.getDataListURL, {
          current: state.getDataListIsPage ? state.current : null,
          size: state.getDataListIsPage ? state.size : null,
          ...state.dataForm
        })
        .then((res) => {
          state.dataListLoading = false;
          state.dataList = state.getDataListIsPage ? res.data.records : res.data;
          state.total = state.getDataListIsPage ? res.data.total : 0;
        })
        .catch(() => {
          state.dataList = [];
          state.dataListLoading = false;
        });
    },
    // 多选
    dataListSelectionChangeHandle(val: IObject[]) {
      state.dataListSelections = val;
    },
    // 排序
    dataListSortChangeHandle(data: IObject) {
      if (!data.order || !data.prop) {
        state.order = "";
        state.orderField = "";
        return false;
      }
      state.order = data.order.replace(/ending$/, "");
      state.orderField = data.prop.replace(/([A-Z])/g, "_$1").toLowerCase();
      viewFns.query();
    },
    // 分页, 每页条数
    pageSizeChangeHandle(val: number) {
      state.current = 1;
      state.size = val;
      viewFns.query();
    },
    // 分页, 当前页
    pageCurrentChangeHandle(val: number) {
      state.current = val;
      viewFns.query();
    },
    //搜索
    getDataList() {
      state.current = 1;
      viewFns.query();
    },
    // 保持页码
    keepPage() {
      viewFns.query();
    },
    // 删除
    deleteHandle(id?: string): Promise<any> {
      return new Promise((resolve) => {
        if (
          state.deleteIsBatch &&
          !id &&
          state.dataListSelections &&
          state.dataListSelections.length <= 0
        ) {
          ElMessage.warning({
            message: "请选择操作项",
            duration: 500
          });
          return;
        }
        ElMessageBox.confirm("确定进行[删除]操作?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            baseService
              .delete(
                `${state.deleteURL}${state.deleteIsBatch ? "" : "/" + id}`,
                state.deleteIsBatch
                  ? id
                    ? [id]
                    : state.dataListSelections
                    ? state.dataListSelections.map(
                        (item: IObject) => state.deleteIsBatchKey && item[state.deleteIsBatchKey]
                      )
                    : {}
                  : {}
              )
              .then(() => {
                ElMessage.success({
                  message: "成功",
                  duration: 500,
                  onClose: () => {
                    viewFns.query();
                    resolve(true);
                  }
                });
              });
          })
          .catch(() => {
            //
          });
      });
    },
    // 导出
    exportHandle() {
      window.location.href = `${app.api}${state.exportURL}?${qs.stringify({
        ...state.dataForm,
        token: getToken()
      })}`;
      // baseService.download(state.exportURL, { ...state.dataForm, token: getToken() });
    },
    //关闭当前窗口
    closeCurrentTab() {
      if (getThemeConfigCacheByKey(EThemeSetting.OpenTabsPage)) {
        emits.emit(EMitt.OnCloseCurrTab);
      } else {
        router.replace("/home");
      }
    }
  };

  return {
    ...viewFns,
    ...rejectFns
  };
};

export default useView;
