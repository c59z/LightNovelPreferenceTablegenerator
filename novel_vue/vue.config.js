const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    open: true,
    // host: '192.168.3.33',
    // host: '192.168.0.110',
    host: '192.168.1.108', // You Ip
    // host: 'localhost',
    port: 8080,
    https: false,
    //以上的ip和端口是我们本机的;下面为需要跨域的
    proxy: { //配置跨域
      '/api': {
        // target: 'http://192.168.3.33:7777', //填写请求的目标地址
        // target: 'http://192.168.0.110:7777', //填写请求的目标地址
        target: 'http://192.168.1.108:7777', //填写请求的目标地址
        changOrigin: true, //允许跨域
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
})




