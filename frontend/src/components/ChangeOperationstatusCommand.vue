<template>
    <v-card outlined>
        <v-card-title>
            ChangeOperationstatus
        </v-card-title>

        <v-card-text>
            <Boolean label="IsHailing" v-model="value.isHailing" :editMode="editMode"/>
            <String label="DriverLocation" v-model="value.driverLocation" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="changeOperationstatus"
            >
                ChangeOperationstatus
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
        name: 'ChangeOperationstatusCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.isHailing = false;
            this.value.driverLocation = '';
        },
        watch: {
        },
        methods: {
            changeOperationstatus() {
                this.$emit('changeOperationstatus', this.value);
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

