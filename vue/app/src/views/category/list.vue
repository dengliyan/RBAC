<template>
    <div id="category-tree">
        <v-dialog 
                   :propsUIDialog="props.dialog.show"
                   :propsForm="formData"
                   :propsTree="tree"
                   @sync-dialog="syncDialog"
                   @on-add="afterAdd" />
        <el-tree 
            v-if="tree!=null" 
            node-key="id" 
            highlight-current
            :data="tree" 
            :default-expanded-keys="props.tree.keys" 
            :props="props.tree.defaultProps" 
            :expand-on-click-node="false"
            :render-content="renderContent"	 />
    </div>
</template>

<script>
import dialog from './dialog.vue'   
import util from '@/config/date-util.js'
export default {
    components:{
        'v-dialog':dialog
    },
    data(){
        return {
            tree:null,
            props:{
                tree:{
                    keys:[1],
                    defaultProps:{
                        children: 'children',
                        label: 'name'
                    }
                },
                dialog:{
                    show:false
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
            that.$api.get('/api/auth/category/tree?id=1').then(response=>{
                if(response.ret==0){
                    that.tree=response.data;  
                    callback&&callback();
                }else{
                    that.message.error('读取失败，请重试。')
                }
            });
        },
        renderContent(h, { node, data, store }) {        
            let edit=data.id==0;
            let remove=(data.id==0)||(data.children!=null&&data.children.length>0);
            return (
            <span class="custom-tree-node">
                <span>{node.label}({data.id})</span>
                <span>
                <el-button size="mini" type="text" on-click={ () => this.append(data) }>权限</el-button>
                <el-button size="mini" type="text" on-click={ () => this.append(data) }>新加</el-button>
                <el-button size="mini" type="text" on-click={ () => this.edit(data) } disabled={edit}>修改</el-button>
                <el-button size="mini"  type="text" on-click={ () => this.del(node, data) } disabled={remove}>删除</el-button>
                </span>
            </span>);
        },
        syncDialog(val){
            this.props.dialog.show=val;
        },
        //添加
        append(data){
            this.props.dialog.show=true;
            let parents=([data.id].concat(data.parent||[]));//拼接
            parents.reverse();//反转 
            this.formData={id:0,name:'',description:'',parents:parents,rank:util.now("yyyy-MM-dd")+" "+"00:00:00"};
        },
        afterAdd(param){
            let that=this;
            this.getData(function () {
               that.props.tree.keys=param.form.parents;//展开
            });
        },
        edit(data){ 
            let that=this;
            that.$api.get('/api/auth/category/item/'+data.id).then(response=>{
                if(response.ret==0){
                    let item=response.data;
                    let parents=([item.id].concat(item.parents||[]));//拼接
                    parents.reverse();//反转 
                    that.props.dialog.show=true;
                    that.formData={id:item.id,name:item.name,description:item.description,parents:parents,rank:util.format(new Date(item.rank),'yyyy-MM-dd hh:mm:ss')};
                }
            });
            //
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
    }
    .custom-tree-node:hover span:nth-of-type(2) {
        display: flex;
    }
    .el-tree-node__content{height: 32px ;line-height: 32px}
}
</style>
