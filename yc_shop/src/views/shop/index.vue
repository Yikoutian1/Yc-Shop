<template>
  <div class="app-container">
    <div>
      <el-input
        v-model="input"
        placeholder="请输入商品名称"
        style="width: 200px"
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
      <el-button type="primary" @click="addShop()"> + 新增商品 </el-button>
    </div>
    <el-dialog
      title="商品添加"
      :visible.sync="dialogVisibleForAddShop"
      width="500px"
      :before-close="handleAddShopClose"
    >
      <el-form ref="diaForm" :model="diaForm" :rules="rules" class="dia-form">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model.trim="diaForm.name" style="width: 230px"></el-input>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input
            v-model.number="diaForm.price"
            type="number"
            style="width: 230px"
          ></el-input>
        </el-form-item>
        <el-form-item label="选择分类名称" prop="categoryId">
          <el-select v-model="diaForm.categoryId" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.id"
              :value="item.id"
              :label="item.name"
              :disabled="!item.status"
            >
            </el-option>
          </el-select>
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
        <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="textarea"
        >
        </el-input>
      </el-form>
      <el-upload
        class="upload-demo"
        action="http://localhost:8080/images/upload"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :before-remove="beforeRemove"
        :on-change="handleChange"
        multiple
        :limit="3"
        :on-exceed="handleExceed"
        :file-list="fileList"
        :on-success="handleUploadSuccess"
      >
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">
          只能上传jpg/png文件，且不超过500kb
        </div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleAddShopClose">取 消</el-button>
        <el-button type="primary" @click="addConfirm()">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 图片预览窗口 -->
    <el-dialog :visible.sync="previewVisible" style="">
      <img :src="previewPath" style="width: 100%; height: 100%" />
    </el-dialog>
  </div>
</template>

<script>
import { getCategoryAllList } from "@/api/category";
import { shopInfoAdd } from "@/api/shop";
export default {
  data() {
    return {
      input: "", // 搜索的值
      dialogVisibleForAddShop: false,
      fileList: [], // 照片
      options: [], // 选择的分类
      value: "", // 选择的数据
      diaForm: {}, // 添加的数据
      textarea: "", // 商品描述内容
      uploadAction: "/images/upload", // 占位符，实际上传操作在后端处理
      previewVisible: false,
      previewPath: "", // url
      diaFormStatus: "启售", // 默认起售
      basePhotoUrl: "http://rz0io97i6.hn-bkt.clouddn.com/",
      resList: [], // 返回的url集合
      rules: {
        name: [{ required: true, message: "请输入商品名字", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请选择分类", trigger: "blur" },
        ],
        price: [
          {
            required: true,
            message: "请输入价格",
          },
          {
            type: "number",
            message: "输入的价格必须为数字",
          },
          { validator: this.validateDecimal, trigger: "blur" },
        ],
      },
    };
  },

  methods: {
    addConfirm() {
      this.$refs.diaForm.validate((valid) => {
        if (valid) {
          this.resList = this.handleFileList(this.fileList);
          this.diaFormStatus = "启售" == this.diaFormStatus ? 1 : 0;
          let list = {
            name: this.diaForm.name,
            price: this.diaForm.price,
            categoryId: this.diaForm.categoryId,
            describle: this.textarea,
            images: this.resList,
            status: this.diaFormStatus,
          };
          // 执行添加操作
          // 调用上传接口
          // console.log(list);
          shopInfoAdd(list)
            .then(() => {
              this.$message({
                showClose: true,
                message: "上传成功",
                type: "success",
              });
              dialogVisibleForAddShop = false;
            })
            .catch((error) => {
              this.$message({
                showClose: true,
                message: "上传失败",
                type: "error",
              });
              dialogVisibleForAddShop = false;
            });
        } else {
          // 验证失败
          return false;
        }
      });
    },
    handleChange(file, fileList) {
      this.fileList = fileList;
      // console.log(this.fileList)
    },
    handleFileList(fileList) {
      let res = [];
      fileList.forEach((item) => {
        // console.log(item)
        let url = item.response.data;
        let back = url.substring(this.basePhotoUrl.length);
        res.push(back);
      });
      // console.log(res)
      return res;
    },
    // 上传成功!!
    handleUploadSuccess(response, file, fileList) {
      // 在上传成功后执行你的操作
      // console.log("上传成功", response);
      let url = response.data;
      // http://rz0io97i6.hn-bkt.clouddn.com/2023/08/18/07c1d68f42b942bb96cd9ddd734adca5.png
      const pre = "http://rz0io97i6.hn-bkt.clouddn.com/";
      let back = url.substring(pre.length);
      // this.fileList.push(back)
      // console.log(this.fileList)
      // 执行其他操作
    },
    handlePreview(file) {
      // 预览文件的处理函数
      if (file.raw) {
        this.previewPath = URL.createObjectURL(file.raw); // 设置当前预览图片的 URL
        this.previewVisible = true; // 打开图片预览弹窗
      }
    },
    handleRemove(file, fileList) {
      // 移除文件的处理函数
      this.fileList = fileList.filter((item) => item.uid != file.uid);
      // console.log(this.fileList);
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + fileList.length
        } 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    // 搜索
    handleQuery() {},
    // 删除
    deleteHandle() {},
    // 改变状态
    statusHandle() {},
    addShop() {
      this.dialogVisibleForAddShop = true;
    },
    // close dialog
    handleAddShopClose() {
      this.dialogVisibleForAddShop = false;
    },
    validateDecimal(rule, value, callback) {
      if (value !== null && value !== undefined) {
        const decimalRegex = /^\d+(\.\d{1,2})?$/;
        if (!decimalRegex.test(value)) {
          callback(new Error("小数位数不能超过两位"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
  },
  mounted() {
    getCategoryAllList().then((res) => {
      this.options = res.data.row;
    });
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
div .upload-demo {
  /* padding-left: 80px; */
}
.el-col .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;

  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
