<script setup lang="ts">
import baseService from "@/service/baseService";
import { onMounted, reactive, ref } from "vue";
import Modify from "@/views/material/components/modify.vue";
import { ElMessageBox, ElMessage } from "element-plus";
import { Delete, Plus } from "@element-plus/icons-vue";
import { NEllipsis } from "naive-ui";
import { ossUpload } from "@/utils/aliOss";

const props = defineProps({
  height: {
    type: String,
    default: "100%"
  },
  showSelected: {
    type: Boolean,
    default: false
  }
});

const emits = defineEmits(["confirm"]);

const groupList = ref<any[]>([]);
const visible = ref(false);
const payload = ref({});

const activeValue = ref<any>({});

const addHandler = () => {
  payload.value = {};
  visible.value = true;
};

const editHandler = (val: any) => {
  payload.value = val;
  visible.value = true;
};

const deleteHandler = (val: string) => {
  ElMessageBox.confirm("确定删除?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    baseService.delete("/material/group/remove/" + val, {}).then(() => {
      ElMessage.success("删除成功");
      getGroupList();

      if (activeValue.value && activeValue.value.id === val) {
        activeValue.value = groupList.value[0];
        getMaterialList();
      }
    });
  });
};

const getGroupList = async () => {
  const { data } = await baseService.get("/material/group/list");
  groupList.value = data;
};

const selectedGroupHandler = (val: any) => {
  activeValue.value = val;
  getMaterialList();
};

const viewList = ref<any[]>([]);
const total = ref(0);

const getMaterialList = async () => {
  const { data } = await baseService.get("/material/page", { groupId: activeValue.value.id, current: page.current, size: page.size });
  viewList.value = data.records;
  total.value = data.total;
};

const loading = ref(false);

const uploadHandler = async (event: any) => {
  const files = event.target?.files as FileList;
  if (!files.length) return;

  if (files.length > 1000) {
    return ElMessage.error("一次最多上传1000个文件");
  }

  // const totalSize = Array.from(files).reduce((total, file) => total + file.size, 0);
  // if (totalSize > 100 * 1024 * 1024) {
  //   return ElMessage.error(`上传总文件大小不能超过100M`);
  // }

  try {
    loading.value = true;
    // const formData = new FormData();
    // for (let i = 0; i < files.length; i++) {
    //   formData.append("files", files[i]);
    // }
    // const { data } = await baseService.post("/file/batch/upload", formData, { "Content-Type": "multipart/form-data" });

    for (let i = 0; i < files.length; i++) {
      const data = await ossUpload(files[i]);

      const obj = {
        groupId: activeValue.value.id,
        materials: [data]
      };
      await baseService.post("/material/batch/add", obj);

      if (Number.isInteger(i / 10)) {
        getMaterialList();
      }
    }

    ElMessage.success("上传成功");
    loading.value = false;
    inputFileKey.value = new Date().getTime();
    getMaterialList();
  } catch (e) {
    inputFileKey.value = new Date().getTime();
    loading.value = false;
    console.log(e);
  }
};

const downloadHandler = async () => {
  await baseService.getExcel("/material/group/excel/" + activeValue.value.id, {}, {}, activeValue.value.name + "-材料导出模版.xlsx");
};

const copyHandler = (value: any) => {
  navigator.clipboard.writeText(value.url);
  ElMessage.success("复制url成功");
};

const deleteMaterialHandler = async (val: string) => {
  ElMessageBox.confirm("确定删除?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    baseService.delete("/material/remove/" + val, {}).then(() => {
      ElMessage.success("删除成功");

      getMaterialList();
    });
  });
};

const previewVisible = ref(false);
const previewUrl = ref("");

const page = reactive({
  current: 1,
  size: 20
});

const previewHandler = async (value: any) => {
  previewUrl.value = value.url;
  previewVisible.value = true;
};

const modifyHandler = (item: any) => {
  if (props.showSelected) {
    if (!selectedList.value.find((i: any) => i.id === item.id)) {
      selectedList.value.push(item);
    }
    return;
  }

  ElMessageBox.prompt("请输入图片名称", "名称修改", {
    confirmButtonText: "确定",
    cancelButtonText: "取消"
  }).then(({ value }) => {
    if (!value) return ElMessage.error("请输入图片名称");
    baseService.put("/material/update", { id: item.id, name: value }).then(() => {
      ElMessage.success("图片名称修改成功");
      getMaterialList();
    });
  });
};

const selectedList = ref<any[]>([]);

const deleteSelectedHandler = (val: string) => {
  selectedList.value = selectedList.value.filter((item: any) => item.id !== val);
};

const confirmHandler = () => {
  emits("confirm", selectedList.value);
};

const inputFileRef_1 = ref<HTMLInputElement>();
const inputFileKey = ref(new Date().getTime());

const showUploadInput = () => {
  inputFileRef_1.value?.focus();
};

onMounted(async () => {
  await getGroupList();

  activeValue.value = groupList.value[0];

  getMaterialList();
});
</script>

