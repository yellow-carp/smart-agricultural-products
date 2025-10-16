<template>
  <div class="page-wrapper">
    <p class="breadcrumb-wrapper">
      当前位置：
      <span v-for="item in currentFolder.crumbs" :key="item.id" @click="crumbClickHandler(item)">{{ item.name }}</span>
    </p>
    <el-form :inline="true" :model="params" @keyup.enter="getList()">
      <div class="search-wrapper">
        <el-input v-model="params.name" placeholder="请输入文件名称" clearable style="width: 320px" @clear="getList" />
        <el-button type="success" @click="getList()">搜索</el-button>
        <el-button type="primary" icon="plus" @click="addFolderHandler">新增文件夹</el-button>
        <el-upload ref="upload" multiple :show-file-list="false" :http-request="uploadFiles" :on-error="errorHandler">
          <el-button type="primary" icon="upload"> 点击上传文件 </el-button>
        </el-upload>
      </div>
    </el-form>

    <div class="file-list-wrapper">
      <div class="file-item-wrapper" v-for="item in dataList" :key="item.id" @dblclick="dblclickHandler(item)" @contextmenu.prevent="rightClickHandler($event, item)">
        <img v-if="item.fileType === 1 && item.hasChild" src="@/views/system/image/folder-has.png" alt="" />
        <img v-if="item.fileType === 1 && !item.hasChild" src="@/views/system/image/folder-empty.png" alt="" />
        <img v-if="item.fileType === 2" :src="item.url" alt="" />
        <img v-if="item.fileType === 3" src="@/views/system/image/file-video.png" alt="" />
        <img v-if="item.fileType === 4" src="@/views/system/image/file-audio.png" alt="" />
        <img v-if="item.fileType === 5" src="@/views/system/image/file-docx.png" alt="" />
        <img v-if="item.fileType === 6" src="@/views/system/image/file-app.png" alt="" />
        <img v-if="item.fileType === 7" src="@/views/system/image/file-zip.png" alt="" />
        <img v-if="item.fileType === 99" src="@/views/system/image/file-other.png" alt="" />
        <NPerformantEllipsis v-if="!item.edit" :line-clamp="2" style="word-break: break-all">{{ item.name }}</NPerformantEllipsis>
        <input v-show="item.edit" class="rename-input" v-model="item.newName" @blur="renameHandler(item)" />
      </div>
    </div>

    <FileManagementAddFolder v-if="addFolderVisible" v-model="addFolderVisible" @refreshDataList="getList" />

    <div tabindex="-1" class="fixed-box" :style="fixedBoxStyleObject" v-show="isShowMenu" @blur="isShowMenu = false" ref="fixedBoxRef">
      <p @click="showRename(rightCurrentNode, rightCurrent)">重命名</p>
      <p @click="deleteFiledHandler(rightCurrent.id)">删除</p>
      <p @click="downloadHandler(rightCurrent)">下载</p>
    </div>

    <FilePreview v-if="previewVisible" v-model="previewVisible" :payload="previewData" />
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import FileManagementAddFolder from "@/views/system/file-management-add-folder.vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";
import { debounce } from "lodash";
import { NPerformantEllipsis } from "naive-ui";
import FilePreview from "@/views/system/file-preview.vue";

const dataList = ref<any[]>([]);
const currentFolder = ref<any>({});
const previewVisible = ref<boolean>(false);
const previewData = ref<any>({});
const crumbClickHandler = (item: any) => {
  if (item.id === currentFolder.value.id) return;
  if (item.id === currentFolder.value.crumbs[currentFolder.value.crumbs.length - 1].id) return;
  params.pid = item.id;
  getList();
};

const fixedBoxStyleObject = ref({
  left: "0px",
  top: "0px"
});

const isShowMenu = ref(false);
const fixedBoxRef = ref();

const rightCurrent = ref<any>({});
const rightCurrentNode = ref<any>({});
const rightClickHandler = (e: any, data: any) => {
  if (e.target.tagName === "DIV") {
    rightCurrentNode.value = e.target;
  } else {
    rightCurrentNode.value = e.target.parentElement;
  }

  e.preventDefault();
  fixedBoxStyleObject.value.left = e.clientX + "px";
  fixedBoxStyleObject.value.top = e.clientY + "px";
  isShowMenu.value = true;
  rightCurrent.value = data;
  setTimeout(() => {
    fixedBoxRef.value.focus();
  }, 1);
};

const dblclickHandler = (payload: any) => {
  switch (payload.fileType) {
    case 1:
      params.pid = payload.id;
      getList();
      break;
    case 2:
    case 3:
    case 4:
      previewVisible.value = true;
      previewData.value = payload;
      break;
    default:
      return;
  }
};

