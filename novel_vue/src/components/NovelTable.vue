<template>
  <div class="hello">
    <div ref="screenshotArea" id="MyDivTable">
      <canvas @click="cellClick" ref="myCanvas" id="MyNovelTable" width="800" height="800" >

      </canvas>
      <el-dialog
          @close="searchClose"
          :close-on-click-modal=false
          title="搜索"
          :visible.sync="centerDialogVisible"
          width="50%"
          center>
        <el-dialog
            :show-close="dialog_show_close"
            :close-on-press-escape=false
            @close="showNovelClose"
            :close-on-click-modal=false
            class="resultDialog"
            width="40%"
            :title=search_title
            :visible.sync="searchResultVisible"
            append-to-body>
          <div
              v-loading="search_loading"
              element-loading-text="正在搜索中..."
              element-loading-spinner="el-icon-loading"
              class="bookDiv">
              <!--如果没找到图片则提示未找到内容-->
            <div v-show="isNotFound"
                class="noFindInfo">
              未找到该小说，可以试试用文字代替封面
            </div>
              <ul id="browserItemList"
                  :key="novel.title"
                  v-for="novel in novel_data" class="infinite-list-item">
                <img class="novelCover" :src="novel.cover" :alt="novel.title">
                <div class="novelTitle">{{novel.title}}</div>
                <br>
                <div class="novelSTitle">{{novel.stitle}}</div>
                <br>
                <div class="novelInfo">{{novel.info}}</div>
                <br>
                <el-button @click="btnSubmitNovel(novel.coverBase64)" class="btnSubmit" type="success" icon="el-icon-check" circle></el-button>
              </ul>
          </div>
        </el-dialog>
<!--        <span>当前行：{{this.current_row}}</span>-->
<!--        <span>当前列：{{this.current_col}}</span>-->
        <br>
        <div class="MySearchBar">
          <el-input style="left: 15%" v-model="search_str" placeholder="请输入要搜索的轻小说"></el-input>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button icon="el-icon-circle-close" type="success" round @click="BtnCloseSearch">取消搜索</el-button>
          <el-button icon="el-icon-search" type="success" round @click="BtnSearchBook">使用番组计划搜索</el-button>
          <el-button icon="el-icon-chat-line-square" type="success" round @click="BtnToTextCover">使用文字作为封面</el-button>
          <el-button @click="clearCurrentCell" icon="el-icon-delete" type="info" round>重置为空</el-button>
        </span>
      </el-dialog>


    </div>
    <p class="pCopyright">GitHub : <a class="aCopyright" target="_blank" href="https://github.com/c59z">https://github.com/c59z
    </a>  | 查询API:
      <a style="text-decoration: none;color: #2c3e50;" target="_blank" href="https://chii.in/">
        番组计划Bangumi
      </a> </p>
    <div id="tableInfo">
      <el-button @click="saveByPhoto" class="btn_create" type="info" icon="el-icon-download" round>点击生成个人爱好表</el-button>
      <el-button @click="clearAllList" type="danger" icon="el-icon-delete" round>清除当前所有内容</el-button>
    </div>
  </div>
</template>

<script>

import html2canvas from "html2canvas";

