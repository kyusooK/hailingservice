<template>
    <v-card outlined>
        <v-card-title>
            CompleteOperation
        </v-card-title>

        <v-card-text>
            <OperationStatus offline label="OperationStatus" v-model="value.operationStatus" :editMode="editMode" @change="change"/>
            <Number label="Fee" v-model="value.fee" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="completeOperation"
            >
                CompleteOperation
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
        name: 'CompleteOperationCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.operationStatus = {};
            this.value.fee = 0;
        },
        watch: {
        },
        methods: {
            completeOperation() {
                this.$emit('completeOperation', this.value);
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

