<template>
 
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
        <van-field
        v-model="confirmPassword"
        type="password"
        name="confirmPassword"
        label="确认密码"
        placeholder="请再次确认密码"
        :rules="[{ required: true, message: '请再次确认密码' }]"
      />
      <van-field
      v-model="List"
      name="List"
      label="标签"
      placeholder="请填写标签"
    ></van-field>
    <van-field
    v-model="gender"
    name="gender"
    label="性别"
    placeholder="请填写性别"
  />
  <van-field
    v-model="email"
    name="email"
    label="邮箱"
    placeholder="请填写邮箱"
  />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          注册
        </van-button>
      </div>
    </van-form> 
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import myAxios from '../../plugins/myAxios';
import { showSuccessToast, showFailToast } from 'vant';
const router = useRouter();
const userAccount = ref('');
const userPassword = ref('');
const confirmPassword =ref('')
const List =ref("");
const gender = ref('')
const arr=(val)=>{
 const tagsList = val.split(',')
 tagsList.map(item=>`"${item}"`)
  return tagsList
};
const numgender =(val)=>{
      if(val==='男') return 1;
      else return 0;
}
const email =ref('')
const onSubmit = async () => {
   const res = await  myAxios.post('/user/register',{
        userAccount:userAccount.value,
        userPassword:userPassword.value,
        checkPassword:confirmPassword.value,
        tags:arr(List.value),
        gender:numgender(gender.value),
        email:email.value
      })
      if(res.data.code === 0 && res.data){
        showSuccessToast("注册成功");
        router.replace("/user/login");
      }else{
        showFailToast("注册失败"+res.data.description);
      }

};
</script>

<style>

</style>