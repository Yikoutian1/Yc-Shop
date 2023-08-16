<template>
  <div class="app-container">
    <div>
      <el-input
        v-model="input"
        placeholder="请输入分类名称"
        style="width: 250px"
        @keyup.native.enter="handleQuery"
      >
      </el-input>
      <el-button @click="handleQuery" class="el-button-search">搜索</el-button>
      <span class="span-btn delBut non" @click="deleteHandle('批量',null)"
        >批量删除</span
      >
      <span class="span-btn blueBug non" @click="statusHandle('1')"
        >批量启售</span
      >
      <span
        class="span-btn delBut non"
        style="border: none"
        @click="statusHandle('0')"
        >批量停售</span
      >
      <el-button
        class="el-button-primary"
        type="primary"
        @click="addCategory('add')"
      >
        + 新建套餐
      </el-button>
    </div>
    <el-table
      @selection-change="handleSelectionChange"
      ref="multipleTableRef"
      :data="categoryList"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="编号" width="55">
        <template #default="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column property="name" label="分类名称" width="120" />
      <el-table-column property="sort" label="排序" width="60" />
      <el-table-column label="状态" width="70">
        <template #default="scope">
          <span style="margin-right: 10px">{{
            scope.row.status == "0" ? "停售" : "启售"
          }}</span>
        </template>
      </el-table-column>
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
      <el-table-column label="操作" width="160" align="center">
        <!-- 修改信息 -->
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            class="blueBug"
            @click="updateInfo(scope.row)"
          >
            修改
          </el-button>
          <!-- 改状态 -->
          <el-button
            type="text"
            size="small"
            class="blueBug"
            @click="statusHandle(scope.row)"
          >
            {{ scope.row.status == "0" ? "启售" : "停售" }}
          </el-button>
          <!-- 删除 -->
          <el-button
            type="text"
            size="small"
            class="delBut"
            @click="deleteHandle('单删', scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div id="pages">
      <el-pagination
        @size-change="sizeChangeQuery"
        @current-change="currentQuery"
        :current-page="1"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="10"
        class="pages-class"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
    <el-dialog
      title="分类信息"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form ref="diaForm" :model="diaForm" class="dia-form">
        <!-- <el-form-item label="分类名称">
          <span>{{ diaForm.name }}</span>
        </el-form-item>
        <el-form-item label="排序">
          <span>{{ diaForm.sort }}</span>
        </el-form-item>
        <el-form-item label="状态">
          <span>{{ diaForm.status }}</span>
        </el-form-item> -->
        <el-form-item label="分类名称">
          <el-input v-model="diaForm.name"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="diaForm.sort"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="diaForm.status"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCategoryList } from "@/api/category";
import { updateCategoryInfo } from "@/api/category";
import { queryPage } from "@/api/category";
import { searchCategory } from "@/api/category";
import { changeCategoryStatusBatch} from "@/api/category";
import { delBatchByIds } from "@/api/category";

export default {
  data() {
    return {
      dialogVisible: false,
      categoryList: [],
      selectedItems: [],
      diaForm: {},
      input: "",
      total: 0,
      currentPageNum: 1, // 默认页码1
      currentPageSize: 10, // 默认一页10个
    };
  },
  methods: {
    // 批量删除和单删
    deleteHandle(type,id){
      let params = {}
      let idArr = []
      if("单删"==type){
        idArr.push(id)
      }else{
        this.selectedItems.forEach(item=>{
          idArr.push(item.id)
        })
      }
      params.ids = idArr
      delBatchByIds(params).then(res=>{
        if(res.code === 200){
          this.$message.success("删除成功");
          this.query();
        }else{
          this.$message.error(res.msg);
        }
      })
    },
    // 启售:停售 状态更改
    statusHandle(row) {
      let params = {};
      if (typeof row === "string") {
        if (this.selectedItems.length == 0) {
          this.$message.error("批量操作，请先勾选操作分类！");
          return false;
        }
        let idsArr = []
        this.selectedItems.forEach(item=>{
          idsArr.push(item.id)
        })
        params.ids = idsArr
        params.status = row;
      } else {
        let idsArr = []
        idsArr.push(row.id)
        params.ids = idsArr;
        params.status = row.status ? "0" : "1";
      }
      this.$confirm("确认更改该套餐状态?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        // 起售停售---批量起售停售接口
        changeCategoryStatusBatch(params)
          .then((res) => {
            if (res.code === 200) {
              this.$message.success("状态已经更改成功！");
              this.query();
            } else {
              this.$message.error(res.msg || "操作失败");
            }
          })
          .catch((err) => {
            this.$message.error("请求出错了：" + err);
          });
      });
    },

    // 搜索
    handleQuery() {
      searchCategory(this.input).then((res) => {
        if (res.code === 200) {
          this.categoryList = [];
          this.categoryList = res.data;
          this.total = res.data.length;
        }
      });
    },

    currentQuery(page) {
      // alert(page)
      // 切换页码 改变当前默认页码
      if (page) {
        this.currentPageNum = page;
      }

      queryPage({
        pageNum: page,
        pageSize: this.currentPageSize,
      }).then((result) => {
        this.categoryList = result.data.row;
        this.total = parseInt(result.data.total);
      });
    },
    sizeChangeQuery(pageSize) {
      // 切换页的大小 改变当前默认大小
      this.currentPageSize = pageSize;
      queryPage({
        pageNum: this.currentPageNum,
        pageSize: pageSize,
      }).then((result) => {
        this.categoryList = result.data.row;
        this.total = parseInt(result.data.total);
      });
    },
    // 更新
    updateConfirm() {
      this.dialogVisible = false;
      updateCategoryInfo(this.diaForm).then((res) => {
        if (res.code === 200) {
          this.$message({
            message: "修改成功",
            type: "success",
          });
        } else {
          this.$message.error("修改失败");
        }
      });
      this.query();
    },
    handleClose() {
      this.dialogVisible = false;
    },
    updateInfo(row) {
      this.diaForm = {};
      this.dialogVisible = true;
      // 修改的对象
      this.diaForm = { ...row };
    },
    // 刷新列表
    query() {
      getCategoryList().then((res) => {
        // console.log(res.data);
        if (res.code === 200) {
          this.categoryList = res.data.row;
          this.total = parseInt(res.data.total);
          // console.log(res.data.total);
        }
      });
    },
    // 获取选中的数据
    handleSelectionChange(selection) {
      this.selectedItems = selection;
      if (this.selectedItems.length > 0) {
        this.selectedItems.forEach((item) => {
          // console.log("id:" + item.id + "  name:" + item.name);
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
.delBut {
  color: red;
}
.line {
  text-align: center;
}
.span-btn {
  cursor: pointer;
  display: inline-block;
  font-size: 14px;
  padding: 0 20px;
  color: #818693;
  border-right: solid 1px #d8dde3;
}
.blueBug {
  color: #419eff !important;
  position: relative;
}
.delBut {
  color: #f56c6c !important;
  position: relative;
}
.el-button-primary {
  color: #333333;
  background-color: #f0ffe7;
  border-color: #fdffa0;
}
.el-button-search {
  margin-left: 10px;
  color: rgb(115, 186, 243);
}
.el-pagination {
  text-align: center;
}
.pages-class {
  margin-top: 10px;
}
</style>
