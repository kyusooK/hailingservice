<template>
    <v-card outlined>
        <v-card-title>
            AcceptCarhailing
        </v-card-title>

        <v-card-text>
            <Boolean label="IsHailing" v-model="value.isHailing" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="acceptCarhailing"
            >
                AcceptCarhailing
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
        name: 'AcceptCarhailingCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.isHailing = false;
        },
        watch: {
        },
        methods: {
            acceptCarhailing() {
                this.$emit('acceptCarhailing', this.value);
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

