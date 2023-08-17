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
        <el-button type="primary" @click="addCategory()"> + 新增商品 </el-button>
      </div>
      <el-select v-model="value" placeholder="请选择">
        <el-option
          v-for="item in options"
          :key="item.id"
          :label="item.name"
          :disabled="!item.status">
        </el-option>
      </el-select>
    </div>
  </template>
  
  <script>
  import { getCategoryAllList } from "@/api/category";
  export default {
    data() {
      return {
        options:[]
      }
    },
    methods: {
      
    },
    mounted(){
      getCategoryAllList().then(res=>{
        this.options = res.data
      })
    }
  }
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

  </style>
  
  