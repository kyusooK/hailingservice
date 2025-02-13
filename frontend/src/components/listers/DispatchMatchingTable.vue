<template>
    <div>
        <v-data-table
                :headers="headers"
                :items="values"
                :items-per-page="5"
                class="elevation-1"
        ></v-data-table>

        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                        v-model="openDialog"
                        width="332.5"
                        transition="dialog-bottom-transition"
                >
                    <template v-slot:activator="{ on, attrs }">
                        <v-fab-transition>
                            <v-btn
                                    color="primary"
                                    fab
                                    dark
                                    large
                                    style="position:absolute; bottom: 5%; right: 2%; z-index:99"
                                    @click="openDialog=true;"
                            >
                                <v-icon v-bind="attrs" v-on="on">mdi-plus</v-icon>
                            </v-btn>
                        </v-fab-transition>
                    </template>

                    <DispatchMatching :offline="offline"  :isNew="true" :editMode="true" v-model="newValue" @add="append" v-if="tick"/>
                
                    <v-btn
                            style="postition:absolute; top: 16px; right:16px;"
                            @click="closeDialog()"
                            depressed 
                            icon 
                            absolute
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-dialog>
            </div>
        </v-col>
    </div>
</template>

<script>
    const axios = require('axios').default;
    import DispatchMatching from './../DispatchMatching.vue';

    export default {
        name: 'DispatchMatchingManager',
        components: {
            DispatchMatching,
        },
        props: {
            offline: Boolean,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            values: [],
            headers: 
                [
                    { text: "id", value: "id" },
                    { text: "destination", value: "destination" },
                    { text: "passengerLocation", value: "passengerLocation" },
                    { text: "driverId", value: "driverId" },
                    { text: "userId", value: "userId" },
                    { text: "latitude", value: "latitude" },
                    { text: "longitude", value: "longitude" },
                    { text: "estimatedTime", value: "estimatedTime" },
                    { text: "estimatedDistance", value: "estimatedDistance" },
                    { text: "driverLocation", value: "driverLocation" },
                ],
            matching : [],
            newValue: {},
            tick : true,
            openDialog : false,
        }),
        async created() {
            if(this.offline){
                if(!this.values) this.values = [];
                return;
            }

            var temp = await axios.get(axios.fixUrl('/matchings'))
            temp.data._embedded.matchings.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])
            this.values = temp.data._embedded.matchings;

            this.newValue = {
                'destination': '',
                'passengerLocation': '',
                'driverId': {},
                'userId': {},
                'latitude': 0,
                'longitude': 0,
                'estimatedTime': 0,
                'estimatedDistance': 0,
                'driverLocation': '',
            }
        },
        methods: {
            closeDialog(){
                this.openDialog = false
            },
            append(value){
                this.tick = false
                this.newValue = {}
                this.values.push(value)
                
                this.$emit('input', this.values);

                this.$nextTick(function(){
                    this.tick=true
                })
            },
        }
    }
</script>

