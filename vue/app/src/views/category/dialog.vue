<template>
    <div class="fixed-dialog">
        <el-dialog
            :title="formData.id==0?'新建分类':'修改分类'"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="720px">            
                <el-form   :model="formData" ref="formData" label-width="72px">
                    <el-form-item label="分类名称">
                        <el-input placeholder="分类名称" v-model="formData.name">
                             <template slot="append">
                                 <v-icon-picker v-model="formData.icon" />
                             </template>
                        </el-input>
                    </el-form-item>
                     <el-form-item label="分类描述">
                        <el-input type="textarea"  v-model="formData.description" placeholder="分类描述" :autosize="{ minRows: 2}"></el-input>
                    </el-form-item>
                    <el-form-item label="所属分类">
                         <el-cascader
                            v-model="formData.parents"
                            placeholder="所属分类"
                            :options="tree"
                            :props="{label: 'name',value:'id',children:'childrens'}"     
                            :change-on-select="true"
                            style="width:100%"
                        ></el-cascader>
                    </el-form-item>
 

                    <el-form-item label="权限设置">
                        <div style="display:flex" class="path-div">
                            <el-input placeholder="路径" v-model="formData.path" style="width:276px"></el-input>
                            <el-checkbox v-model="formData.childrenOnly">仅对子元素生效</el-checkbox>
                            <el-checkbox v-model="formData.inMenu">显示在菜单中</el-checkbox>
                        </div>
                    </el-form-item>

                    <el-form-item label="排序">
                        <v-date-time-picker :value="formData.rank" />
                    </el-form-item> 
                </el-form>
               
                <span slot="footer" class="dialog-footer">
                    <el-button v-if="formData.id>0&&formData.path&&formData.path.length>0" type="text" @click="next">下一步</el-button>
                    <el-button type="primary" @click="submit" :disabled="UISubmit" :loading="UISubmit">{{UISubmit?'提交中...':'确 定'}}</el-button>
                </span>
            </el-dialog>
    </div>
</template>

<script>
export default {
    components:{
        'v-date-time-picker':resolve => require(["@/components/date-time-picker.vue"], resolve),
        'v-icon-picker':resolve => require(["@/components/icon-picker.vue"], resolve)
    },
    props: {
        value: {
            type: Object,
            default: {}
        },
        propsTree: {
            type: Array,
            default: []
        }
    },
    mounted(){
        if(this.value) {
            this.UIDialog = this.value.show;
            this.formData=this.value.form;   
            this.dateTimePicker=this.formData.rank;
            //setTimeout(()=>{this.dateTimePicker=this.formData.rank;},10);              
        };
        if(this.propsTree){
            this.tree=this.propsTree;
        }
    },
    watch: {
        value(val) {
            this.UIDialog = val.show;
            this.formData=val.form; 
            this.dateTimePicker=this.formData.rank;
            //setTimeout(()=>{this.dateTimePicker=this.formData.rank;},10); 
        },
        propsTree(val){
            this.tree=val;
        }
    },
    data(){
        return {
            UIDialog:false,
            UISubmit:false,
            dateTimePicker:'',
            formData:{
                id:0,
                rank:'',
                name:'',
                description:'',
                parents:[1]
            },
            tree:[]
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
            delete data.parents;//删除无用的记录
            if(data.id==0){
                that.$api.post('/api/auth/category/add',data).then(response=>{
                    if(response.ret==0){
                        that.$message('添加成功');
                        that.$emit('on-load',that.formData);//回调成功事件
                        if(response.data.id>0&&response.data.path&&response.data.path.length>0){
                            this.$emit('on-next',response.data);            
                        }
                        setTimeout(() => {
                            this.UIDialog=false;
                        }, 200);
                    }else{
                        that.$message.error(response.msg||'添加失败');
                    }
                });
            }else{
                that.$api.post('/api/auth/category/update',data).then(response=>{
                    if(response.ret==0){
                        that.$message('修改成功');                        
                        that.$emit('on-load',that.formData);//回调成功事件
                        if(response.data.id>0&&response.data.path&&response.data.path.length>0){
                            this.$emit('on-next',response.data);            
                        }
                        setTimeout(() => {
                            this.UIDialog=false;
                        }, 200);
                    }else{
                        that.$message.error(response.msg||'修改失败');
                    }
                });
            }
        },
        next(){     
            this.$emit('on-next',this.formData);
            setTimeout(() => {
                this.UIDialog=false;
            }, 200);
        }
    }
}
</script>


<style lang="less">
.path-div{
    display: flex;
    justify-content:flex-start;
    .el-input+.el-checkbox{
        margin-left: 6px
    }
    .el-checkbox+.el-checkbox{
        margin-left: 6px
    }
}
</style>
