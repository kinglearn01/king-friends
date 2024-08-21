import {defineStore} from  'pinia'

export const userInfoStore = defineStore('userInfo', {
    state: () => ({
        //-登录状态
        isLogin: false,
        //-登录用户信息
        userInfo: {
            userName: null,
            userPhone: null
        }
    }),
    //-开启持久化
    persist: {
        enabled: true
    }
})