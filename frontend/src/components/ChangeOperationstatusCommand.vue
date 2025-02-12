<template>
    <v-card outlined>
        <v-row class="ma-0 pa-4">
            <v-card-title class="ma-0 pa-0">
                운행상태 변경
            </v-card-title>
            <v-spacer></v-spacer>
            <v-icon @click="close">mdi-close</v-icon>
        </v-row>

        <v-card-text>
            <Boolean v-model="value.isHailing" :editMode="editMode"/>
            <String class="delete-input-detail" label="운전자 위치" v-model="value.driverLocation" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="changeOperationstatus"
            >
                운행상태 변경
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

