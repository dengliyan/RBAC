<template>
  <div>
      <el-collapse accordion style="width:100%;overflow:hidden">

            <el-collapse-item v-for="x in collapse.data" :title="x.name" :name="x.name">
                    <el-table
                    :data="x.options" border stripe>
                    
                        <el-table-column
                            prop="name"
                            label="姓名"
                            width="180">
                            <template slot-scope="scope">
                                <el-button type="text" size="small" >{{ scope.row.name }}</el-button>
                            </template>
                        </el-table-column>
                        <el-table-column
                            prop="a"
                            label="邮箱"
                            width="150">                            
                        </el-table-column>
                        <el-table-column
                            prop="b"
                            label="手机号"
                            width="150">                            
                        </el-table-column>
                        <el-table-column
                            prop="text"
                            label="部门层级">
                        </el-table-column>
                    </el-table>
            </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        collapse: {
            data:[],
            keys:[0],
            props:{
                children: 'childrens',
                label: 'name'
            }
        }
      };
    },
    mounted(){
        this.getData();
    },    
    methods:{
        getData(){
            let that=this;
            that.$api.get('/api/auth/dept/user').then(response=>{
                if(response.ret==0){
                    that.collapse.data=response.data;  
                }else{
                    that.message.error('读取失败，请重试。')
                }
            });
        },
    }
  };
</script>

<style>

</style>
