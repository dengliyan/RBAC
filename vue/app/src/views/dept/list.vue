<template>
    <div id="category-tree">
        <v-dept-dialog 
                   :value="deptDialog"
                   :tree="tree.data"
                   @sync-dialog="syncDialog"
                   @on-add="afterAdd"
                   @on-edit="afterEdit" />

        <v-user-dialog
            :value="userDialog" 
            :tree="tree.data"
            @sync-dialog="syncDialog2" />

        <v-user-detail-dialog
            :value="userDetailDialog" 
            :tree="tree.data" />

        <el-tree  
            node-key="id" 
            highlight-current
            :data="tree.structure" 
            :default-expanded-keys="tree.keys" 
            :props="tree.defaultProps" 
            :expand-on-click-node="true"
            :render-content="renderContent"	 />
    </div>
</template>

<script>
 import util from '@/config/date-util.js'
export default {
    components:{
        'v-dept-dialog':resolve => require(["./dept.vue"], resolve),
        'v-user-dialog':resolve => require(["./user.vue"], resolve),
        'v-user-detail-dialog':resolve => require(["./user_detail.vue"], resolve),
    },
    data(){
        return {
            tree:{
                data:[],
                keys:['0'], 
                structure:[{id:'0',name:'组织架构',childrens:[]}],
                defaultProps:{
                    children: 'childrens',
                    label: 'name'
                },
            },
            deptDialog:{ 
                show:false,
                form:{}
            },
            userDialog:{
                show:false,
                form:{},
            },
            userDetailDialog:{
                show:false,
                form:{},
            }
            
        }
    },
    mounted(){
        this.getData();
    },    
    methods:{
        getData(callback){
            let that=this;
            that.$api.get('/api/auth/dept/structure').then(response=>{
                if(response.ret==0){
                    that.tree.structure=[{id:'0',name:'组织架构',childrens:response.data}];  
                    callback&&callback();
                }else{
                    that.message.error('读取失败，请重试。')
                }
            });
            that.$api.get('/api/auth/dept/tree').then(response=>{
                if(response.ret==0){
                    that.tree.data=[{id:0,name:'部门列表',childrens:response.data}];              
                }
            });
        },
        syncDialog(val){
            this.formDialog={show:false,form:null};
        },
        syncDialog2(val){
            this.userDialog.show=val;
        },
        //添加
        add(data,e){
            if(data.id<0){
                return;
            }
            let that=this;
            if(data.id==0){
                let parents=[data.id];
                that.deptDialog={show:true,form:{id:0,name:'',description:'',parents:parents,rank:util.now("yyyy-MM-dd")+" "+"00:00:00"}};
            }else{
                that.$api.get('/api/auth/dept?id='+data.id).then(response=>{
                    if(response.ret==0){
                        let parents=([response.data.id].concat(response.data.parents||[]));//拼接
                        parents.push(0);//
                        parents.reverse();//反转 
                        that.deptDialog={show:true,form:{show:true,id:0,name:'',description:'',parents:parents,rank:util.now("yyyy-MM-dd")+" "+"00:00:00"}};
                    }
                });
            }
        },
        afterAdd(param){
            let that=this;
            this.getData(function () {  
               that.tree.keys=param.form.parents.map(item=>item.toString());//展开
            });
        },
        edit(data,e){ 
            let that=this;            
            that.$api.get('/api/auth/dept?id='+data.id).then(response=>{
                if(response.ret==0){
                    let item=response.data;
                    let parents=item.parents||[];//拼接
                    parents.push(0);
                    parents.reverse();//反转 
                    that.deptDialog={show:true,form:{id:item.id,name:item.name,description:item.description,parents:parents,rank:util.format(new Date(item.rank),'yyyy-MM-dd hh:mm:ss')}}
                };
            });
        },
        afterEdit(param){
            let that=this;
            this.getData(function () {
               that.tree.keys=param.form.parents.map(item=>item.toString()).concat(param.form.id+'');//展开
            });
        },
        del(node, data,e){
            let that=this;
            that.$confirm('删除分类 '+data.name+', 是否继续?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消', type: 'warning'}).then(() => {
                that.$api.post('/api/auth/dept/delete',{id:data.id}).then(response=>{
                    if(response.ret==0){
                        that.$message('已删除');
                        that.getData(function () {
                            that.tree.keys=data.parents.map(item=>item.toString());//展开
                        });
                    }else{
                        that.$message.error(response.msg||'删除失败');
                    }
                });
            }).catch(() => {

            });
        },
        user(data,e){ console.log(data)                     
            let that=this;
            that.$api.get('/api/auth/dept?id='+data.value).then(response=>{
                if(response.ret==0){
                    let parents=([response.data.id].concat(response.data.parents||[]));//拼接
                    parents.push(0);//
                    parents.reverse();//反转 
                    this.userDialog={show:true,form:{ dept:parents}};
                }
            });
        },
        show(data,e){
            let that=this;
            if(data==null||!data.user){
                return;
            }
            that.$api.get('/api/auth/user?id='+data.value).then(response=>{
                if(response.ret==0){
                    this.userDetailDialog={show:true,form:{
                        id:response.data.id,
                        name:response.data.name,
                        email:response.data.email,
                        mobile:response.data.mobile, 
                        dept:[],
                        role:[]
                    }};
                }
            });
        }
        ,        
        renderContent(h, { node, data, store }) { 
            let edit=data.id<=0;
            let remove=(data.id<=0)||(data.childrens!=null&&data.childrens.length>0);
            if(data.user){
                return (<span  on-click={ (e) => this.show(data,e) } class="custom-tree-node tree-user">
                    <span><i class="fa fa-user" /> {node.label}</span>
                    <span></span>
                </span>);
            }
            return (
                <span  class="custom-tree-node">
                    <span><i class={data.id==0?'fa fa-sitemap':'fa fa-folder'} /> {node.label}</span>
                    <span on-click={ (e) => e.stopPropagation() }>  
                        <el-button-group>              
                            <el-button title="添加" size="mini" icon="fa fa-plus-square" circle type="text" on-click={ (e) => this.add(data,e) }  disabled={data.id<0}></el-button>
                            <el-button title="修改" size="mini" icon="fa fa-edit" circle type="text" on-click={ (e) => this.edit(data,e) } disabled={data.id<=0}></el-button>
                            <el-button title="删除" size="mini" icon="fa fa-times-circle" circle  type="text" on-click={ (e) => this.del(node, data,e) } disabled={remove}></el-button>
                            <el-button title="添加人员" size="mini" icon="fa fa-user-plus" circle  type="text" on-click={ (e) => this.user( data,e) } disabled={data.id<=0}></el-button>
                        </el-button-group>
                    </span>
                </span>);
        },
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
