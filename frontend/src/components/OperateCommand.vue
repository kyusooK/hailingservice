<template>
    <v-card outlined>
        <v-card-title>
            Operate
        </v-card-title>

        <v-card-text>
            <OperationStatus offline label="OperationStatus" v-model="value.operationStatus" :editMode="editMode" @change="change"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="operate"
            >
                Operate
            </v-btn>
            
            <v-btn
                    color="primary"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>
   
    export default {
        name: 'OperateCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.operationStatus = {};
        },
        watch: {
        },
        methods: {
            operate() {
                this.$emit('operate', this.value);
            },
            close() {
                this.$emit('closeDialog');
            },
            change() {
                this.$emit('input', this.value);
            },
        }
    }
</script>

