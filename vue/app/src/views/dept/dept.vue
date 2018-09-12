<template>
    <div class="fixed-dialog">
        <el-dialog
            :title="formData.id==0?'新建部门':'修改部门'"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="600px">            
                <el-form :model="formData" ref="formData" label-width="72px">
                    <el-form-item label="部分名称">
                        <el-input placeholder="部分名称" v-model="formData.name"></el-input>
                    </el-form-item>
                    <el-form-item label="上级部门">
                         <el-cascader
                            v-model="formData.parents"
                            placeholder="上级部门"
                            :options="tree"
                            :props="{label: 'name',value:'id',children:'childrens'}"     
                            :change-on-select="true"
                            style="width:100%"
                        ></el-cascader>
                    </el-form-item>

                    <el-form-item label="排序">
                        <v-date-time-picker v-model="formData.rank"  />
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
    components:{
        'v-date-time-picker':resolve => require(["@/components/date-time-picker.vue"], resolve)
    },
    mounted() {
        if(this.value){
            if(this.value.show!=null&&this.value.show!=undefined){
                this.UIDialog = this.value.show;
            }
            if(this.value.form!=null&&this.value.form!=undefined){
                this.formData=this.value.form;     
            }              
        }
    },
    props: {
        value:{
            type:Object,
            default:{}
        },
        tree:{
            type:Array,
            default:[]
        }
    },
    watch: {
        value(item){                    
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
            dateTimePicker:'',
            formData:{
                parents:[1]
            },
        }
    },
    methods:{
        submit(){            
            let that=this;
            let data={}
            for(var g in that.formData){
                data[g]=that.formData[g];
            }
            data.pid=data.parents[data.parents.length-1];            
            if(data.id==0){
                that.$api.post('/api/auth/dept/add',data).then(response=>{
                    if(response.ret==0){
                        that.$message('添加成功');
                        that.UIDialog=false;
                        that.$emit('on-add',{response:response.data,form:that.formData});//回调成功事件
                    }else{
                        that.$message.error(response.msg||'添加失败');
                    }
                });
            }else{
                that.$api.post('/api/auth/dept/update',data).then(response=>{
                    if(response.ret==0){
                        that.$message('修改成功');
                        that.UIDialog=false;
                        that.$emit('on-edit',{response:response.data,form:that.formData});//回调成功事件
                    }else{
                        that.$message.error(response.msg||'修改失败');
                    }
                });
            }
        },
       /* syncPicker(val){
            this.formData.rank=val;
        }*/
    }
}
</script>

<style>

</style>