export default {
  name: 'NovelTable',
  props: {
    msg: String
  },
  computed:{

  },
  methods: {
    // 使用了Html2Canvas插件进行
    saveByPhoto(){
      const screenshotArea = this.$refs.screenshotArea;
      html2canvas(screenshotArea).then(canvas => {
        // 将canvas转换为图片URL
        const imageUrl = canvas.toDataURL();

        // 创建一个a标签用于下载截图
        const downloadLink = document.createElement('a');
        downloadLink.href = imageUrl;
        downloadLink.download = '个人轻小说生涯喜好表.png';
        downloadLink.click();
      });

    },
    // 使用文字作为封面
    BtnToTextCover(){
      var currentCell = 5 * (this.current_row - 1) + this.current_col - 1;
      // 将该单元格的封面清空
      this.cover_list[currentCell] = ''
      this.title_list[currentCell] = this.search_str
      // 重新调用画图方法(重要，否则选择后将会无法继续绘制图片)
      this.drawNovelCovers()
      console.log(this.title_list[currentCell])
      this.searchResultVisible = false
      this.centerDialogVisible = false
    },
    // 清除当前所选的小说封面
    clearCurrentCell(){
      var currentCell = 5 * (this.current_row - 1) + this.current_col - 1;
      this.cover_list[currentCell] = '';
      this.title_list[currentCell] = '';
      // 重新调用画图方法(重要，否则选择后将会无法继续绘制图片)
      this.drawNovelCovers()
      this.searchResultVisible = false
      this.centerDialogVisible = false
    },
    // 清除所有选择的小说封面
    clearAllList(){
      this.cover_list = []
      this.title_list = []
      this.drawNovelCovers()
    },
    // 绘制页面画板(核心)
    drawNovelCovers(){
      const canvas = this.$refs.myCanvas;
      const ctx = canvas.getContext('2d');
      // 每次调用清空画布
      // ctx.translate(50, 50)
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = '#ffffff';
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      ctx.fillStyle = '#333';
      ctx.font = "bold 32px Microsoft YaHei"
      ctx.strokeStyle  = "black"
      const text = "个人轻小说生涯喜好表";
      const textWidth = ctx.measureText(text).width;
      ctx.fillText(text,canvas.width/2-textWidth/2,50);



      const numRows = 3;
      const numCols = 5;
      const cellWidth = canvas.width / numCols;
      const cellHeight = (canvas.height -75) / numRows;


      // 自适应表格高度（文字）
      const textHeight = (cellHeight/10)*1.2;
      const cellTextHeight = cellHeight/12;
      // 因为第一条竖线和最后一条竖线过小所以
      ctx.lineWidth = 3;
      ctx.strokeRect(0,75,0,800)
      ctx.strokeRect(800,75,800,800)
      ctx.lineWidth = 1;
      // 绘制表格
      for (let row = 0; row <= numRows; row++) {
        for (let col = 0; col <= numCols; col++) {
          const x = col * cellWidth;
          const y = row * cellHeight + 75;
          // 绘制15个单元格
          ctx.strokeRect(x, y, cellWidth, cellHeight);
          // 绘制小表格
          if(row !== 0){
            ctx.strokeRect(x, y-textHeight, cellWidth, textHeight);
          }
          // 在每个单元格下面添加文字备注
          const Tag = this.tag_list[row * numCols + col]
          const cellTextWidth = ctx.measureText(Tag).width;
          const cellTextX = x + (cellWidth - cellTextWidth) / 2;
          const cellTextY = y + cellHeight - cellTextHeight / 2;
          ctx.font = "normal 22px Microsoft YaHei"
          ctx.fillText(Tag, cellTextX, cellTextY+2);

          // 填充图片
          const img = new Image();
          img.src = this.cover_list[row * numCols + col]; // 根据索引获取图片URL
          img.onload = function() {
            ctx.drawImage(img, x+1, y+1, cellWidth-2, cellHeight - textHeight - 2);
          };

          // 绘制文字
          const novel_title = this.title_list[row * numCols + col]
          if(novel_title){
            if (ctx.measureText(novel_title).width > cellWidth - 10) {
              console.log(">>>长标题")
              // 如果文本宽度超过单元格宽度，则进行自动换行
              const words = novel_title.split('');
              let line = '';
              const lines = [];

              for (const word of words) {
                const testLine = line + (line ? ' ' : '') + word;
                const testMetrics = ctx.measureText(testLine);
                if (testMetrics.width > cellWidth - 10) {
                  console.log("testMetrics.width > cellWidth - 10")
                  lines.push(line);
                  line = word;
                } else {
                  line = testLine;
                }
              }
              lines.push(line);
              // 绘制换行后的文本
              // ctx.font = '14px Arial';
              // ctx.fillStyle = '#333';
              for (let i = 0; i < lines.length; i++) {
                ctx.fillText(lines[i], x + 10, y + 35 + (i * 30), cellWidth - 10);
              }
            }else{
              console.log(">>>短标题")
              // ctx.font = '14px Arial';
              ctx.fillText(novel_title, x + 5, y + 20);
            }
          }
        }
      }
    },
    // 提交当前选择的轻小说
    btnSubmitNovel(cover){
      // 判断选择了哪一个小说 cover 传值
      // 计算当前到底选择了哪一个单元格
      var currentCell = 5 * (this.current_row - 1) + this.current_col - 1;
      // 将当前轻小说封面的base64插入cover_list中
      this.cover_list[currentCell] = cover;
      // 清除当前单元格的文字
      this.title_list[currentCell] = '';
      // 重新调用画图方法(重要，否则选择后将会无法继续绘制图片)
      this.drawNovelCovers()
      this.searchResultVisible = false
      this.centerDialogVisible = false
    },
    // 关闭查找轻小说对话框后调用
    showNovelClose(){
      this.novel_data = ""
      this.search_loading = true
      this.isNotFound = false
    },
    // 关闭搜索对话框后调用
    searchClose(){
      this.search_str = ""
    },
    // 关闭最外部搜索框后
    BtnCloseSearch(){
      this.centerDialogVisible = false
      this.search_str = ''
    },
    // 请求后端，搜索对应轻小说
    BtnSearchBook(){
      this.searchResultVisible = true
      // 向后端发送请求
      let _this = this;
      _this.request.post("/search?search_text="+this.search_str).then(res => {
        if (res.code !== 200){
          // 请求失败error
          this.$message.error(res.msg);
          this.dialog_show_close = true
        }else{
          // 请求成功success
          this.$message.success("查询成功!");
          this.dialog_show_close = true
          this.search_loading = false
          this.isNotFound = false
          // res.data 里面是搜索到的轻小说信息
          if(res.data[0].length>0){
            this.novel_data = res.data[0]
            console.log("log>>>返回结果")
            console.log(this.novel_data)
            console.log("log>>>返回结果结束")
          }else{
            console.log("没有查询到")
            this.isNotFound = true
          }

        }
      })
      this.search_title = "关于 「"+this.search_str+"」 的搜索结果如下:"
    },

    // 点击查询轻小说
    cellClick(event) {
      const canvas = this.$refs.myCanvas;
      const rect = canvas.getBoundingClientRect();
      const mouseX = event.clientX - rect.left;
      const mouseY = event.clientY - rect.top;

      const numRows = 3;
      const numCols = 5;
      const cellWidth = canvas.width / numCols;
      const cellHeight = (canvas.height - 50) / numRows; // 调整表格高度

      for (let row = 0; row < numRows; row++) {
        for (let col = 0; col < numCols; col++) {
          const x = col * cellWidth;
          const y = row * cellHeight + 50;

          if (
              mouseX >= x && mouseX <= x + cellWidth &&
              mouseY >= y && mouseY <= y + cellHeight
          ) {
            // console.log(`点击了 (${row + 1}, ${col + 1})`);
            this.current_row = row + 1;
            this.current_col = col + 1;
            // 弹出搜索框
            this.openSearchdialog()
            return;
          }
        }
      }
    },
    openSearchdialog(){
      this.centerDialogVisible = true;
    }
  },
  data(){
    return {
      // 存放图片的数组
      cover_list: [

      ],
      title_list: ['','','','','',
      '','','','','',
      '','','','','',],
      tag_list: [
        '入坑作','最喜欢的','看最多次','最佳插画','最想安利'
        ,'最胃疼','最对xp','最有乐子','最动人的','印象最深的'
        ,'最光滑的', '最砂纸的','最被高估的','最被低估的','最lkd'],
      centerDialogVisible: false,
      current_row: 0,
      current_col: 0,
      searchResultVisible: false,
      search_str: '',
      search_title: '',
      novel_data: [],
      search_loading: true,
      isNotFound: false,
      dialog_show_close: false,
    }
  },
  mounted() {
    // 页面初始化时调用绘图方法
    this.drawNovelCovers()
  }
}
</script>

