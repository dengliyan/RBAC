<template>
    <div>

            <el-table
                :data="data"
                style="width: 100%">
                <el-table-column
                    prop="date"
                    label="角色名"
                    width="200">
                </el-table-column>
                
                <el-table-column
                    prop="address"
                    label="地址">
                </el-table-column>
                </el-table>
    </div>
</template>
<script>
export default {
    data(){
        return {
            data:[]
        }
    },
    mounted(){
        let that=this;
        that.$api.get('/api/auth/role').then(response=>{
            if(response.ret==0){
                that.data=response.data;
            }
        });
    }
}
</script>