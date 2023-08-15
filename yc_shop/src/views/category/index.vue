<template>
  <div class="app-container">
    <el-table
      @selection-change="handleSelectionChange"
      ref="multipleTableRef"
      :data="categoryList"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="分类编号" width="120">
        <template #default="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column property="name" label="分类" width="120" />
      <el-table-column
        property="createTime"
        label="创建时间"
        show-overflow-tooltip
      />
      <el-table-column
        property="updateTime"
        label="更新时间"
        show-overflow-tooltip
      />
    </el-table>
    >
  </div>
</template>

<script>
import { getCategoryList } from "@/api/category";
export default {
  data() {
    return {
      categoryList: [],
      selectedItems: [],
    };
  },
  methods: {
    // 刷新列表
    query() {
      getCategoryList().then((res) => {
        if (res.code === 200) {
          this.categoryList = res.data;
        }
      });
    },
    // 获取选中的数据
    handleSelectionChange(selection) {
      this.selectedItems = selection;
      if (this.selectedItems.length > 0) {
        this.selectedItems.forEach((item) => {
          console.log("id:" + item.id + "  name:" + item.name);
        });
      }
    },
  },
  created() {
    this.query();
  },
};
</script>

<style scoped>
.line {
  text-align: center;
}
</style>
