<template>
    <van-form @submit="onSubmit">
          <van-field
            v-model="editUser.currentValue"
            :name="editUser.editKey"
            :label="editUser.editName"
            :placeholder="`请输入${editUser.editName}`"
            :rules="[{ required: true, message: '请填写用户名' }]"
          />
        <div style="margin: 16px;">
          <van-button round block type="primary" native-type="submit">
            提交
          </van-button>
        </div>
      </van-form>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {useRoute,useRouter} from 'vue-router'
import myAxios from '../../plugins/myAxios';
import { rewriteDefault } from 'vue/compiler-sfc';
import {getCurrentUser} from '../../service/user.ts'
import { showSuccessToast, showFailToast } from 'vant';




const router = useRouter();
const route=useRoute();
const editUser = ref({
    editKey:route.query.editKey,
    currentValue:route.query.currentValue,
    editName:route.query.editName
})



const onSubmit = async () => {
  const currentUser = await getCurrentUser();
      if(!currentUser){
        showSuccessToast('未登录');
        return;
      }
      
    const ref =  await myAxios.post("/user/update",{
      id:currentUser.data.id,
      [editUser.value.editKey] :editUser.value.currentValue
    })
    console.log(currentUser)
    if(currentUser.code === 0 ){
      showSuccessToast('更新成功');
      router.back();
    }else{
      showFailToast('更新失败');
    }
};

</script>


<style>

</style>