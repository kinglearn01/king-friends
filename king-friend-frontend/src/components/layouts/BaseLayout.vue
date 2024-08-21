<template>
    <van-nav-bar
    :title="title"
    right-text="按钮"
    left-arrow
    @click-left="onClickLeft"
    @click-right="onClickRight"
  >
    <template #right>
      <van-icon name="search" size="18" />
    </template>
  </van-nav-bar>

  <div id="content">
    <router-view></router-view>
</div>

  <van-tabbar route>
    <van-tabbar-item to="/" icon="home-o" name="index">主页</van-tabbar-item>
    <van-tabbar-item to="/team" icon="search" name="team">队伍</van-tabbar-item>
    <van-tabbar-item to="/user" icon="friends-o" name="user">个人</van-tabbar-item>
  </van-tabbar>
  

</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Toast } from 'vant';
import Index from '../pages/Index.vue'
import Team from '../pages/Team.vue'
import {useRouter,useRoute} from 'vue-router'
import routes from '../../config/index'

const router = useRouter();

    const onClickLeft = () => {
      router.back()
    };
    const onClickRight = () => {
      router.push('/serach')
    };

    const active = ref(0);
    const onChange = (index) => Toast(`标签 ${index}`)
   const title=ref('');
router.beforeEach((to, from) => {
  if (to.meta && to.meta.title) {  
    // 假设你使用了一个方法来更新页面标题  
    title.value = to.meta.title;  
  }    

});
console.log()
</script>

<style>
#content{
  padding-bottom: 50px;
}
</style>

