<template>
    <div>
        <div style="margin-bottom: 10px">
            <el-button type="primary" @click="add(0)">添加角色</el-button>
        </div>
        <v-role-dialog :value="roleDialog" :options="data" @on-load="getData" @on-next="step2" />
        <v-role-category-dialog :value="roleCategoryDialog" :options="[]" />  
        <el-table
            :data="data"
            border
            stripe
            style="width: 100%">
                <el-table-column
                    prop="name"
                    label="角色名"
                    width="200">
                    <template slot-scope="scope">
                        <el-button type="text" @click="edit(scope.row.id)"> {{scope.row.name}}</el-button>
                    </template>
                </el-table-column>

                <el-table-column
                    prop="address"
                    label="权限分组列表">
                        <template slot-scope="scope">
                            <div class="group-tags">
                                <el-tag size="medium" @click.native="edit(g.id)" v-for="(g,idx) in scope.row.groups" :closable="idx>0" :type="idx==0?'danger':'success'" @close="del(g)">{{g.name}}</el-tag>
                                <el-tag size="medium" @click.native="add(scope.row.id)" type="info">+分组</el-tag>
                            </div>
                        </template>
                </el-table-column>
        </el-table>
    </div>
</template>
<script>
export default {
    components:{
        'v-role-dialog':resolve => require(["./role-dialog.vue"], resolve),
        'v-role-category-dialog':resolve => require(["./role-category-dialog.vue"], resolve),
        'v-role-permission-dialog':resolve => require(["./role-permission-dialog"], resolve),
    },
    data(){
        return {
            roleDialog:{
                show:false,
                form:{},
            },
            roleCategoryDialog:{
                show:false,
                form:{id:0,name:''}
            },
            data:[]
        }
    },
    mounted(){
        let that=this;
        that.getData();
    },
    methods:{
        add(parent){           
            this.roleDialog={
                show:true,
                form:{
                    id:0,
                    name:'',
                    description:'',
                    parent:parent
                }
            }
        },
        edit(id){
            let that=this;
            that.$api.get('/api/auth/role?id='+id).then(response=>{
                if(response.ret==0){
                    that.roleDialog={
                        show:true,
                        form:response.data
                    }
                }
            });
        },
        step2(data){
            let that=this;
            that.$api.get('/api/auth/role/category?id='+data.id).then(response=>{    
                if(response.ret!=0){
                    that.$message.error(response.msg||'加载分类失败');
                }
                that.roleCategoryDialog={show:true,form:data,keys:response.data||[]};                
            });
        },
        del(data){
            let that=this;
            that.$confirm('删除分组"'+data.name+'"，是否继续?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(() => {
                that.$api.post('/api/auth/role/delete',{id:data.id}).then(response=>{
                    if(response.ret!=0){
                        return that.$message.error(rep.msg||'删除失败');
                    }
                    that.$message('删除成功');
                    that.getData();
                });
            }).catch(() => {});
        },
        getData(){
            let that=this;
            that.$api.get('/api/auth/role').then(response=>{
                that.data=response.ret==0?response.data||[]:[];
            });
        }
    }
}
</script>

<style>
.group-tags span{
    cursor: pointer;
    margin-right:12px;
    
}
</style>