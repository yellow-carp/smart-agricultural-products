<script setup lang="ts">
import { HomeFilled, Plus, Switch, UploadFilled, Warning } from "@element-plus/icons-vue";
import { computed, ref, toRef, toRefs } from "vue";
import { ElMessage } from "element-plus";
import baseService from "@/service/baseService";
import SelectMaterial from "@/views/material/components/selectMaterial.vue";
import draggable from "vuedraggable";

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  limit: {
    type: Number,
    default: 50
  },
  uploadSize: {
    type: Number,
    default: 10 * 1024 * 1024
  },
  totalSize: {
    type: Number,
    default: 10 * 1024 * 1024
  },
  showList: {
    type: Boolean,
    default: true
  }
});

const inputFileRef = ref<HTMLInputElement>();
const fileList = computed({
  get: () => props.modelValue,
  set: (value) => {
    emit("update:modelValue", value);
  }
});
const localHandler = () => {
  inputFileRef.value?.click();
};

const handleFileChanged = async (e: any) => {
  const files = e.target?.files as FileList;

  if (fileList.value.length + files.length > props.limit) {
    return ElMessage.error(`上传文件不能超过${props.limit}个`);
  }

  const totalSize = Array.from(files).reduce((total, file) => total + file.size, 0);
  if (totalSize > props.totalSize) {
    return ElMessage.error(`上传总文件大小不能超过${props.totalSize / 1024 / 1024}M`);
  }

  if (Array.from(files).some((file: any) => file.size > props.uploadSize)) {
    return ElMessage.error(`上传文件不能超过${props.uploadSize / 1024 / 1024}M`);
  }

  const form = new FormData();
  for (let i = 0; i < files.length; i++) {
    form.append("files", files[i]);
  }
  const { data } = await baseService.post("/file/batch/upload", form, { "Content-Type": "multipart/form-data" });

  fileList.value = [...fileList.value, ...data];
};

const visible = ref(false);

const showSelectMaterialHandler = () => {
  visible.value = true;
};

const previewVisible = ref(false);
const previewUrl = ref("");
const viewHandler = (item: any) => {
  previewUrl.value = item.url;
  previewVisible.value = true;
};

const draggableVisible = ref(false);
const drag = ref(false);

const emit = defineEmits(["update:modelValue"]);
const confirmHandler = (value: any) => {
  if (fileList.value.length + value.length > props.limit) {
    return ElMessage.error(`上传文件不能超过${props.limit}个`);
  }
  fileList.value = [...fileList.value, ...value];
};

const showFooter = ref(-1);

const showDraggableHandler = () => {
  draggableVisible.value = true;
};
</script>

<template>
  <div class="file-wrapper">
    <div v-for="(item, index) in fileList" :key="item.id" class="file-wrapper-item" @mouseenter="showFooter = index" @mouseleave="showFooter = -1">
      <img v-if="index <= 1" src="@/assets/images/goods-poster-tip.png" alt="" class="img-tips" />
      <el-image style="width: 100px; height: 100px" :src="item.url" fit="cover" :class="{ mark: showFooter === index }" />
      <div v-if="showFooter === index" class="file-wrapper-item-footer">
        <el-icon size="18px" color="#409eff" style="cursor: pointer" @click="viewHandler(item)"><View /></el-icon>
        <el-icon size="18px" color="#409eff" style="cursor: pointer" @click="showDraggableHandler"><Switch /></el-icon>
        <el-icon size="18px" color="#f56c6c" style="cursor: pointer" @click="fileList.splice(fileList.indexOf(item), 1)"><Delete /></el-icon>
      </div>
    </div>

    <el-popover popper-class="upload-popover" placement="top" :width="200">
      <div class="popover-body-wrapper">
        <p>
          <el-icon size="16px"><Warning /></el-icon>文件大小不超过10M
        </p>
        <div class="popover-btn-wrapper">
          <p @click="localHandler">
            <el-icon size="20px"><HomeFilled /></el-icon>本地
          </p>
          <p @click="showSelectMaterialHandler">
            <el-icon size="20px"><UploadFilled /></el-icon>媒体库
          </p>
        </div>
      </div>
      <template #reference>
        <div v-if="fileList.length < limit">
          <div class="upload-wrapper-btn">
            <el-icon size="22px" color="#409eff"><Plus /></el-icon>
          </div>
          <div v-if="showList">{{ fileList.length }}/{{ limit }}</div>
        </div>
      </template>
      <input class="upload-btn" ref="inputFileRef" type="file" name="file" id="file" :multiple="true" accept=".png,.jpg,.jpeg,.mp4,.mp3,.gif,.pdf" @change="handleFileChanged" />
    </el-popover>
  </div>

  <SelectMaterial v-if="visible" v-model="visible" @confirm="confirmHandler" />

  <el-dialog v-model="previewVisible" width="500px">
    <img :src="previewUrl" alt="" style="width: 100%" />
  </el-dialog>

  <el-dialog v-if="draggableVisible" v-model="draggableVisible" title="拖动图片排序">
    <draggable :list="fileList" animation="500" @start="drag = true" @end="drag = false" item-key="url" tag="ul">
      <template #item="{ element }">
        <el-image style="width: 100px; height: 100px; margin-right: 5px" :src="element.url" fit="cover" />
      </template>
    </draggable>
  </el-dialog>
</template>

<style lang="less">
.upload-popover.el-popper {
  padding: 0;
  height: 80px;
  overflow: hidden;
}
</style>

<style scoped lang="less">
.upload-wrapper-btn {
  width: 100px;
  height: 100px;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  background-color: #ecebeb;
  display: flex;
  align-items: center;
  justify-content: center;
}

.popover-body-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  > p {
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
    padding-top: 7px;
    color: var(--el-color-primary);

    > i {
      margin-top: -2px;
      margin-right: 3px;
    }
  }

  .popover-btn-wrapper {
    display: flex;
    border-top: 1px solid #eee;
    padding: 10px 10px;
    justify-content: space-between;

    > p {
      width: 80px;
      padding: 3px 5px;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1px solid var(--el-border-color);
      border-radius: 5px;
      cursor: pointer;

      > i {
        margin-top: -3px;
        margin-right: 3px;
      }
    }
  }
}

.file-wrapper {
  display: flex;
  gap: 10px;
  margin-right: 10px;
  height: 120px;

  .file-wrapper-item {
    position: relative;

    .el-image {
      border: 1px solid var(--el-border-color);
    }

    .img-tips {
      position: absolute;
      top: 0;
      right: 0;
      width: 50px;
      height: 50px;
      z-index: 1000;
    }

    &-footer {
      position: absolute;
      bottom: 20px;
      left: 0;
      width: 100%;
      background: rgba(255, 255, 255, 0.79);
      height: 20px;
      padding: 0 10px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      z-index: 1000;
    }
  }
}

.mark {
  &:after {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 100;
    background: rgba(229, 227, 230, 0.58);
  }
}
</style>
