<template>
  <div id="teamCardList">
    <van-card
    v-for="team in teamList"
    :desc="'口号：'+team.description"
    thumb='https://tse1-mm.cn.bing.net/th/id/OIP-C.VnqbqHI999-VVVkUOWBcMwAAAA?rs=1&pid=ImgDetMain'
    :title="`${team.name}`"
  >
    <template #tags>
      <van-tag plain type="primary" style="margin:8px 0 0 8px ;">{{teamStatusEnum[team.status]}}</van-tag>
    </template>
    <template #bottom>
    <div>{{ '最大人数: '+team.maxNum }}</div>
    <div>{{ '创建时间: '+team.createTime }}</div>
    <div>{{ '过期时间: '+team.expireTime }}</div>
    </template>
    <template #footer>
      <van-button v-if="team.userId !== currentUser?.data.id&&!team.hasJoin" size="mini" type="primary" @click="preJoinTeam(team)">加入队伍</van-button>
      <van-button v-if="team.userId === currentUser?.data.id" size="mini" type="primary" @click="doupdateTeam(team.id)">更新队伍</van-button>
      <van-button v-if="team.userId !== currentUser?.data.id&&team.hasJoin" size="mini" type="primary" @click="doQuitTeam(team.id)">退出队伍</van-button>
      <van-button v-if="team.userId === currentUser?.data.id" size="mini" type="warning" @click="doDeleteTeam(team.id)">解散队伍</van-button>
    </template>
  </van-card>
  <van-dialog v-model:show="show" title="请输入密码" show-cancel-button @confirm="doJoinTeam" @cancel="doCancel">
    <van-field v-model="password"  />
  </van-dialog>
  </div>
  
</template>

<script setup lang="ts">
import { defineProps,withDefaults,ref,onMounted} from 'vue';
import {TeamType} from '../models/team'
import {teamStatusEnum} from "../constant/team";
import myAxios from '../plugins/myAxios';
import { showFailToast, showSuccessToast,} from 'vant';
import { getCurrentUser } from '../service/user';
import { useRouter } from 'vue-router';
const router =useRouter();
const show = ref(false);
const joinTeamId =ref();
const password =ref('')
const pone=ref('')
const doupdateTeam =(id:number)=>{
  router.push({
    path:'/team/update',
    query:{
      id:id
    }
  })
}
const doCancel =()=>{
  joinTeamId.value=0;
  password.value=''
}
const preJoinTeam =(team:TeamType)=>{
  joinTeamId.value=team.id;
  if(team.status === 0){
    doJoinTeam();
  }else{
    show.value=true
  }
}
const doQuitTeam =  async(id:number)=>{
  const res = await myAxios.post('/team/quit',{
     teamId:id
    })
    if(res?.data.code === 0&&res.data){
        showSuccessToast("退出成功");
        location.reload()
    }else{
      showFailToast("退出失败,"+res.data.description);
    }
}
const doDeleteTeam =async(id:number)=>{
  const res = await myAxios.post('/team/delete',{
     id
    })
   
    if(res?.data.code === 0&&res.data){
        showSuccessToast("解散成功");
        location.reload()
    }else{
      showFailToast("解散失败,"+res.data.description);
    }
}
interface teamCardListProps {
    teamList: TeamType[];
}
const currentUser = ref();


const props = withDefaults(defineProps<teamCardListProps>(),{
    teamList:[] as TeamType[],
});
/**
 * 加入队伍
 * 
**/
const doJoinTeam = async  ()=>{
  if(!joinTeamId.value){
    return;
  }
   const res = await myAxios.post('/team/join',{
     teamId:joinTeamId.value,
     password:password.value,
    })
   
    if(res?.data.code === 0&&res.data){
        showSuccessToast("加入成功");
        location.reload()
    }else{
      showFailToast("加入失败,"+res.data.description);
    }
}
onMounted(async () => {
  currentUser.value=await getCurrentUser();
})

</script>

<style scoped>
#teamCardList :deep(.van-image__img){
  height: 128px;
  object-fit: unset;
}
</style>