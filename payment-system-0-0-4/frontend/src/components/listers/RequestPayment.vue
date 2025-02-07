<template>
    <div>
        <v-card
            class="mx-auto"
            outlined
            color="primary"
            style="padding:10px 0px 10px 0px; margin-bottom:40px;"
        >
            <v-row>
                <v-list-item class="d-flex" style="background-color: white;">
                    <h1 class="align-self-center ml-3">결제</h1>
                    <div class="secondary-text-color" style="margin-left:30px;"></div>
                </v-list-item>
            </v-row>
        </v-card>
        <v-col style="margin-bottom:40px;">
            <div v-if="isLoading">
                <v-progress-circular
                    indeterminate
                    color="primary"
                    style="margin-left:50%; margin-top:10%;"
                ></v-progress-circular>
            </div>
            <div v-else>
                <payment v-if="isValidation" serviceType="pay" :requestInfo="info"></payment>
                <div v-else> 결제 정보가 없습니다. </div>
            </div>
           
        </v-col>
    </div>
</template>

<script>
    import Payment from './Payment.vue';
    import axios from 'axios';

    export default {
        name: 'RequestPaymentPaymentManager',
        components: {
            'payment': Payment
        },
        props: {
            offline: Boolean
        },
        data: () => ({
            info: {},
            isValidation: false,
            isLoading: false,
        }),
        async created() {
            try {
                const id = this.$route.params.id;
                this.isLoading = true
                const response = await axios.get(`http://localhost:8088/payments/${id}`);

                if(response.status == 200) {
                    response.data.orderId = id
                    this.info = response.data
                    this.isValidation = true
                } else {
                    this.isValidation = false
                }
                this.isLoading = false     
            } catch(e) {
                this.isValidation = false
                this.isLoading = false     
            }
        },
        methods:{

            
        }
    };
</script>


<style>
    .video-card {
        width:300px; 
        margin-left:4.5%; 
        margin-top:50px; 
        margin-bottom:50px;
    }
</style>

