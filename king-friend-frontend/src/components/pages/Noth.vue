<template>
    <van-card
    v-for="item in userList"
    :desc="item.profile"
    :title="`${item.username} (${item.planetCode})`"
    :thumb="item.avatarUrl"
  >
  <template #tags>
    <van-tag plain type="primary" v-for="tag in item.tags" style="margin:8px 0 0 8px ;">{{tag}}</van-tag>
  </template>
  <template #footer>
    <van-button size="mini">联系我</van-button>
  </template>
</van-card>
    <van-empty v-if="!userList || userList.length<1" description="搜索结果为空"></van-empty>
</template>

<script setup lang="ts">
import {ref,onMounted} from 'vue';
import { useRoute,useRouter } from 'vue-router'; 
import myAxios from '../../plugins/myAxios';
import qs from 'qs'
import { showSuccessToast, showFailToast } from 'vant';
const route = useRoute();
const searchTest = ref('');
const {tags} = route.query;
console.log(tags)
const userList = ref([]);
onMounted(async () => {
    const userListData = await myAxios.get('/user/serach/tags',{
        params:{
            tagNameList:tags,
        },
        paramsSerializer:params=>{
            return qs.stringify(params,{ indices:false })
        }
    })
    .then(function (response) {
    return response.data?.data;
  })
  .catch(function (error) {
  })
  if(userListData){
    userListData.forEach(user=>{
        if(user.tags){
            user.tags = JSON.parse(user.tags)
        }
    })
    userList.value = userListData;
  }
  });  




</script>

<style>

</style>