<style scoped>

  #MyDivTable {
    border-radius: 3rem;
    background-color: #fff;
    width: 70rem;
    height: 55rem;
    display: inline-block;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
  }

  #MyNovelTable:hover {
    /*background-color: #fff;*/
    cursor: pointer;
  }

  #tableInfo {
    margin-top: 1rem;
    border-radius: 3rem;
    background-color: #fff;
    width: 70rem;
    height: 6rem;
    display: inline-block;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
  }

  .pCopyright {
    margin-top: 1rem;
  }
  .aCopyright {
    color: #2c3e50;
    text-decoration: none;
  }
  .btn_create {
    margin-top: 2rem;
    display: inline-block;
  }
  .MySearchBar {
    width: 75%;
  }
  .el_input{
    text-align: center;
  }
  .bookDiv {
    overflow-y: scroll; /* 添加垂直滚动条 */
    width: 100%;
    max-height: 36rem;
    min-height: 12rem;
  }
  .resultDialog {
    padding: 10px;
    overflow: hidden;
  }

  #browserItemList {
    padding: 1rem;
    border-radius: 1rem;
    background-color: rgba(0,0,0,0.1);
    overflow: hidden;
    margin: 10px 0;
    width: 94%;
    height: auto;
  }

  .noFindInfo{
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1rem;
    border-radius: 1rem;
    background-color: rgba(0,0,0,0.1);
    overflow: hidden;
    margin: 10px 0;
    width: 94%;
    height: 14rem;
    font-size: 24px;
  }

  .novelCover {
    float: left;
    display: inline-block;
    width: 20%;
    height: 11rem;
  }
  .novelTitle {
    float: left;
    padding-left: 1rem;
    width: 70%;
    display: inline-block;
    font-size: 18px;
  }
  .novelSTitle {
    float: left;
    margin-top: 0.5rem;
    padding-left: 1rem;
    width: 70%;
    display: inline-block;
    font-size: 16px;
  }

  .novelInfo {
    float: left;
    margin-top: 1rem;
    padding-left: 1rem;
    width: 70%;
    display: inline-block;
    font-size: 12px;
  }


  .btnSubmit {
    position: relative;
    /*display: block;*/
    bottom: -4rem;
  }

</style>
