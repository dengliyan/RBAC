<template>
<!--
    重新封装日期控件，原控件操作不是很方便 
    改成为日期+时间两个控件分别显示
-->
    <div>
        <el-date-picker
            type="date"
            v-model="date"            
            :clearable="false"
            value-format="yyyy-MM-dd"
            placeholder="选择日期时间" :style="{width:'145px'}" />

        <el-time-picker                            
            placeholder="00:00:00"
            v-model="time"
            value-format="HH:mm:ss"
            :clearable="false"
            :style="{width:'125px'}" />
        
        <input type="text" :value="currentValue" style='display:none;'>   
    </div>
</template>

<script>
export default {
    props: {
        value:{
            type:String,
            default:''
        },
    },
    mounted() {
        let array=this.value.split(' ');
        if(array.length==2){
            this.date = array[0];
            this.time = array[1];
            this.currentValue=[this.date,this.time].join(' ');
        }
    },
    watch: {
        value(data) {
            this.value=data;
            let array=data.split(' ');
            if(array.length==2){
                this.date = array[0];
                this.time = array[1];
            }
        },
        date(val){
            this.currentValue=[this.date,this.time].join(' ');
            this.$emit('input', this.currentValue);
        },
        time(val){
            this.currentValue=[this.date,this.time].join(' ');
            this.$emit('input',this.currentValue);
        },
    },
    data(){
        return {
            date:'',
            time:'',
            currentValue:this.value
        }
    }
}
</script>

<style>

</style>
