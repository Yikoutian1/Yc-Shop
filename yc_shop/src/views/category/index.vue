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
      <span class="span-btn delBut non" @click="deleteHandle('批量', null)"
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
      <el-button type="primary" @click="addCategory()"> + 新建分类 </el-button>
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
      <el-table-column property="name" label="分类名称" width="350" />
      <el-table-column property="sort" label="排序" width="90" />
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <span style="margin-right: 10px">{{
            scope.row.status == "0" ? "停售" : "启售"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="200"
        property="createTime"
        label="创建时间"
        show-overflow-tooltip
      />
      <el-table-column
        width="200"
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
        <el-form-item label="分类名称">
          <el-input v-model.trim="diaForm.name"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model.number="diaForm.sort"></el-input>
        </el-form-item>
        <!-- <el-form-item label="状态">
          <el-input v-model="diaForm.status"></el-input>
        </el-form-item> -->
        <el-tooltip :content="'分类: ' + diaFormStatus" placement="top">
          <el-switch
            inactive-text="停售"
            active-text="启售"
            v-model="diaFormStatus"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="启售"
            inactive-value="停售"
          >
          </el-switch>
        </el-tooltip>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateConfirm">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="分类添加"
      :visible.sync="dialogVisibleForAddCategory"
      width="40%"
      :before-close="handleAddCategoryClose"
    >
      <el-form ref="diaForm" :model="diaForm" class="dia-form">
        <el-form-item label="分类名称">
          <el-input v-model.trim="diaForm.name"></el-input>
        </el-form-item>
        <el-form-item label="排序(默认为0)">
          <br />
          <el-input-number
            size="small"
            v-model="diaFormSort"
            :min="1"
            :max="999"
            :step="1"
          ></el-input-number>
        </el-form-item>
        <el-switch
          inactive-text="停售"
          active-text="启售"
          v-model="diaFormStatus"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="启售"
          inactive-value="停售"
        >
        </el-switch>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="concalAdd">取 消</el-button>
        <el-button type="primary" @click="addConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCategoryList } from "@/api/category";
import { updateCategoryInfo } from "@/api/category";
import { queryPage } from "@/api/category";
import { searchCategory } from "@/api/category";
import { changeCategoryStatusBatch } from "@/api/category";
import { delBatchByIds } from "@/api/category";
import { addCategoryInfo } from "@/api/category";
export default {
  data() {
    return {
      dialogVisible: false,
      dialogVisibleForAddCategory: false,
      categoryList: [],
      selectedItems: [],
      diaForm: {},
      diaFormSort: 10,
      diaFormStatus: "启售", // 默认选择框value
      input: "",
      total: 0,
      currentPageNum: 1, // 默认页码1
      currentPageSize: 10, // 默认一页10个
      MAX: 999,
      MIN: 1,
    };
  },
  methods: {
    addCategory() {
      this.diaForm = {};
      this.dialogVisibleForAddCategory = true;
    },
    handleAddCategoryClose() {
      this.dialogVisibleForAddCategory = false;
    },
    // 批量删除和单删
    deleteHandle(type, id) {
      let params = {};
      let idArr = [];
      if ("单删" == type) {
        idArr.push(id);
      } else {
        this.selectedItems.forEach((item) => {
          idArr.push(item.id);
        });
      }
      params.ids = idArr;
      delBatchByIds(params).then((res) => {
        if (res.code === 200) {
          this.$message.success("删除成功");
          this.query();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 启售:停售 状态更改
    statusHandle(row) {
      let params = {};
      if (typeof row === "string") {
        if (this.selectedItems.length == 0) {
          this.$message.error("批量操作，请先勾选操作分类！");
          return false;
        }
        let idsArr = [];
        this.selectedItems.forEach((item) => {
          idsArr.push(item.id);
        });
        params.ids = idsArr;
        params.status = row;
      } else {
        let idsArr = [];
        idsArr.push(row.id);
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
    // 新增分类确认
    addConfirm() {
      this.dialogVisible = false;
      if (this.diaFormStatus == "启售") {
        this.diaForm.status = 1;
      } else {
        this.diaForm.status = 0;
      }
      this.diaForm.sort = this.diaFormSort;
      // console.log(this.diaForm.name);
      if (this.diaForm.name == undefined || this.diaForm.name == "") {
        this.$prompt("请输入分类名称", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputPattern: /^.+$/,
          inputErrorMessage: "名字不能为空",
        })
          .then(({ value }) => {
            this.$message({
              type: "success",
              message: "输入的分类名称为: " + value,
            });
            this.diaForm.name = value;
            addCategoryInfo(this.diaForm).then((res) => {
              console.log(this.diaForm);
              if (res.code === 200) {
                this.$message({
                  message: "新增成功",
                  type: "success",
                });
                this.query();
              } else {
                this.$message.error("新增失败");
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "取消输入",
            });
          });
      } else {
        addCategoryInfo(this.diaForm).then((res) => {
          console.log(this.diaForm);
          if (res.code === 200) {
            this.$message({
              message: "新增成功",
              type: "success",
            });
            this.query();
          } else {
            this.$message.error("新增失败");
          }
        });
      }
    },
    // 更新
    updateConfirm() {
      this.dialogVisible = false;
      // console.log(this.diaFormStatus);
      if (this.diaFormStatus == "启售") {
        this.diaForm.status = 1;
      } else {
        this.diaForm.status = 0;
      }
      if (this.diaForm)
        // console.log(this.diaForm);
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
    // TODO
    updateInfo(row) {
      this.diaForm = {};
      // row.status = row.status == 1 ? "启售" : "停售";
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
    concalAdd() {
      this.dialogVisibleForAddCategory = false;
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
