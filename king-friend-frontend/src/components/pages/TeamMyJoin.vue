<template>
    <div id="teamPage">
        <van-search v-model="searchText" @search="onSearch" placeholder="请输入队伍名称" />
        <team-card-list :team-list="teamList"/>
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
//跳转到加入队伍页面
const doJoinTeam = ()=>{
    router.push("/team/add")
}
const searchText = ref('');
const teamList = ref([]);

const listTeam = async(val='')=>{
    const res = await myAxios.get("/team/list/my/join",{
        params:{
            searchText:val,
            page:1
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

<style>

</style>