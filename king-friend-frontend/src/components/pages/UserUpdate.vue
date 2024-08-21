<template>
<template v-if="user">
    <van-cell title="用户名" is-link to="/user/edit" :value="user.username" @click="toEdit('username','用户名',user.username)" />
    <van-cell title="账号"  :value="user.userAccount" />
    <van-cell title="头像" is-link to="/user/edit" @click="toEdit('avatarUrl','头像',user.avatarUrl)">
        <img style="height:48px;" :src="user.avatarUrl"/>
    </van-cell>
    <van-cell title="性别" is-link to="/user/edit"  :value="user.gender?'男':'女'" @click="toEdit('gender','性别',user.gender?'男':'女')" />
    <van-cell title="电话" is-link to="/user/edit"  :value="user.phone" @click="toEdit('phone','电话',user.phone)"/>
    <van-cell title="邮箱" is-link to="/user/edit"  :value="user.email" @click="toEdit('email','邮箱',user.email)" />
    <van-cell title="注册时间"  :value="user.createTime" />
</template>
    
</template>

<script setup lang="ts">
import { onMounted,ref} from 'vue';
import { useRouter } from 'vue-router'
import  {getCurrentUser } from '../../service/user.ts'
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
</script>

<style>

</style>