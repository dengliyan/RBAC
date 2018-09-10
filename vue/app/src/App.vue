<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style lang="less">
.fixed-dialog .el-dialog__wrapper{
    display: flex;
    justify-content:center;
    align-items: center;    
    .el-dialog__title{font-weight: bold}
    .el-dialog{margin-bottom: 0}
    .el-dialog__body{padding-top:0;padding-bottom: 0}
}
</style>
