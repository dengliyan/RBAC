<template>
    <div id="category-tree">
        <v-dialog 
                   :value="categoryForm"
                   :propsTree="tree"
                   @on-load="after"
                   @on-next="next" />
        <v-permission-dialog :value="permissionDialog" />
        <el-tree 
            v-if="tree!=null" 
            node-key="id" 
            highlight-current
            :data="tree" 
            :default-expanded-keys="props.tree.keys" 
            :props="{children: 'childrens',label: 'name'}" 
            :expand-on-click-node="false"
            :render-content="renderContent"	 />
    </div>
</template>

<script>
import util from '@/config/date-util.js'
export default {
    components:{
        'v-dialog':resolve => require(["./dialog.vue"], resolve),
        'v-permission-dialog':resolve => require(["./permission-dialog.vue"], resolve),
    },
    data(){
        return {
            tree:null,
            categoryForm:{
                show:false,
                form:{}
            },
            permissionDialog:{
                show:false,
                form:{}
            },
            props:{
                tree:{
                    keys:[0],
                }
            },
        }
    },
    mounted(){
        this.getData();
    },    
    methods:{
        getData(callback){
            let that=this;
            that.$api.get('/api/auth/category/tree').then(response=>{
                if(response.ret==0){
                    that.tree=[{id:0,name:'全部模块',parent:-1,childrens:response.data}];  
                    callback&&callback();
                }else{
                    that.message.error('读取失败，请重试。')
                }
            });
        },
        renderContent(h, { node, data, store }) {        
            let edit=data.id==0;
            let remove=(data.id==0)||(data.childrens!=null&&data.childrens.length>0);
            return (
            <span class="custom-tree-node">
                <span><i class={node.icon}></i>{node.label}({data.id})</span>
                <span>
                    <el-button-group> 
                        <el-button size="mini" icon="fa fa-plus-square" circle type="text" on-click={ () => this.add(data) }></el-button>
                        <el-button size="mini" icon="fa fa-edit" circle type="text" on-click={ () => this.edit(data) } disabled={edit}></el-button>
                        <el-button size="mini" icon="fa fa-times-circle" circle  type="text" on-click={ () => this.del(node, data) } disabled={remove}></el-button>
                    </el-button-group> 
                </span>
            </span>);
        },
        //添加
        add(data){
            let that=this;
            if(data.id==0){
                that.categoryForm={show:true,form:{
                    id:0,
                    name:'',
                    description:'',
                    parents:[0],
                    rank:util.now("yyyy-MM-dd")+" "+"00:00:00",
                    childrenOnly:false,
                    inMenu:false
                }};
                return;
            }
            that.$api.get('/api/auth/category?id='+data.id).then(response=>{
                if(response.ret==0){
                    let parents=([response.data.id].concat(response.data.parents||[]));//拼接
                    parents.push(0);//添加顶级
                    parents.reverse();//反转 
                    that.categoryForm={show:true,form:{
                        id:0,
                        name:'',
                        description:'',
                        parents:parents,rank:util.now("yyyy-MM-dd")+" "+"00:00:00",
                        childrenOnly:false,
                        inMenu:false
                    }};
                }
            });
            
        },
        after(param){
            let that=this;
            this.getData(function () {
               that.props.tree.keys=param.parents;//展开
            });
        },
        edit(data){ 
            let that=this;
            that.$api.get('/api/auth/category?id='+data.id).then(response=>{
                if(response.ret==0){
                    let item=response.data;
                    let parents=item.parents||[];//拼接
                    parents.push(0);//添加顶级
                    parents.reverse();//反转 
                    let data=response.data;
                    data.parents=parents;
                    data.rank=util.format(new Date(data.rank),'yyyy-MM-dd hh:mm:ss');
                    that.categoryForm={show:true,form:data};
                }
            });
        },
        del(node, data){
            let that=this;
            that.$confirm('删除分类 '+data.name+', 是否继续?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消', type: 'warning'}).then(() => {
                that.$api.post('/api/auth/category/delete',{id:data.id}).then(response=>{
                    if(response.ret==0){
                        that.$message('已删除');
                        that.getData(function () {
                            that.props.tree.keys=data.parents;//展开
                        });
                    }else{
                        that.$message.error(response.msg||'删除失败');
                    }
                });
            }).catch(() => {

            });
        },
        next(data){
            let that=this;
            that.$api.get('/api/auth/category/permission',{category:data.id}).then(response=>{
                if(response.ret==0){
                    this.permissionDialog={show:true,form:data,data:response.data};
                }else{
                   that.$message.error(response.msg||'读取失败');
                }
            });
            
        }
    },
}
</script>

<style lang="less">
#category-tree{
    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }
    .custom-tree-node span:nth-of-type(2) {
        display: none;
        width: 50%;
        text-align: right;
        justify-content:flex-end
    }
    
    .el-tree-node__content{height: 30px ;line-height: 30px;}
    .el-tree-node__expand-icon + .custom-tree-node span:nth-of-type(1){color: #000}
    .el-tree-node__expand-icon + .custom-tree-node.tree-user span:nth-of-type(1){color: #2B51AE;}
    .el-tree-node__expand-icon + .custom-tree-node span:nth-of-type(2) .fa{font-size: 24px}
    .el-tree-node__expand-icon.expanded + .custom-tree-node .fa-folder:before{content: "\f07c";}

    .custom-tree-node:hover span:nth-of-type(2),
    .el-tree-node.is-current >.el-tree-node__content .custom-tree-node span:nth-of-type(2) {
        display: flex;
        
    }
}
</style>
