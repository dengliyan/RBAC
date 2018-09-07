<template>
    <div class="login-dialog">
        <el-dialog :visible="dialogFormVisible" 
                    :close-on-click-modal="false" 
                    :close-on-press-escape="false" 
                    :show-close="false"
                    title="系统登录"
                    width="500px"
                    top="50px">
            <el-form :model="loginForm">
                <el-form-item label="用户名" :label-width="formLabelWidth">
                    <el-input v-model="loginForm.userName" auto-complete="off"></el-input>
                </el-form-item>
                    <el-form-item label="密 码" :label-width="formLabelWidth">
                    <el-input type="password" v-model="loginForm.userPwd" auto-complete="off" @keyup.enter.native="login"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="login">登　录</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data(){
        return {
            dialogFormVisible:true,
            formLabelWidth:120,
            loginForm:{
                userName:'',
                userPwd:''
            }
        };
    }
}
</script>

<style <style scoped>
.login-dialog .el-dialog__wrapper{
    display: flex;
    justify-content:center;
    align-items: center;    
}
</style>

