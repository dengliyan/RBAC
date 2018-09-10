<template>
    <div class="fixed-dialog">
        <el-dialog
            :title="formData.id==0?'新建分类':'修改分类'"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="600px"
                        >            
                <el-form   :model="formData" ref="formData" label-width="72px">
                    <el-form-item label="分类名称">
                        <el-input placeholder="分类名称" v-model="formData.name"></el-input>
                    </el-form-item>
                     <el-form-item label="分类描述">
                        <el-input type="textarea"  v-model="formData.description" placeholder="分类描述" :autosize="{ minRows: 3}"></el-input>
                    </el-form-item>
                    <el-form-item label="所属分类">
                         <el-cascader
                            v-model="formData.parents"
                            placeholder="所属分类"
                            :options="tree"
                            :props="{label: 'name',value:'id'}"     
                            :change-on-select="true"
                            style="width:100%"
                        ></el-cascader>
                    </el-form-item>

                    <el-form-item label="排序">
                        <v-date-time-picker :propsValue="dateTimePicker" @sync-picker="syncPicker" />
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
import picker from '@/views/components/date-time-picker.vue'
export default {
    components:{
        'v-date-time-picker':picker
    },
    props: {
        propsUIDialog:{
            type:Boolean,
            default:false
        },
        propsForm: {
            type: Object,
            default: {}
        },
        propsTree: {
            type: Array,
            default: []
        }
    },
    watch: {
        propsUIDialog(val) {
            this.UIDialog = val;//②监听外部对props属性的变更，并同步到组件内的data属性中                 
        },
        UIDialog(val){
            this.$emit('sync-dialog',val);
        },
        propsForm(val){            
            this.formData=val;
            setTimeout(()=>{this.dateTimePicker=this.formData.rank;},10);
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
            let _this=this;
            let data={}
            for(var g in _this.formData){
                data[g]=_this.formData[g];
            }
            data.pid=data.parents[data.parents.length-1];
            //如果是添加
            if(data.id==0){
                _this.$api.post('/api/auth/category/add',data).then(response=>{
                    if(response.ret==0){
                        _this.$message('添加成功');
                        _this.UIDialog=false;
                        _this.$emit('on-add',{response:response.data,form:_this.formData});//回调成功事件
                    }else{
                        _this.$message.error(response.msg||'添加失败');
                    }
                });
            }
        },
        syncPicker(val){
            this.formData.rank=val;
        }
    }
}
</script>

<style>

</style>
