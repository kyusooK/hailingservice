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
                    <h1 class="align-self-center ml-3">Payment</h1>
                    <div class="secondary-text-color" style="margin-left:30px;"></div>
                </v-list-item>
            </v-row>
        </v-card>
        <v-col>
            <div>
                <!-- 모드 종류 : receipt(영수증 조회), pay(결제정보), refund(환불관련), buyerInfoMode(구매자 정보 표시관련)  -->
                <PaymentSystem
                    :serviceType="'pay'" 
                    :buyerInfoMode="true"
                    :requestInfo="requestInfo"
                />

                <PaymentSystem
                    :serviceType="'refund'" 
                    :buyerInfoMode="true"
                    :requestInfo="requestInfo"
                />

                <PaymentSystem
                    :serviceType="'receipt'" 
                    :buyerInfoMode="true"
                    :requestInfo="requestInfo"
                />
            </div>
        </v-col>
        <v-row class="ma-0 pa-0">
            <v-col class="pa-4" cols="12" sm="6" md="3" v-for="(value, index) in values" :key="index">
                <PaymentSystem :offline="offline"  v-model="values[index]" @delete="remove"/>
            </v-col>
        </v-row>
    </div>
</template>

<script>

    // const axios = require('axios').default;
    import PaymentSystem from './PaymentSystem.vue';

    export default {
        name: 'RequestPaymentPaymentManager',
        components: {
            PaymentSystem
        },
        props: {
            offline: Boolean
        },
        data: () => ({
            values: [],
            newValue: {},
            tick : true,
            // buyerInfoMode : false,
            requestInfo: {
                itemId: null, // 상품 번호
                price: 0, // 상품 가격
                name: '', // 상품 이름
                buyerId: '', // 구매자 아이디
                buyerName: '', // 구매자 이름
                buyerTel: '', // 구매자 전화번호
                buyerEmail: '', // 구매자 이메일
                reason: '' // 환불 사유
            },
        }),
        async created() {
            // var me = this;
            // if(me.offline){
            //     if(!me.values) me.values = [];
            //     return;
            // } 

            // var temp = await axios.get(axios.fixUrl('/payments'))
            // me.values = temp.data._embedded.payments;
            
            // me.newValue = {
            //     'amount': 0,
            //     'issuedDate': '2024-12-30',
            //     'approvalDate': '2024-12-30',
            //     'refunedDate': '2024-12-30',
            // }
            // this.setRequestInfo();
        },
        methods:{
            setRequestInfo() {
                this.requestInfo = {
                    id: 2, // search 
                    paymentId: 'payment-53f93879-79b9-467f-9958-71f0c483052b',
                    itemId: 2,
                    price: 129000,
                    name: '키보드',
                    buyerId: 'kibum',
                    buyerName: '박기범',
                    buyerTel: '010-0000-0000',
                    buyerEmail: 'kibum0405@gmail.com',
                    reason: ''
                };
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
            remove(value){
                var where = -1;
                for(var i=0; i<this.values.length; i++){
                    if(this.values[i]._links.self.href == value._links.self.href){
                        where = i;
                        break;
                    }
                }

                if(where > -1){
                    this.values.splice(i, 1);
                    this.$emit('input', this.values);
                }
            },
        }
    };
</script>


<style>
    
</style>

