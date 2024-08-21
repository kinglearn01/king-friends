<template>
    <van-card
    v-for="item in userList"
    :desc="item.profile?item.profile:'未设置'"
    :title="`${item.username?item.username:'未设置'} (${item.planetCode?item.planetCode:'未设置'})`"
    :thumb="item.avatarUrl?item.avatarUrl:'https://pub-cdn-oss.chuangkit.com/designTemplate/2022/02/09/6a432763-396f-4026-a878-56146bf675d3_thumb?v=1644400441000&x-oss-process=image/resize,w_600/sharpen,100/format,webp'"
  >
  <template #tags>
    <van-tag plain type="primary" v-for="tag in item?.tags" style="margin:8px 0 0 8px ;">{{tag?tag:'未设置'}}</van-tag>
  </template>
  <template #footer>
    <van-button size="mini">联系我</van-button>
  </template>
</van-card>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
import {UserType} from '../models/user'
interface userCardListProps {
    userList: UserType[];
}
const props = defineProps<userCardListProps>();
</script>

<style>

</style>