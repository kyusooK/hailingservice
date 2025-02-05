<template>
    <v-card outlined>
        <v-card-title>
            RequestCancel
        </v-card-title>

        <v-card-text>
            <Number label="Id" v-model="value.id" :editMode="editMode"/>
            <String label="Paymethod" v-model="value.paymethod" :editMode="editMode"/>
            <String label="StoreId" v-model="value.storeId" :editMode="editMode"/>
            <Number label="Price" v-model="value.price" :editMode="editMode"/>
            <Date label="TimeStamp" v-model="value.timeStamp" :editMode="editMode"/>
            <String label="Name" v-model="value.name" :editMode="editMode"/>
            <String label="BuyerName" v-model="value.buyerName" :editMode="editMode"/>
            <String label="BuyerTel" v-model="value.buyerTel" :editMode="editMode"/>
            <String label="BuyerEmail" v-model="value.buyerEmail" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="requestCancel"
            >
                RequestCancel
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
        name: 'RequestCancelCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.id = 0;
            this.value.paymethod = '';
            this.value.storeId = '';
            this.value.price = 0;
            this.value.timeStamp = '2025-02-05';
            this.value.name = '';
            this.value.buyerName = '';
            this.value.buyerTel = '';
            this.value.buyerEmail = '';
        },
        watch: {
        },
        methods: {
            requestCancel() {
                this.$emit('requestCancel', this.value);
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

