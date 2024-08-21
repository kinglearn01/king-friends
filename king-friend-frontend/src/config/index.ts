import * as VueRouter from 'vue-router'
import Index from "../components/pages/Index.vue";
import Team from "../components/pages/Team.vue";
import TeamMyJoin from "../components/pages/TeamMyJoin.vue";
import TeamMyCreate from "../components/pages/TeamMyCreate.vue";
import User from "../components/pages/User.vue";
import UserUpdate from "../components/pages/UserUpdate.vue";
import Serach from "../components/pages/Serach.vue";
import EditUser from "../components/pages/EditUser.vue";
import SerachResultPage from "../components/pages/SerachResultPage.vue";
import Login from "../components/pages/Login.vue";
import Register from "../components/pages/Register.vue";
import TeamAddPage from "../components/pages/TeamAddPage.vue";
import TeamUpdatePage from "../components/pages/TeamUpdatePage.vue";
import {createWebHistory} from "vue-router";
import {userInfoStore} from "../store";


const routes = [
    {path: '/', component: Index, meta: {title: "伙伴匹配", isLogin: true}},
    {path: '/team', component: Team, meta: {title: "找队伍", isLogin: true}},
    {path: '/team/my/create', component: TeamMyCreate, meta: {title: "我的队伍", isLogin: true}},
    {path: '/team/my/join', component: TeamMyJoin, meta: {title: "已加入队伍", isLogin: true}},
    {path: '/user', component: User, meta: {title: "用户信息", isLogin: true}},
    {path: '/user/update', component: UserUpdate, meta: {title: "个人信息", isLogin: true}},
    {path: '/serach', component: Serach, meta: {title: "搜索伙伴", isLogin: true}},
    {path: '/user/edit', component: EditUser, meta: {title: "个人信息修改", isLogin: true}},
    {path: '/user/list', component: SerachResultPage, meta: {title: "伙伴列表", isLogin: true}},
    {path: '/user/login', component: Login, meta: {title: "登录", isLogin: false}},
    {path: '/user/register', component: Register, meta: {title: "注册", isLogin: false}},
    {path: '/team/add', component: TeamAddPage, meta: {title: "创建队伍", isLogin: true}},
    {path: '/team/update', component: TeamUpdatePage, meta: {title: "更新队伍", isLogin: true}}

]
const router = VueRouter.createRouter({
    history: createWebHistory(),
    routes,
})
router.beforeEach((to, from, next) => {
    const store = userInfoStore()
    //-：获取是否登录的状态
    let isLogin = store.isLogin
    //-:访问的请求不是 login，不是reg 也没有登录
    if (to.path !== '/user/login' && to.path !== '/user/register' && !isLogin) {
        next({path:'/user/login'})
    } else if (to.path == '/user/login' && isLogin) {//-:已经登录了，还在访问登录请求
        next({path:'/'})
    } else {//否则，该干啥干啥
        next()
    }
})

export default router
