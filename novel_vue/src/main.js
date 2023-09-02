import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import request from "./utils/request";
import router from './router/router'; // 导入路由配置

// import axios from "axios"
Vue.prototype.request = request
Vue.config.productionTip = false;




Vue.config.productionTip = false
Vue.use(ElementUI);

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
