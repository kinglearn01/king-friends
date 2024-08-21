<template>
    <form action="/">
        <van-search
          v-model="searchTest"
          show-action
          placeholder="请输入搜索标签"
          @search="onSearch"
          @cancel="onCancel"
          autofocus
        />
      </form>
      <van-divider content-position="left">已选标签</van-divider>
      <div v-if="activeIds.length === 0">请选择标签</div>
      <van-row gutter="16" style="padding: 0 16px">
        <van-col v-for="tag in activeIds">
        <van-tag closeable size="small" type="primary" @close="doClose(tag)">
            {{tag }}
        </van-tag>
        </van-col>
     </van-row>
      <van-divider content-position="left">选择标签</van-divider>
      <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex" :items="tagList"
      />
    <div style="padding:12px;">
      <van-button type="primary" block @click="doSerachResult">搜索</van-button>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { showToast } from 'vant';
import { useRouter } from 'vue-router';
const searchTest = ref('');
let originList = [
      {
        text: '性别',
        children: [
          { text: '男', id:'男' },
          { text: '女', id: '女' },
        ],
      },
      {
        text: '年级',
        children: [
          { text: '大一', id: '大一'},
          { text: '大二', id: '大二'},
          { text: '大三', id: '大三'},
          { text: '大四', id: '大四'},
        ],
      },
    ];
    let tagList=ref(originList);
const onSearch = (val) => {
    tagList.value =   originList.map(parentTag=>{
        const tempChildren = [...parentTag.children];
        const tempParentTag ={...parentTag};
        tempParentTag.children=tempChildren.filter(item=>item.text.includes(searchTest.value))
        return tempParentTag;
       });    
       
};
const onCancel = () => {
    searchTest.value='';
    tagList.value=originList
};
const activeIds = ref([]);
    const activeIndex = ref(0);
     const doClose=(tag)=>{
        activeIds.value =  activeIds.value.filter(items=>{
            return item !==tag;
        })
    }
    const router = useRouter();
const doSerachResult=()=>{
    router.push({
      path:'/user/list',
      query:{
        tags:activeIds.value
      }
    })
}


</script>

<style>

</style>