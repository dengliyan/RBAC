<template>
    <div id="fixed-dialog">
        <el-dialog :visible="true" 
                    :close-on-click-modal="false" 
                    :close-on-press-escape="false" 
                    :show-close="false"
                    title="系统登录"
                    width="480px"
                    top="0px">
            <el-form :model="formLogin">
                <el-form-item label="邮箱或手机号" label-width="120">
                    <el-input v-model="formLogin.name" 
                        auto-complete="off" 
                        placeholder="邮箱或手机号"
                        prefix-icon="fa fa-user"></el-input>
                </el-form-item>
                    <el-form-item label="密 码" label-width="120">
                    <el-input type="password" v-model="formLogin.password" 
                        auto-complete="off" 
                        placeholder="密码"
                        @keyup.enter.native="login"
                        prefix-icon="fa fa-lock"></el-input>
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
            formLogin:{
                name:'',
                password:''
            }
        };
    },
    methods:{
        login(){
            let that=this;
            this.$api.post('/api/user/login',that.formLogin).then(rep=>{
                if(rep.ret==0){//登录成功                     
                     that.$message('登录成功')                     
                }else{
                    that.$message.error(rep.msg||'登录失败');
                }
            },err=>{
                that.$message.error('登录失败');
            })
        }
    }
}
</script>



