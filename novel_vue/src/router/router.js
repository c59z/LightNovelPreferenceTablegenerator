// router.js
import Vue from 'vue';
import VueRouter from 'vue-router';
import Index from '../components/NovelTable'; // 假设 Index.vue 是你的首页组件

Vue.use(VueRouter);

const routes = [
    {
        path: '/index', // 这里定义了路由路径
        name: 'Index',
        component: Index,
    },
    {
        path: '/', // 添加一个重定向路由
        redirect: '/index', // 重定向到 /index
    },
];

const router = new VueRouter({
    mode: 'history', // 使用 HTML5 历史模式，可以去掉 URL 中的 #
    routes,
});

export default router;
