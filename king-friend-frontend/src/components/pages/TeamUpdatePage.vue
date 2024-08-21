<template>
    <van-form @submit="onSubmit">
        <van-cell-group inset>
          <van-field
            v-model="addTeamData.name"
            name="name"
            label="队伍名"
            placeholder="请填写队伍名"
            :rules="[{ required: true, message: '请填写队伍名' }]"
          />
          <van-field
            v-model="addTeamData.description"
            rows="1"
            autosize
            label="队伍描述"
            type="textarea"
            placeholder="请输入队伍描述"
         />
         <van-field
            v-model="addTeamData.expireTime"
            is-link
            readonly
            name="datePicker"
            label="过期时间"
            placeholder="点击选择时间"
            @click="showPicker = true"
            />
            <van-popup v-model:show="showPicker" position="bottom">
            <van-date-picker @confirm="onConfirm"  @cancel="showPicker = false" :min-date="time" />
            </van-popup>
           
          <van-field name="radio" label="队伍状态">
            <template #input>
              <van-radio-group v-model="addTeamData.status" direction="horizontal">
                <van-radio name="0">公开</van-radio>
                <van-radio name="1">私有</van-radio>
                <van-radio name="2">加密</van-radio>
              </van-radio-group>
            </template>
          </van-field>
          <van-field
            v-if="Number(addTeamData.status)===2"
            v-model="addTeamData.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请填写队伍密码"
            :rules="[{ required: true, message: '请填写密码' }]"
          />
        </van-cell-group>
        <div style="margin: 16px;">
          <van-button round block type="primary" native-type="submit">
            提交
          </van-button>
        </div>
      </van-form>
      
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { showSuccessToast, showFailToast } from 'vant';
import { ref,onMounted } from 'vue';
import myAxios from '../../plugins/myAxios';
const router =useRouter();
const route = useRoute();
const time =new Date;
const addTeamData = ref({})
const id = route.query.id
//获取之前的队伍信息
onMounted(async ()=>{
  if(id<=0){
    showFailToast("加载队伍失败!");
    return;
  }
  const res = await myAxios.get("/team/get",{
    params:{
      id
    }
  })
  if(res?.data.code == 0 && res.data){
        addTeamData.value=res.data.data
    }else{
        showFailToast("加载队伍失败");
    } 
})
const showPicker = ref(false);
const onConfirm = ({ selectedValues }) => {
  addTeamData.value.expireTime = selectedValues.join('-');
  showPicker.value = false;
};
 
const  onSubmit = async ()=>{
    const postData = {
        ...addTeamData.value,
        status:Number(addTeamData.value.status),
        expireTime:new Date(addTeamData.value.expireTime)
    }
      const res = await myAxios.post("/team/update",postData);
      console.log(res);
    if(res?.data.code === 0 && res.data){
        showSuccessToast("更新成功");
        router.push({
            path:'/team',
            replace:true
        })
    }else{
        showFailToast("更新失败");
    }
  }
</script>

<style>

</style>