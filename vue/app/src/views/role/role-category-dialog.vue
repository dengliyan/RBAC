<template>
    <div id="role-category">
        <el-dialog
                :title="formData.parentName+'('+formData.name+')'+'权限设置'"
                :visible.sync="UIDialog"
                top="0px"
                :close-on-click-modal="false" 
                :close-on-press-escape="false" 
                :before-close="close"
                width="700px"
                custom-class="full-dialog"
                >
                    <div style="position: absolute;left: 20px;top: 0;bottom: 0;right: 20px;overflow: auto;">
                        <el-tree
                            ref="tree"
                            show-checkbox
                            default-expand-all	
                            check-strictly	
                            node-key="id"
                            :props="{label: 'name',children: 'childrens'}"
                            :data="data"
                            :check-on-click-node="true"
                            :default-checked-keys="keys" >
                        </el-tree>
                    </div>
                <span slot="footer" class="dialog-footer" style="display:flex;justify-content: space-between;">
                    <div>
                       
                    </div>
                    <div>
                        <el-button type="text" @click="skip">下一步</el-button>
                        <el-button type="primary" @click="submit" :disabled="UISubmit" :loading="UISubmit">提 交</el-button>
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
        options:{
            type:Array,
            default:[]

        }
    },    
    watch: {
        value(item) {
            this.UIDialog = item.show;  
            this.formData=item.form;
            this.keys=item.keys;
        },
    },
    mounted(){
        if(this.value) {
            this.UIDialog = this.value.show;
            this.formData=this.value.form; 
            this.keys=this.value.keys;            
        };
        let that=this;
        that.$api.get('/api/auth/category/tree').then(response=>{
            that.data=response.data||[];
            if(that.data.length>0){
                that.data=that.data[0].childrens;
                for(var g in that.data){
                    that.data[g].disabled=true;
                }
            }
        });
    },
    data(){
        return {
            UIDialog:false,
            UISubmit:false,
            formData:{id:0,name:''},
            data: [],  
            keys:[]
        }
    },
    methods:{
        doSubmit(data){
            let that=this;
            that.$api.post('/api/auth/role/category',{id:this.formData.id,data:data.map(item=>item.id).join(',')}).then(response=>{
                if(response.ret!=0){
                    return that.$message.error(rep.msg||'保存失败');
                }                
                that.$message('保存成功');        
            });
        },
        skip(){

        },
        submit(){
            let nodes=this.$refs.tree.getCheckedNodes();
            if(nodes.length==0){
                this.$confirm('当前未选择，是否继续?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(() => {
                    this.doSubmit(nodes);
                }).catch(() => {});
            }else{
                 this.doSubmit(nodes);
            }            
        },
        close(done){
            this.$refs.tree.setCheckedKeys([]);
            done();
        }
    }
}
</script>


<style lang="less">

</style>