<template>
  <div id="app">
    <!-- 路由出口，匹配的组件将渲染在这里 -->
    <router-view />
  </div>
</template>

<script setup>
// 根组件，无需额外逻辑
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
}
</style>