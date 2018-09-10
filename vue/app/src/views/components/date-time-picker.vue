<template>
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
    </div>
</template>

<script>
export default {
    props: {
        propsValue:{
            type:String,
            default:''
        }
    },
    watch: {
        propsValue(val) {
            console.log('-----------');
            if(val.indexOf(' ')>0){
                let a=val.split(' ')
                this.date = a[0];
                this.time = a[1];
            }
        },
        date(val){
            console.log("date ->"+val+" "+[this.date,this.time].join(' '));

            this.$emit('sync-picker',[this.date,this.time].join(' '))
        },
        time(val){
            console.log("time ->"+val+" "+[this.date,this.time].join(' '));
            this.$emit('sync-picker',[this.date,this.time].join(' '))
        },
    },
    data(){
        return {
            date:'',
            time:'',
        }
    }
}
</script>

<style>

</style>
