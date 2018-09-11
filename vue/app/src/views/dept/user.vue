<template>
    <div>
        <el-dialog
            :title="propsTitle+'人员列表'"
            :visible.sync="UIDialog"
            top="0px"
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            width="680px">
                 <el-form  label-width="0">
                     <el-form-item>
                        <div style="height:290px">
                            <el-select                                
                                multiple	
                                style="width:100%;" 
                                v-model="selected" 
                                placeholder="请选择人员">
                                    <el-option-group
                                        v-for="group in propsData"
                                        :key="group.name"
                                        :label="group.name">
                                        <el-option
                                                v-for="item in group.options"
                                                :key="item.value"
                                                :label="item.name"
                                                :value="item.value">
                                                    <span>{{ item.name }}</span>
                                                    <span>{{ item.text }}</span>
                                        </el-option>
                                    </el-option-group>
                            </el-select>
                        </div>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="UIDialog=false">取 消</el-button>
                    <el-button type="primary" @click="submit" :disabled="UISubmit" :loading="UISubmit">{{UISubmit?'提交中...':'确 定'}}</el-button>
                </span>
            </el-dialog>
    </div>
</template>

<script>
export default {
    
    props: {
        propsUIDialog:{
            type:Boolean,
            default:false
        },
        propsTitle:{
            type:String,
            default:"部门"
        },
        propsData:{
            type:Array,
            default:[]
        },
        propsSelected:{
            type:Array,
            default:[]
        }
    },
    watch: {
        propsUIDialog(val) {
            this.UIDialog = val;//②监听外部对props属性的变更，并同步到组件内的data属性中                 
        },
        UIDialog(val){
            this.$emit('sync-dialog',val);
        },
        propsSelected(val){
            this.selected=val;
        }    
    },
    data(){
        return {
            UIDialog:false,
            UISubmit:false, 
            selected:[],
        }
    },
    methods:{
        submit(){
            let that=this;
            let data={}
            
        },
        syncPicker(val){
            this.formData.rank=val;
        }
    }
}
</script>

<style lang="less">


</style>
