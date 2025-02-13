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
                    <h1 class="align-self-center ml-3">환불</h1>
                </v-list-item>
            </v-row>
        </v-card>
        <v-col style="margin-bottom:40px;">
            <payment serviceType="refund" :requestInfo="info"></payment>
        </v-col>
    </div>
</template>

<script>
    import Payment from './Payment.vue';

    export default {
        name: 'RequestPaymentPaymentManager',
        components: {
            'payment': Payment
        },
        data: () => ({
            info: {}
        }),
        async created() {
            this.info = {
                id : "payment-bcbdaea3-1c6c-4b19-8abc-bc35e0954d44", // 환불 주문번호
                price : 10000, // 환불 가격
                reason: "취소 사유" // 환불 사유
            }
        },
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

