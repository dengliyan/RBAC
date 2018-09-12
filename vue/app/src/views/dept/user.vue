<template>
    <div>
        <el-dialog
            title="添加人员"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="600px">                 
                <el-form :model="formData"   label-width="6em">
                    <el-form-item label="用户姓名">
                        <el-input v-model="formData.name" placeholder="用户姓名" ></el-input>
                    </el-form-item>
                    
                    <el-form-item label="邮箱">
                        <el-input v-model="formData.email" placeholder="邮箱" ></el-input>
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input v-model="formData.phone" placeholder="手机号" ></el-input>
                    </el-form-item>
                    <el-form-item label="所属部门">
                         <el-cascader
                            v-model="formData.dept"
                            placeholder="选择部门"
                            :options="tree"
                            :props="{label: 'name',value:'id',children:'childrens'}"     
                            :change-on-select="true"
                            style="width:100%"
                        ></el-cascader>
                    </el-form-item>
                    <el-form-item label="初始密码">
                        <el-input type="password" v-model="formData.password" placeholder="初始密码" ></el-input>
                    </el-form-item>
                    <el-form-item label="重复密码">
                        <el-input type="password" v-model="formData.password2" placeholder="重复密码" ></el-input>
                    </el-form-item>
                </el-form>

                <span slot="footer" class="dialog-footer">
                    <el-button @click="UIDialog=false">取 消</el-button>
                    <el-button type="primary" @click="submit" :disabled="UISubmit" :loading="UISubmit">{{UISubmit?'提交中...':'确 定'}}</el-button>
                </span>
            </el-dialog>
    </div>
</template>

<script>
export default {    
    props: {
        value:{
            type:Object,
            default:{}
        },
        propsUIDialog:{
            type:Boolean,
            default:false
        },
        tree:{
            type:Array,
            default:[]
        },
    },
    mounted(){

        if(this.value){
            if(this.value.show!=null&&this.value.show!=undefined){
                this.UIDialog = this.value.show;
            }
            if(this.value.form!=null&&this.value.form!=undefined){
                this.formData=this.value.form;     
            }
        }
        //初始化
        this.formData.name='';
        this.formData.email='';
        this.formData.phone='';
        this.formData.password='';
        this.formData.password2='';
    },
    watch: {
        value(item) {
            this.UIDialog = item.show;
            this.formData=item.form;   
            this.formData.name='';
            this.formData.email='';
            this.formData.phone='';
            this.formData.password='';
            this.formData.password2='';                    
        },
        UIDialog(val){
            this.$emit('sync-dialog',val);
        },
    },
    data(){
        return {
            UIDialog:false,
            UISubmit:false, 
            formData:{},
        }
    },
    methods:{
        submit(){
            let that=this;
            let data={};
            for (var key in that.formData) {
                data[key]=that.formData[key];
            } 
            data.dept= data.dept[data.dept.length-1];   
            that.$api.post('/api/auth/user/add',data).then(response=>{
                if(rep.ret==0){
                    that.$message('添加成功');
                    that.UIDialog=false;
                    that.$emit('on-add',{response:response.data,form:that.formData});                
                }else{
                    that.$message.error(rep.msg||'登录失败');
                }
            });
        },
    }
}
</script>

<style lang="less">


</style>
