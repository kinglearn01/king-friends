<template>
 <div style="display: flex; justify-content: center; align-items: center;">
   <svg t="1724139459249" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4836" width="200" height="200"><path d="M686 595.1c-19.8-17.6-42.3-31.8-66.6-42.4 41.2-32.4 67.8-82.6 67.8-139 0-97.4-79.2-176.6-176.6-176.6-97.4 0-176.6 79.2-176.6 176.6 0 56.4 26.5 106.6 67.8 139-24.3 10.6-46.8 24.9-66.6 42.4-48 42.7-74.5 99.7-74.5 160.6 0 16.6 13.4 30 30 30s30-13.4 30-30c0-91.2 85.2-165.5 189.9-165.5s189.9 74.2 189.9 165.5c0 16.6 13.4 30 30 30s30-13.4 30-30c-0.1-60.8-26.5-117.9-74.5-160.6zM394 413.7c0-64.3 52.3-116.6 116.6-116.6s116.6 52.3 116.6 116.6-52.3 116.6-116.6 116.6S394 478 394 413.7z" fill="#1296db" p-id="4837"></path><path d="M321.9 589.6c0-16.6-13.4-30-30-30-52.2 0-94.6-42.4-94.6-94.6 0-52.2 42.4-94.6 94.6-94.6 16.6 0 30-13.4 30-30s-13.4-30-30-30c-85.2 0-154.6 69.4-154.6 154.6 0 46.7 20.8 88.6 53.7 117-18.6 8.8-35.8 20.2-51.2 33.8C98 653 75 702.7 75 755.8c0 16.6 13.4 30 30 30s30-13.4 30-30c0-75.1 70.4-136.1 156.9-136.1 16.6-0.1 30-13.5 30-30.1zM883.8 615.9c-15.4-13.7-32.6-25-51.2-33.8 32.8-28.4 53.7-70.3 53.7-117 0-85.2-69.4-154.6-154.6-154.6-16.6 0-30 13.4-30 30s13.4 30 30 30c52.2 0 94.6 42.4 94.6 94.6 0 52.2-42.4 94.6-94.6 94.6-16.6 0-30 13.4-30 30s13.4 30 30 30c86.5 0 156.9 61.1 156.9 136.1 0 16.6 13.4 30 30 30s30-13.4 30-30c0-53.1-23-102.8-64.8-139.9z" fill="#1296db" p-id="4838"></path></svg>
 </div>

  <van-form @submit="onSubmit">
        <van-cell-group inset>
          <van-field
            v-model="userAccount"
            name="userAccount"
            label="账号"
            placeholder="请填写账号"
            :rules="[{ required: true, message: '请填写账号' }]"
          />
          <van-field
            v-model="userPassword"
            type="password"
            name="userPassword"
            label="密码"
            placeholder="请填写密码"
            :rules="[{ required: true, message: '请填写密码' }]"
          />
        </van-cell-group>
        <div style="display:flex;justify-content: space-around;margin-top:20px">
          <van-button type="primary" round size="large" native-type="submit">登录</van-button>
          <van-button type="primary" round size="large" to="/user/register">注册</van-button>
        </div>
      </van-form> 
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import myAxios from '../../plugins/myAxios';
import { showSuccessToast, showFailToast } from 'vant';
import {userInfoStore} from "../../store";
const router = useRouter();
const userAccount = ref('');
const userPassword = ref('');

const onSubmit = async () => {
   const res = await  myAxios.post('/user/login',{
        userAccount:userAccount.value,
        userPassword:userPassword.value
      })
      if(res.data.code === 0 && res.data){
        showSuccessToast("登录成功");
        const store = userInfoStore();
        store.isLogin=true;
        router.replace("/");
      }else{
        showFailToast("登录失败");
      }

};
</script>

<style>

</style>