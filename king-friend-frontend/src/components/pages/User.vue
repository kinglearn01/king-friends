<template>
<template v-if="user">
    <van-cell title="当前用户" :value="user?.username" />
    <van-cell title="修改信息" is-link to="/user/update" />
    <van-cell title="我创建的队伍" is-link to="/team/my/create" />
    <van-cell title="我加入的的队伍" is-link to="/team/my/join" />
    <div style="padding:12px;">
        <van-button type="primary" block @click="layoutUser">退出登录</van-button>
    </div>
</template>
    
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router'
import myAxios from '../../plugins/myAxios';
import {getCurrentUser} from '../../service/user.ts'
import {showFailToast, showSuccessToast} from 'vant';
import {userInfoStore} from "../../store";

const router  = useRouter();


const user = ref({});

onMounted(async () => {
    const res =  await getCurrentUser(); 
    user.value=res.data
  });  

const toEdit=(editKey:string,editName:string,currentValue:string)=>{
    router.push({
        path:'/user/edit',
        query:{
            editKey,
            editName,
            currentValue,
        }
    })
}
const layoutUser = async ()=>{
   const res = await myAxios.post("/user/logout");
   if(res.data.code === 0 && res.data){
        showSuccessToast("退出成功");
      const store = userInfoStore();
      store.isLogin=false;
        router.replace("/user/login");
      }else{
        showFailToast("退出失败");
      }
}
</script>

<style>

</style>