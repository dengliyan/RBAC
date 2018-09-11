<template>
    <div id="category-tree">
        <v-dialog 
                   :propsUIDialog="props.formDialog.show"
                   :propsForm="formData"
                   :propsTree="tree"
                   @sync-dialog="syncDialog"
                   @on-add="afterAdd"
                   @on-edit="afterEdit" />

        <v-user-dialog
                    :propsUIDialog="props.userDialog.show" 
                    :propsTitle="props.userDialog.title"
                    :propsData="props.userDialog.data"
                    :propsSelected="props.userDialog.selected"
                    @sync-dialog="syncDialog2" />

        <el-tree 
            v-if="tree!=null" 
            node-key="id" 
            highlight-current
            :data="tree" 
            default-expand-all	
            :default-expanded-keys="props.tree.keys" 
            :props="props.tree.defaultProps" 
            :expand-on-click-node="false"
            :render-content="renderContent"	 />
    </div>
</template>

<script>
 import util from '@/config/date-util.js'
export default {
    components:{
        'v-dialog':resolve => require(["./dialog.vue"], resolve),
        'v-user-dialog':resolve => require(["./user.vue"], resolve),
    },
    data(){
        return {
            tree:null,
            props:{
                tree:{
                    keys:[0],
                    defaultProps:{
                        children: 'childrens',
                        label: 'name'
                    }
                },
                formDialog:{
                    show:false
                },
                userDialog:{
                    show:false,
                    title:'',
                    data:[],
                    selected:[]
                }
            },
            formData:{}
        }
    },
    mounted(){
        this.getData();
    },    
    methods:{
        getData(callback){
            let that=this;
            that.$api.get('/api/auth/dept/tree').then(response=>{
                if(response.ret==0){
                    that.tree=[{id:0,name:'组织架构',childrens:response.data}];  
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
                <span>{node.label}</span>
                <span>  
                    <el-button-group>              
                        <el-button title="添加" size="mini" icon="fa fa-plus-square" circle type="text" on-click={ () => this.add(data) }></el-button>
                        <el-button title="修改" size="mini" icon="fa fa-edit" circle type="text" on-click={ () => this.edit(data) } disabled={edit}></el-button>
                        <el-button title="删除" size="mini" icon="fa fa-times-circle" circle  type="text" on-click={ () => this.del(node, data) } disabled={remove}></el-button>
                        
                    </el-button-group>
                </span>
            </span>);
        },
        syncDialog(val){
            this.props.formDialog.show=val;
        },
        syncDialog2(val){
            this.props.userDialog.show=val;
        },
        //添加
        add(data){
            let that=this;
            if(data.id==0){
                let parents=[data.id];
                that.props.dialog.show=true;
                that.formData={id:0,name:'',description:'',parents:parents,rank:util.now("yyyy-MM-dd")+" "+"00:00:00"};
            }else{
                that.$api.get('/api/auth/dept?id='+data.id).then(response=>{
                    if(response.ret==0){
                        let parents=([response.data.id].concat(response.data.parents||[]));//拼接
                        parents.push(0);//
                        parents.reverse();//反转 
                        that.props.formDialog.show=true;
                        that.formData={id:0,name:'',description:'',parents:parents,rank:util.now("yyyy-MM-dd")+" "+"00:00:00"};
                    }
                });
            }
            
        },
        afterAdd(param){
            let that=this;
            this.getData(function () {
               that.props.tree.keys=param.form.parents;//展开
            });
        },
        edit(data){ 
            let that=this;            
            that.$api.get('/api/auth/dept?id='+data.id).then(response=>{
                if(response.ret==0){
                    let item=response.data;
                    let parents=item.parents||[];//拼接
                    parents.push(0);
                    parents.reverse();//反转 
                    that.props.formDialog.show=true;
                    that.formData={id:item.id,name:item.name,description:item.description,parents:parents,rank:util.format(new Date(item.rank),'yyyy-MM-dd hh:mm:ss')};
                }
            });
        },
        afterEdit(param){
            let that=this;
            this.getData(function () {
               that.props.tree.keys=param.form.parents;//展开
            });
        },
        del(node, data){
            let that=this;
            that.$confirm('删除分类 '+data.name+', 是否继续?', '提示', {confirmButtonText: '确定',cancelButtonText: '取消', type: 'warning'}).then(() => {
                that.$api.post('/api/auth/dept/delete',{id:data.id}).then(response=>{
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
        user(data){
            //读取当前用户
            let that=this;
            that.$api.get('/api/auth/dept/user?dept='+data.id).then(response=>{
                let selected=[];
                for(var g in response.data){
                    for(var x in response.data[g].options){
                        if(response.data[g].options[x].val==data.id){
                            selected.push(response.data[g].options[x].value);
                        }
                    }
                }
                that.props.userDialog={show:true,title:data.name,data:response.data,selected:selected};
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
    }
    .custom-tree-node:hover span:nth-of-type(2) {
        display: flex;
    }
    .el-tree-node__content{height: 32px ;line-height: 32px}
}
</style>
