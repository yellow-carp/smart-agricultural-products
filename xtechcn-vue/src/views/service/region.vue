<template>
  <div class="page-wrapper">
    <div class="header-wrapper">
      <div class="header-title">
        <div class="header-title__font">城市管理</div>
        <el-button type="primary" @click="addOrUpdateHandle()" :icon="Plus">新增</el-button>
      </div>
      <div class="header-search">
        <div class="header-search__input">
          <el-input v-model="state.dataForm.name" placeholder="请输入区域名称" clearable style="width: 200px;">
            <template #prefix>
              <div class="header-search__font">
                区域名称：
              </div>
            </template>
          </el-input>
          <dict-select v-model="state.dataForm.type" dictType="SERVICE_REGION_TYPE" placeholder="请选择区域类型" style="width: 200px;" />
        </div>
        <div class="header-search__button">
          <el-button type="success" @click="state.getDataList()" :icon="Search">搜索</el-button>
          <el-button type="warning" @click="state.getDataList()" :icon="RefreshRight">重置</el-button>
        </div>
      </div>
    </div>
    <div class="main-wrapper">
      <el-table :data="dataList" v-loading="state.dataListLoading" :stripe="true" height="100%" border>
        <el-table-column label="区域名称" prop="name" header-align="center" align="center"></el-table-column>
        <el-table-column label="区域类型" prop="type" header-align="center" align="center">
          <template v-slot="scope">
            <dict-show v-model="scope.row.type" type="SERVICE_REGION_TYPE" />
          </template>
        </el-table-column>
        <el-table-column label="物流模式" prop="mode" header-align="center" align="center">
          <template v-slot="scope">
            <dict-show v-model="scope.row.mode" type="SERVICE_LOGISTICS_MODE" />
          </template>
        </el-table-column>
        <el-table-column label="行政区划" prop="regionName" header-align="center" align="center"></el-table-column>
        <el-table-column label="区域状态" prop="state" header-align="center" align="center">
          <template v-slot="scope">
            <el-switch v-model="scope.row.state" active-text="启用" inactive-text="停用" />
          </template>
        </el-table-column>
        <el-table-column label="操作" prop="name" header-align="center" align="center">
          <template v-slot="scope">
            <el-button type="primary" plain @click="addOrUpdateHandle(scope.row)" :icon="Edit" size="small">编辑
            </el-button>
            <el-button type="danger" plain @click="state.deleteHandle(scope.row.id)" :icon="Delete" size="small">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="footer-wrapper">
      <el-pagination :current-page="state.current" :page-sizes="[10, 20, 50, 100]" :page-size="state.size"
                     :total="state.total" layout="total, sizes, prev, pager, next, jumper"
                     @size-change="state.pageSizeChangeHandle"
                     @current-change="state.pageCurrentChangeHandle" />
    </div>
    <!-- 弹窗, 新增 / 修改 -->
    <RegionForm ref="formRef" @refreshDataList="state.getDataList" />
  </div>
</template>

<script setup lang="ts">

import { onMounted, reactive, ref, toRefs } from "vue";
import { IViewHooksOptions } from "@/types/interface";
import useView from "@/hooks/useView";
import DictSelect from "@/components/dict-select/src/dict-select.vue";
import RegionForm from "@/views/service/region-form.vue";
import DictShow from "@/components/dict-show/src/dict-show.vue";
import { Plus, Search, Edit, Delete, RefreshRight } from "@element-plus/icons-vue";

const formRef = ref();

const view = reactive<IViewHooksOptions>({
  getDataListURL: "",
  getDataListIsPage: true,
  deleteURL: "",
  dataForm: {
    name: null,
    type: null
  },
  dataList: [],
  current: 1,
  size: 20,
  total: 0
});

const dataList = [
  {
    id: 1,
    name: "name1",
    type: "1",
    mode: "1",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: true
  },
  {
    id: 2,
    name: "name2",
    type: "2",
    mode: "1",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: false
  },
  {
    id: 3,
    name: "name3",
    type: "2",
    mode: "2",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: true
  },
  {
    id: 4,
    name: "name4",
    type: "1",
    mode: "1",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: true
  },
  {
    id: 5,
    name: "name5",
    type: "1",
    mode: "1",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: true
  },
  {
    id: 6,
    name: "name6",
    type: "2",
    mode: "2",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: true
  },
  {
    id: 7,
    name: "name7",
    type: "1",
    mode: "1",
    regionCode: "150000^150100^150104",
    regionName: "内蒙古自治区^呼和浩特市^玉泉区",
    state: true
  }
];

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateHandle = (id?: number) => {
  formRef.value.init(id);
};


onMounted(() => {

});

</script>

<style scoped lang="less">

.page-wrapper {
  display: flex;
  flex-direction: column;

  .header-wrapper {
    flex-shrink: 0;

    .header-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 15px;

      .header-title__font {
        font-weight: 700;
        font-size: 20px;
      }
    }

    .header-search {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 100px;
      padding: 10px 0;

      .header-search__input {
        display: flex;
        gap: 15px;
        flex-wrap: wrap;
        flex: 1;

        .header-search__font {
          color: #333333;
        }
      }

      .header-search__button {
        display: flex;
        gap: 15px;
        flex-shrink: 0;
        align-items: center;
      }
    }
  }

  .main-wrapper {
    flex: 1;
    min-height: 500px;
  }

  .footer-wrapper {
    flex-shrink: 0;
  }
}


</style>