<template>
  <div class="main-wrapper" :style="{ height: height }">
    <div class="first-card">
      <div class="header-wrapper"><span>素材组</span> <el-button text type="primary" icon="Plus" style="padding: 0" @click="addHandler">新增</el-button></div>
      <div class="group-wrapper">
        <div class="group-wrapper-item" v-for="item in groupList" :key="item.id" :class="{ active: item.id === activeValue.id }" @click="selectedGroupHandler(item)">
          <span style="font-size: 14px">{{ item.name }}</span>
          <span>
            <el-text text type="primary" size="small" @click="editHandler(item)">编辑</el-text>
            <el-text text type="danger" style="margin-left: 10px" size="small" @click="deleteHandler(item.id)">删除</el-text>
          </span>
        </div>
      </div>
    </div>
    <div class="second-card">
      <div class="header-wrapper">
        <span>{{ activeValue.name }}</span>
        <div class="search-wrapper">
          <el-input placeholder="请输入名称" clearable size="small" prefix-icon="Search" />
          <el-button text type="primary" icon="Download" @click="downloadHandler">下载网络地址表</el-button>
        </div>
      </div>
      <div class="material-wrapper" :style="{ height: showSelected ? 'calc(100vh - 320px)' : 'calc(100vh - 250px)' }" v-loading="loading">
        <div class="upload-wrapper" style="cursor: pointer" @click="showUploadInput">
          <el-icon size="30px" color="#409eff"><Plus /> </el-icon>
          <input :key="inputFileKey" class="upload-btn" ref="inputFileRef_1" title="" type="file" name="file_1" id="file_1" :multiple="true" accept=".png,.jpg,.jpeg,.gif,.pdf" @change="uploadHandler" />
        </div>
        <div v-for="item in viewList" :key="item.id" class="material-wrapper-item" @click="modifyHandler(item)">
          <NEllipsis style="max-width: 145px">
            {{ item.name }}
          </NEllipsis>
          <el-image style="width: 100px; height: 100px" :src="item.url" fit="cover" />
          <div class="material-wrapper-item-footer">
            <el-icon size="18px" color="#409eff" style="cursor: pointer" @click.stop="previewHandler(item)"><View /></el-icon>
            <el-text text type="primary" style="cursor: pointer" size="small" @click.stop="copyHandler(item)">复制URL</el-text>
            <el-icon size="18px" color="#f56c6c" style="cursor: pointer" @click.stop="deleteMaterialHandler(item.id)"><Delete /></el-icon>
          </div>
        </div>
      </div>
      <el-pagination layout="total, sizes, prev, pager, next" v-model:current-page="page.current" :page-sizes="[20, 50, 100, 200]" v-model:page-size="page.size" @size-change="getMaterialList" @current-change="getMaterialList" :total="total" />
    </div>

    <div v-if="showSelected" class="third-card">
      <div class="header-wrapper">
        <span>已选择</span>
        <div class="search-wrapper">
          <el-text text type="warning" style="cursor: pointer" @click="selectedList = []">清空选择</el-text>
          <el-text text type="primary" style="cursor: pointer" @click="confirmHandler">确定</el-text>
        </div>
      </div>
      <div class="material-wrapper">
        <div v-for="item in selectedList" :key="item.id" class="material-wrapper-item selected-item" @click="modifyHandler(item)">
          <NEllipsis style="max-width: 145px">
            {{ item.name }}
          </NEllipsis>
          <el-image style="width: 100px; height: 100px" :src="item.url" fit="cover" />
          <div class="material-wrapper-item-footer">
            <el-icon size="18px" color="#409eff" style="cursor: pointer" @click.stop="previewHandler(item)"><View /></el-icon>
            <el-icon size="18px" color="#f56c6c" style="cursor: pointer" @click.stop="deleteSelectedHandler(item.id)"><Delete /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!--  新增/编辑 素材组  -->
    <Modify v-if="visible" v-model="visible" :payload="payload" @refreshData="getGroupList" />

    <el-dialog v-model="previewVisible" width="500px">
      <img :src="previewUrl" alt="" style="width: 100%" />
    </el-dialog>
  </div>
</template>

<style scoped lang="less">
.main-wrapper {
  display: flex;
  gap: 20px;

  > div {
    background: #fff;
    border-radius: 10px;
    border: 1px solid #dcdfe6;
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.12);
  }

  .first-card {
    flex: 1;
  }

  .second-card {
    flex: 3;
  }

  .third-card {
    flex: 2;
  }

  .header-wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid #dcdfe6;
    margin-bottom: 10px;
    padding: 10px 10px;
  }

  .search-wrapper {
    display: flex;
    gap: 20px;

    .el-input {
      width: 200px;
    }
  }

  .group-wrapper {
    display: flex;
    gap: 10px;
    flex-direction: column;

    &-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 5px;
      cursor: pointer;

      &:hover,
      &.active {
        background: linear-gradient(45deg, #f4f4f1, #9ad1f3);
        box-shadow: 0 0 0 rgba(0, 0, 0, 0.039) inset, 0 0 0 rgba(0, 0, 0, 0.057) inset, 10px 9px 12px rgba(0, 0, 0, 0.07) inset;
      }
    }
  }

  .third-card .material-wrapper {
    height: calc(100% - 65px);
  }

  .material-wrapper {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    justify-content: start;
    align-content: start;
    overflow-y: scroll;
    padding: 0 5px;

    > div {
      width: calc((100% - 40px) / 5);
      padding: 10px;
      border: 1px dashed #ccc;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0;
    }
  }

  .selected-item {
    width: calc((100% - 20px) / 3) !important;

    .material-wrapper-item-footer {
      margin-bottom: 10px;
    }
  }

  .material-wrapper-item {
    display: flex;
    flex-direction: column;
    gap: 10px;

    &-footer {
      display: flex;
      align-items: center;
      gap: 10px;
    }
  }

  .upload-wrapper {
    position: relative;
    width: 100px;
    min-height: 180px;
  }

  .upload-btn {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
  }
}
</style>
