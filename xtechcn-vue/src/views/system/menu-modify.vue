<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle" label-width="120px">
      <el-form-item prop="menuType" label="类型">
        <el-radio-group v-model="dataForm.type" :disabled="!!dataForm.id">
          <el-radio :value="1">菜单</el-radio>
          <el-radio :value="0">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="name" label="名称">
        <el-input v-model="dataForm.name" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item prop="parentName" label="上级菜单" class="menu-list">
        <el-popover ref="menuListPopover" placement="bottom-start" trigger="click" :width="400" popper-class="popover-pop">
          <template v-slot:reference>
            <el-input v-model="dataForm.parentName" :readonly="true" placeholder="上级菜单">
              <template v-slot:suffix>
                <el-icon v-if="dataForm.pid !== '0'" @click.stop="deptListTreeSetDefaultHandle()" class="el-input__icon"><circle-close /></el-icon>
              </template>
            </el-input>
          </template>
          <div class="popover-pop-body">
            <el-tree :data="menuList" :props="{ label: 'name', children: 'children' }" node-key="id" ref="menuListTree" :highlight-current="true" :expand-on-click-node="false" accordion @current-change="menuListTreeCurrentChangeHandle"> </el-tree>
          </div>
        </el-popover>
      </el-form-item>
      <el-form-item v-if="dataForm.type === 1" prop="path" label="路由">
        <el-input v-model="dataForm.path" placeholder="路由" />
      </el-form-item>
      <el-form-item prop="sort" label="排序">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item v-if="dataForm.type === 1" prop="isHidden" label="是否隐藏">
        <el-radio-group v-model="dataForm.isHidden">
          <el-radio :value="1">是</el-radio>
          <el-radio :value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="permission" label="授权标识">
        <el-input v-model="dataForm.permission" placeholder="多个用逗号分隔，如：sys:menu:save,sys:menu:update"></el-input>
      </el-form-item>
      <el-form-item v-if="dataForm.type === 1" prop="icon" label="图标" class="icon-list">
        <el-popover ref="iconListPopover" placement="top-start" trigger="click" popper-class="popover-pop mod-sys__menu-icon-popover">
          <template v-slot:reference> <el-input v-model="dataForm.icon" :readonly="true" placeholder="图标"></el-input></template>
          <div class="mod-sys__menu-icon-inner">
            <div class="mod-sys__menu-icon-list">
              <el-button v-for="(item, index) in iconList" :key="index" @click="iconListCurrentChangeHandle(item)" :class="{ 'is-active': dataForm.icon === item }">
                <svg class="icon-svg" aria-hidden="true"><use :xlink:href="`#${item}`"></use></svg>
              </el-button>
            </div>
          </div>
        </el-popover>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { getIconList } from "@/utils/utils";
import { IObject } from "@/types/interface";
import { ElMessage } from "element-plus";

const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const menuList = ref([]);
const iconList = ref<string[]>([]);
const dataFormRef = ref();
const menuListTree = ref();
const menuListPopover = ref();
const iconListPopover = ref();

const initDataForm = () => ({
  id: "",
  type: 1,
  name: "",
  pid: "0",
  parentName: "",
  path: "",
  permission: "",
  sort: 0,
  icon: "",
  isHidden: 0
});

const dataForm = reactive(initDataForm());

const rules = ref({
  name: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  parentName: [{ required: true, message: "必填项不能为空", trigger: "change" }],
  path: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

const init = (payload: IObject) => {
  visible.value = true;
  dataForm.id = "";

  // 重置表单数据
  dataFormRef.value?.resetFields();
  Object.assign(dataForm, initDataForm());
  iconList.value = getIconList();

  dataForm.pid = "0";
  dataForm.parentName = "一级菜单";

  Object.assign(dataForm, payload);

  getMenuList().then(() => {
    if (dataForm.pid) {
      menuListTree.value.setCurrentKey(dataForm.pid);
    }
  });
};

const init2 = (row: IObject) => {
  visible.value = true;

  Object.assign(dataForm, initDataForm());
  // 重置表单数据
  dataFormRef.value?.resetFields();
  iconList.value = getIconList();

  dataForm.id = "";
  dataForm.pid = row.id;
  dataForm.parentName = row.name;

  getMenuList();
};

// 获取菜单列表
const getMenuList = () => {
  return baseService.get("/menu/tree?type=0").then((res) => {
    menuList.value = res.data;
  });
};

// 上级菜单树, 设置默认值
const deptListTreeSetDefaultHandle = () => {
  dataForm.pid = "0";
  dataForm.parentName = "一级菜单";
};

// 上级菜单树, 选中
const menuListTreeCurrentChangeHandle = (data: IObject) => {
  dataForm.pid = data.id;
  dataForm.parentName = data.name;
  menuListPopover.value.hide();
};

// 图标, 选中
const iconListCurrentChangeHandle = (icon: string) => {
  dataForm.icon = icon;
  iconListPopover.value.hide();
};

// 表单提交
const dataFormSubmitHandle = async () => {
  await dataFormRef.value.validate();
  (!dataForm.id ? baseService.post : baseService.put)(!dataForm.id ? "/menu/add" : "/menu/update", dataForm).then(() => {
    ElMessage.success("成功");
    visible.value = false;
    emit("refreshDataList");
  });
};

defineExpose({
  init,
  init2
});
</script>

<style lang="less">
.el-popover.el-popper {
  overflow-x: hidden;
}
.mod-sys__menu {
  .menu-list,
  .icon-list {
    .el-input__inner,
    .el-input__suffix {
      cursor: pointer;
    }
  }
  &-icon-popover {
    width: 458px !important;
    overflow-y: hidden !important;
  }
  &-icon-inner {
    width: 100%;
    max-height: 260px;
    overflow-x: hidden;
    overflow-y: auto;
  }
  &-icon-list {
    width: 458px !important;
    padding: 0;
    margin: -8px 0 0 -8px;
    > .el-button {
      padding: 8px;
      margin: 8px 0 0 8px;
      > span {
        display: inline-block;
        vertical-align: middle;
        width: 18px;
        height: 18px;
        font-size: 18px;
      }
    }
  }
}
</style>
