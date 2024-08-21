<template>
  <van-tabs v-model:active="active" type="card" @click-tab="onClickTab">
    <van-tab title="随机模式">
      <user-card-list :user-list="userList"></user-card-list>
      <van-empty v-if="!userList || userList.length<1" description="数据为空"></van-empty>
    </van-tab>
    <van-tab title="匹配模式">
      <user-card-list :user-list="matchList"></user-card-list>
      <van-empty v-if="!matchList || matchList.length<1" description="数据为空"/>
    </van-tab>
  </van-tabs>

</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import myAxios from '../../plugins/myAxios';
import UserCardList from '../UserCardList.vue';

const route = useRoute();
const searchTest = ref('');
const {tags} = route.query;
const userList = ref([]);
const active=true
onMounted(async () => {
  const userListData = await myAxios.get('/user/recommend', {
    params: {
      pageSize: 20,
      pageNum: 1
    }
  })
      .then(function (response) {
        return response?.data?.data.records;
      })
      .catch(function (error) {
      })
  if (userListData) {
    userListData.forEach(user => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags)
      }
    })
    userList.value = userListData;
  }
});
const matchList = ref([]);
const domatch = async () => {
  const num = 10;
  const matchListData = await myAxios.get('/user/match', {
    params: {
      num,
    }
  })
      .then(function (response) {
        return response?.data?.data;
      })
      .catch(function (error) {
      })
  if (matchListData) {
    matchListData.forEach(user => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags)
      }
    })
    matchList.value = matchListData;
  }
}
const onClickTab = ({title}) => {
  if (title === "随机模式") {

  }
  if (title === "匹配模式") {
    domatch()

  }
}
</script>

<style>

</style>