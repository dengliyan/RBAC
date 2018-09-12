<template>
    <div>
        <el-dialog
            :title="formData.name"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="600px">                 
                <el-form :model="formData"   label-width="6em">
                    <el-form-item label="用户姓名">
                        <el-input v-model="formData.name" placeholder="用户姓名" >                            
                            <el-button type="danger" slot="append" icon="fa fa-ban" style="color: #fff;background-color:#f56c6c;border-color: #f56c6c;"> 删除用户</el-button>
                        </el-input>
                    </el-form-item>
                    
                    <el-form-item label="邮箱">
                        <el-input v-model="formData.email" placeholder="邮箱" >
                            <el-button slot="append" icon="fa fa-envelope"> 密码重置</el-button>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input v-model="formData.mobile" placeholder="手机号" :maxlength="11"></el-input>
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
                    <el-form-item label="当前角色">
                        <el-select v-model="formData.role" multiple placeholder="请选择" style="width:100%">
                            <el-option
                                v-for="item in role"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>

                <span slot="footer" class="dialog-footer" style="display:flex;justify-content: space-between;">
                    <div>
                        <el-button-group>
                            
                        </el-button-group>  
                    </div>
                    <div>
                        <el-button type="primary" @click="submit" :disabled="UISubmit" :loading="UISubmit">修改信息</el-button>
                    </div>
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
    },
    watch: {
        value(item) {
            this.UIDialog = item.show;
            this.formData=item.form;              
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
            role:[{
                    value: '管理员',
                    label: '管理员'
                },
                {
                    value: '主编',
                    label: '主编'
                }]
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
            that.$api.post('/api/auth/user/regist',data).then(response=>{
                if(response.ret==0){
                    that.$message('添加成功');
                    that.UIDialog=false;
                    that.$emit('on-add',{response:response.data,form:that.formData});                
                }else{
                    that.$message.error(response.msg||'添加失败');
                }
            });
        },
    }
}
</script>

<style lang="less">


</style>
