<script setup lang="ts">
import { computed, onMounted, ref, h } from "vue";
import { NCheckbox, NDataTable } from "naive-ui";
import type { DataTableColumns } from "naive-ui";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

interface RowData {
  name: string;
  id: string;
  children?: RowData[];
}

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  payload: {
    type: Object,
    default: () => ({})
  }
});

const emits = defineEmits(["update:modelValue"]);

const show = computed({
  get() {
    return props.modelValue;
  },
  set(val: boolean) {
    emits("update:modelValue", val);
  }
});

const treeData = ref<any[]>([]);
const checkedRowKeys = ref<any>([]);
const expandedRowKeys = ref<any>([]);
let cacheFlotList: any[] = []; // 缓存 flot后的数据。 方便勾选上级

// 按钮点击
const handlePermitBtnCheck = (checked: boolean, item: any, row: RowData) => {
  item.checked = checked;
  if (!checkedRowKeys.value.includes(row.id)) checkedRowKeys.value.push(row.id) && handleCheck([], [], { row, action: checked ? "check" : "uncheck", button: item.id });
};

const columns: DataTableColumns<RowData> = [
  {
    type: "selection",
    width: 55,
    disabled: (data: any) => data.disabled
  },
  {
    key: "name",
    title: "菜单权限",
    width: 350
  },
  {
    key: "buttons",
    title: "按钮权限",
    render(row: any) {
      return !row.buttons?.length
        ? ""
        : row.buttons.map((item: any) => {
            return h(NCheckbox, { checked: item.checked, disabled: item.disabled, onUpdateChecked: (checked) => handlePermitBtnCheck(checked, item, row) }, () => item.name);
          });
    }
  }
];

// 拆分data，分 button list
const loopList = (list: any[], result: any[] = []) => {
  list.forEach((item) => {
    if (item.children && item.children.length > 0 && item.children.every((x: any) => x.type !== 1)) {
      item.buttons = item.children;
      delete item.children;
      result.push(item);
    }
    if (item.children && item.children.length > 0) {
      loopList(item.children);
    }
  });
  return list;
};

// 获取数据
const getTreeData = async () => {
  const { data = [] } = await baseService.get("/authority/role/menus/checked", {
    roleId: props.payload.id
  });
  treeData.value = loopList(data);
  cacheFlotList = flotList(data);
};

// flot 数组
const flotList = (list: any[], result: any[] = []) => {
  list.forEach((item: any) => {
    result.push(item);
    if (item.children && item.children.length > 0) {
      return flotList(item.children, result);
    }
  });
  return result;
};

// 列表 设置回显
const loopChecked = (list: any[], result: any[], expand?: any[], type?: boolean) => {
  list.forEach((item) => {
    if (item.checked) {
      result.push(item.id);
    }
    if (item.children && item.children.length > 0) {
      if (type) expand?.push(item.id);
      return loopChecked(item.children, result, expand, type);
    }
  });
  return type ? expand : result;
};

/**
 * 获取选中的 node 用来拿取选中 button
 * @param list 列表
 * @param keys 列表选中值
 * @param result
 */
const loopCheckedNode = (list: any[], keys: any[], result: any[] = []) => {
  list.forEach((item) => {
    if (keys.includes(item.id) && item.buttons && item.buttons.length > 0) {
      item.buttons.forEach((sub: any) => {
        if (sub.checked) result.push(sub.id);
      });
    }
    if (item.children && item.children.length > 0) {
      return loopCheckedNode(item.children, keys, result);
    }
  });
  return result;
};

// 由该节点 向上勾选 父节点
const itemToSelect = (row: any) => {
  if (!row) return;
  if (row.pid === 0) return;
  if (checkedRowKeys.value.includes(row.pid)) return;
  checkedRowKeys.value.push(row.pid);
  itemToSelect(cacheFlotList.find((item: any) => item.id === row.pid));
};

// 设置 选中
const handleCheck = (rowKeys: any, rows: any, { row, action, button }: any) => {
  if (action === "check") itemToSelect(row);
  // 设置选中按钮
  switch (action) {
    case "check":
      unCheckAll([row], true, button);
      break;
    case "checkAll":
      unCheckAll(treeData.value, true, button);
      break;
    case "uncheckAll":
      unCheckAll(treeData.value, false, button);
      break;
    case "uncheck":
      unCheckAll([row], false, button);
      break;
    default:
      return;
  }
};

/**
 * @param list 过滤数组
 * @param flag 是否选中
 * @param button 选中按钮
 */
const unCheckAll = (list: any, flag: boolean, button: any) => {
  list.forEach((item: any) => {
    if (flag) {
      checkedRowKeys.value.push(item.id);
    } else {
      checkedRowKeys.value = checkedRowKeys.value.filter((x: any) => x !== item.id);
    }
    if (item.buttons && item.buttons.length > 0) {
      item.buttons.forEach((sub: any) => {
        if (button && button === sub.id) {
          sub.checked = flag;
        }

        if (!button) {
          sub.checked = flag;
        }
      });
    }
    if (item.children && item.children.length > 0) {
      unCheckAll(item.children, flag, button);
    }
  });
};

// 设置展开项目 - 展开报错
const handleExpand = (rowKeys: any) => {
  expandedRowKeys.value = rowKeys;
};

const confirmHandler = async () => {
  try {
    const buttons = loopCheckedNode(treeData.value, checkedRowKeys.value, []);
    await baseService.post("/role/permit/grant", {
      roleId: props.payload.id,
      checkMenuIds: [...checkedRowKeys.value, ...buttons]
    });
    ElMessage.success("授权成功");
    show.value = false;
  } catch (e) {
    console.error(e);
  }
};

onMounted(async () => {
  await getTreeData();

  checkedRowKeys.value = loopChecked(treeData.value, []);
  expandedRowKeys.value = loopChecked(treeData.value, [], [], true);
});
</script>

<template>
  <el-dialog v-model="show" :title="`角色授权 - ${payload.name}`" width="50vw" :close-on-click-modal="false" :close-on-press-escape="false">
    <n-data-table max-height="520px" :cascade="false" v-model:checked-row-keys="checkedRowKeys" @update:checked-row-keys="handleCheck" :columns="columns" :data="treeData" :expanded-row-keys="expandedRowKeys" @update:expanded-row-keys="handleExpand" :row-key="(row) => row.id" border />
    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="confirmHandler">确定</el-button>
    </template>
  </el-dialog>
</template>
