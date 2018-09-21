<template>
    <div>
        <el-dialog
            :title="value.form.name+' 操作权限'"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            custom-class="full-dialog"
            :before-close="close"
            width="850px">


<div class="scroll">
            <el-form label-width="60px">
                <el-form-item
                    v-for="(p, index) in permissions"
                    :label="'操作' + (index+1)"
                    :key="p.id">
                        <div class="permission-item">
                            <el-input v-model="p.name" placeholder="名称" style="width:15em"></el-input>
                            <el-input v-model="p.path" placeholder="操作地址" style="width:20em">
                                 <template slot="prepend">{{value.form.path}}</template>
                            </el-input>
                            <el-input-number v-model="p.rank"  :min="0" :max="9" label="排序"></el-input-number>
                            <el-button @click="submit(index)" type="primary">保存</el-button>
                            <el-button @click="del(index)">删除</el-button>
                        </div>
                </el-form-item>
                <el-form-item>
                    <el-button @click="add">新增操作</el-button>
                </el-form-item>

            </el-form>
</div>


            
        </el-dialog>
    </div>
</template>

<script>
export default {
    props:{
        value:{
            type:Object,
            default:{
                show:false,
                form:{
                    name:''
                }
            }
        }
    },
    mounted(){
        if(this.value){
            this.UIDialog=this.value.show||false;  
        }
        if(this.value&&this.value.data){
            this.permissions=this.value.data;            
        }
        for(var i=this.permissions.length;i<8;i++){
                this.permissions.push({id:-1*i,name:'',path:'',rank:0})
        }
    },
    watch:{
        value(val){
            this.UIDialog=val.show;
            this.permissions=val.data;
            for(var i=this.permissions.length;i<8;i++){
                this.permissions.push({id:-1*i,name:'',path:'',rank:0})
            }
        }
    },
    data(){
        return {
            UIDialog:false,
            UISubmit:false,
            permissions:[]
        }
    },
    
    methods:{
        close(done){
            this.permissions=[];
            for(var i=0;i<8;i++){
                this.permissions.push({id:-1*i,name:'',path:'',rank:0})
            }
            done();
        },
        add(){
            let max=0;
            for(var g in this.permissions){
                max=Math.min(this.permissions[g].id,max)
            }
            this.permissions.push({id:max-1,name:'',path:'',rank:0})
        },
        doDel(idx){
            let item=this.permissions[idx];
            let that=this;
            that.$api.post('/api/auth/category/permission/delete',{category:that.value.form.id,id:item.id}).then(response=>{
                if(response.ret!=0){                    
                    return that.$message.error(response.msg||'删除失败')
                }
                let data=[];
                for(var i=0,len=that.permissions.length;i<len;i++){
                    if(i!=idx){
                        data.push(that.permissions[i]);
                    }
                }
                that.permissions=data;
            });
        },
        del(idx){
            let item=this.permissions[idx];
            if(item.id>0){
                this.$confirm('确定删除当前操作?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(() => {
                    this.doDel(idx);
                }).catch(() => {});
            }else{
                let data=[];
                for(var i=0,len=this.permissions.length;i<len;i++){
                    if(i!=idx){
                        data.push(this.permissions[i]);
                    }
                }
                this.permissions=data;
            }
        },
        
        submit(idx){
            let item=this.permissions[idx];
            if(!(item.name&&item.name.length>0)){
                return this.$message.error('请输入名称')
            }
            if(!(item.path&&item.path.length>0)){
                return this.$message.error('请输入操作地址')
            }
            item.category=this.value.form.id;
            let that=this;
            that.$api.post('/api/auth/category/permission',item).then(response=>{
                if(response.ret!=0){
                    
                    return that.$message.error(response.msg||'操作失败')
                }
                item.id=response.data.id;//更新本地记录
                console.log(this.permissions)
                return that.$message('保存成功')
            });
        }
    }
}
</script>

<style>

.permission-item{display: flex;}
.permission-item .el-input,.permission-item .el-input-number{margin-right: 6px}
</style>
