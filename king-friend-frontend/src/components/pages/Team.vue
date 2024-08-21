<template>
    <div id="teamPage">
        <van-search v-model="searchText" @search="onSearch" placeholder="请输入队伍名称" />
        <van-tabs v-model:active="active" @change="onTabChange">
            <van-tab title="公开" name="public" />
            <van-tab title="加密" name="private" />
          </van-tabs>
          
        <team-card-list :team-list="teamList"/>
        <van-button icon="plus" round type="primary" class="add-button" to="/team/add" />
        <van-empty v-if="!teamList || teamList.length<1" description="数据为空"></van-empty>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ref,onMounted } from 'vue';
import myAxios from '../../plugins/myAxios';
import TeamCardList from '../TeamCardList.vue'
import { showFailToast, showSuccessToast } from 'vant';
 const router =useRouter();
 const active = ref('public');
//跳转到加入队伍页面
const doJoinTeam = ()=>{
    router.push("/team/add")
}
const searchText = ref('');
const teamList = ref([]);
const onTabChange =(name)=>{
    if(name==='public'){
        listTeam(searchText.value,0);
    }else{
        listTeam(searchText.value,2);
    }
    
}

const listTeam = async(val='',status)=>{
    const res = await myAxios.get("/team/list",{
        params:{
            searchText:val,
            page:1,
            status,
        }
    });
    if(res?.data.code == 0 && res.data){
        teamList.value=res.data.data
    }else{
        showFailToast("加载队伍失败");
    }
}
onMounted(async () => {
   listTeam()
})
const onSearch = (val) => {
    listTeam(val)
}
</script>

<style scoped>
.add-button{
    position: fixed;
    bottom: 80px;
    right: 12px;
    border-radius: 50%;
}
</style>