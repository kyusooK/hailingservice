<template>
    <div>
    <h1>매칭정보</h1>
        <v-row>
            <v-card
                class="mx-auto"
                style="height:300px; width:300px; margin-bottom:20px; text-align: center;"
                outlined
            >
                <v-list-item>
                    <v-list-item-avatar 
                        class="mx-auto"
                        size="80"
                        style="margin-top:80px;"
                    ><v-icon color="primary" x-large>mdi-plus</v-icon>
                    </v-list-item-avatar>
                </v-list-item>

                <v-card-actions>
                    <v-btn 
                        v-on="on"
                        class="mx-auto"
                        outlined
                        rounded
                        @click="openDialog=true;"
                        color="primary"
                        style="font-weight:500; font-size:20px; padding:15px; border:solid 2px; max-width:250px; overflow:hidden"
                    >
                        매칭정보 등록
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-row>
        <v-list two-line>
            <template>
                <v-list-item v-for="(data, n) in values" :key="n">
                    <v-list-item-avatar color="grey darken-1">
                        <v-img :src="data.photo ? data.photo:'https://cdn.vuetifyjs.com/images/cards/cooking.png'"/>
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title style="margin-bottom:10px;">
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        </v-list-item-title>

                        <v-list-item-subtitle style="font-size:25px; font-weight:700;">
                            [ Id :  {{data.id }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Destination :  {{data.destination }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ PassengerLocation :  {{data.passengerLocation }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ DriverId :  {{data.driverId }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ UserId :  {{data.userId }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Latitude :  {{data.latitude }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ Longitude :  {{data.longitude }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ EstimatedTime :  {{data.estimatedTime }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ EstimatedDistance :  {{data.estimatedDistance }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            [ DriverLocation :  {{data.driverLocation }} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </v-list-item-subtitle>

                    </v-list-item-content>
                </v-list-item>

                <v-divider v-if="n !== 6" :key="`divider-${n}`" inset></v-divider>
            </template>
        </v-list>

        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                        v-model="openDialog"
                        width="332.5"
                        transition="dialog-bottom-transition"
                >

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
            }
        },
    };
</script>


<style>
</style>

