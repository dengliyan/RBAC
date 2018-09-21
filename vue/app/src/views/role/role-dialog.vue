<template>
    <el-dialog
            :title="formData.parent>0?'分组':'角色'"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="600px">
            <el-form v-model="formData" label-width="6em">
                     
                    <el-form-item :label="formData.parent==0?'角色名':'分组名'">
                        <el-input  v-model="formData.name" placeholder="名称" >                            
                            <el-button  type="danger" 
                                        slot="append" 
                                        icon="fa fa-ban" 
                                        v-if="formData.id>0&&formData.parent==0"
                                        @click="del"
                                        style="color: #fff;background-color:#f56c6c;border-color: #f56c6c;"> 删除角色</el-button>
                        </el-input>
                    </el-form-item>
                    
                    <el-form-item label="描述">
                        <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="描述" >
                        </el-input>
                    </el-form-item>

                    <el-form-item label="所属角色"  v-show="formData.parent>0">
                        <el-select :disabled="formData.parent>0" v-model="formData.parent" placeholder="请选择">
                            <el-option key="0" label="请选择" :value="0" />
                            <el-option
                                v-for="item in options"
                                
                                :key="item.id"
                                :label="item.name"
                                :value="item.id" />
                        </el-select>
                    </el-form-item>
                    
            </el-form>


            <span slot="footer" class="dialog-footer" style="display:flex;justify-content: space-between;">
                    <div></div>
                    <div>
                        <el-button type="text" @click="skip" v-if="formData.id>0&&formData.parent>0">下一步</el-button>
                        <el-button type="primary" @click="submit" :disabled="UISubmit" :loading="UISubmit">提交</el-button>
                    </div>
                </span>

    </el-dialog>      
</template>

<script>

export default { 
    components:{        
    },   
    props: {
        value:{
            type:Object,
            default:{}
        },
        options:{
            type:Array,
            default:[]

        }
    },    
    watch: {
        value(item) {
            this.UIDialog = item.show;  
            this.formData=item.form;            
        },
    },
    mounted(){
        if(this.value) {
            this.UIDialog = this.value.show;
            this.formData=this.value.form;              
        };
    },
    data(){
        return {
            UIDialog:false,
            UISubmit:false,
            formData:{
                id:0,
                name:'',
                description:'',
                parent:0
            },
        }
    },
    methods:{
        del(){
            let that=this;
            that.$confirm('删除角色"'+that.formData.name+'"，是否继续?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(() => {
                that.$api.post('/api/auth/role/delete',{id:that.formData.id}).then(response=>{
                    if(response.ret!=0){
                        return that.$message.error(rep.msg||'删除失败');
                    }
                    that.$message('删除成功');
                    that.UIDialog=false;
                    that.$emit('on-load');
                });
            }).catch(() => {});
        },
        skip(){
            this.$emit('on-next',this.formData);  
            setTimeout(() => {
                this.UIDialog=false;       
            }, 200);
        },
        submit(){
            let that=this;
            that.UISubmit=true;    
            console.log(that.formData)
            if(that.formData.id==0){
                that.$api.post('/api/auth/role/add',that.formData).then(response=>{
                    that.UISubmit=false;
                    if(response.ret==0){
                        setTimeout(() => {that.UIDialog=false;}, 200);
                        that.$emit('on-load');
                        that.$emit('on-next',response.data);
                    }else{
                        that.$message.error(response.msg||'添加失败');
                    }
                })
            }else{
                that.$api.post('/api/auth/role/update',that.formData).then(response=>{
                    that.UISubmit=false;
                    if(response.ret==0){
                        setTimeout(() => {that.UIDialog=false;}, 200);
                        that.$emit('on-load');
                        if(response.data.parent>0){//跳转下一步
                            that.$emit('on-next',that.formData);
                        }
                    }else{
                        that.$message.error(response.msg||'修改失败');
                    }
                })
            }
        }
    }
}
</script>

<style>
</style>