const downloadHandler = async (item: any) => {
  isShowMenu.value = false;
  if (item.fileType === 1) return ElMessage.error("文件夹不支持下载");
  const itemUrl = item.url; // 替换成你要下载的文件的URL
  const fileName = item.name; // 自定义文件名
  const x = new XMLHttpRequest(); //禁止浏览器缓存；否则会报跨域的错误
  //下载过程监听
  //异步执行
  x.open("GET", itemUrl, true);
  x.responseType = "blob";
  x.onload = function (e) {
    const url = window.URL.createObjectURL(x.response); //生成同源url
    const a = document.createElement("a");
    a.href = url;
    a.download = fileName;
    a.click();
  };
  x.send();
};

const params = reactive({
  pid: 0,
  name: ""
});

const getList = async () => {
  try {
    const { data } = await baseService.get("/cfs/search", params);
    dataList.value = data.children;
    currentFolder.value = data.current;
  } catch (error) {
    dataList.value = [];
    console.error(error);
  }
};

const addFolderVisible = ref<boolean>(false);

const fileList = ref<any>([]);

const errorHandler = () => {
  ElMessage.error("文件上传失败，请联系管理员！");
};

const submitHandler = async () => {
  try {
    await baseService.post("/cfs/touch/multiple", { pid: currentFolder.value.id ? currentFolder.value.id : 0, nodes: fileList.value });
    ElMessage.success("上传成功");
    getList();
  } catch (e) {
    console.error(e);
  }
};

const test = debounce(submitHandler, 1);

// 上传头像
const uploadFiles = async ({ file }: { file: any }) => {
  const formData = new FormData();
  formData.append("file", file);
  const { data } = await baseService.post("/file/upload", formData, { "Content-Type": "multipart/form-data" });
  fileList.value.push({ ...data, name: data.originalName });
  test();
};

/**
 * 编辑文件
 */
const showRename = (e: any, item: any) => {
  item.edit = true;
  item.newName = item.name;
  isShowMenu.value = false;
  setTimeout(() => {
    e.lastElementChild.focus();
  }, 100);
};

const renameHandler = async (item: any) => {
  if (item.newName === item.name) return (item.edit = false);
  try {
    await baseService.put(`/cfs/rename`, { name: item.newName, id: item.id });
    ElMessage.success("重命名成功");
    item.edit = false;
    getList();
  } catch (e) {
    console.error(e);
  }
};

/**
 * 新增文件夹
 */
const addFolderHandler = () => {
  addFolderVisible.value = true;
};

/**
 * 删除文件
 */
const deleteFiledHandler = async (id: number) => {
  try {
    ElMessageBox.confirm("是否进行[删除]操作", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
      .then(async () => {
        await baseService.delete(`/cfs/remove/${id}`);
        ElMessage.success("删除成功");
        isShowMenu.value = false;
        getList();
      })
      .catch((e: any) => {
        console.error(e);
      });
  } catch (e) {
    console.error(e);
  }
};

onMounted(() => {
  getList();
});
</script>

<style lang="less" scoped>
.breadcrumb-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #000;

  > span + span:before {
    content: "/";
    margin-right: 10px;
  }
  span {
    cursor: pointer;
  }

  span:last-child {
    color: #999;
    cursor: unset;
  }
}

.file-list-wrapper {
  margin-top: 10px;
  height: calc(100vh - 220px);
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  gap: 10px 45px;
  overflow-y: auto;

  .file-item-wrapper {
    width: 120px;
    height: 130px;
    padding: 10px;
    cursor: pointer;
    border-radius: 10px;
    margin-top: 5px;
    margin-left: 5px;
    gap: 5px;
    background-color: #fff;
    font-size: 12px;
    color: #000;
    text-align: center;

    &:hover {
      background-color: #f3f1f1;
      box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.3);
      transform: scale(1.05);
    }

    img {
      height: 80px;
      width: 80px;
    }
  }
}

.rename-input {
  border: 1px solid #ccc;
  border-radius: 3px;
  width: 90px;
  text-align: center;
  height: 22px;

  &:focus {
    outline: none;
  }
}

.search-wrapper {
  display: flex;
  gap: 10px;

  .el-button + .el-button {
    margin-left: 0;
  }
}

.fixed-box {
  position: fixed;
  color: black;
  padding: 8px;
  width: 100px;
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
  gap: 10px;
  border-radius: 5px;
  box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  top: 50%;
  left: 50%;
  font-size: 13px;
  line-height: 18px;

  > p {
    padding-left: 5px;
    &:hover {
      background: var(--el-color-primary);
      color: #fff;
      border-radius: 3px;
    }
  }

  &:focus {
    outline: none;
  }
}
.contextmenu {
  width: 300px;
  height: 300px;
  margin: 100px auto;
}
</style>
