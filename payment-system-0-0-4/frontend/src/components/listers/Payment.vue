<template>
    <div>
        <v-btn 
            class="mx-auto"
            @click="openDialog = true"
            color="primary"
            text
        >
            결제
        </v-btn>
        <v-dialog width="400" v-model="openDialog">
            <v-card>
                <div v-if="serviceType == 'pay'"
                    style="width:100%;"
                >
                    <v-row class="ma-0 pa-4 pb-0">
                        <v-card-title class="pa-0">결제하기</v-card-title>
                        <v-spacer></v-spacer>
                        <v-icon @click="closeDialog()">mdi-close</v-icon>
                    </v-row>
                    <v-card-text class="payment-dialog-scroll">
                        <h3 class="pb-2">상품명 : {{ requestInfo.name }}</h3>
                        <v-row class="ma-0 pa-0">
                            <div class="sub-title">
                                <div>결제 번호 : {{ paymentId }}</div>
                                <div>주문 번호 : {{ requestInfo.orderId }}</div>
                            </div>
                        </v-row>

                        <v-divider class="mt-2 mb-2"></v-divider>
                        <!-- pay 디테일 -->
                        <div v-if="paymentDetail">
                            <v-text-field outlined v-model="requestInfo.buyerName" label="구매자 성명" />
                            <v-text-field outlined v-model="requestInfo.buyerEmail" label="구매자 이메일" />
                            <v-text-field outlined v-model="requestInfo.buyerId" label="구매자 아이디" />
                            <v-text-field outlined v-model="requestInfo.buyerTel" label="구매자 전화번호" />
                        </div>
                    </v-card-text>
                    <v-row class="ma-0 pa-4">
                        <v-spacer></v-spacer>
                        <v-btn @click="pay()" color="primary">{{ formattedPrice }}원 결제하기</v-btn>
                    </v-row>
                </div>
                <div v-if="serviceType == 'refund'">
                    <v-row class="ma-0 pa-4 pb-0">
                        <v-card-title class="pa-0">환불하기</v-card-title>
                        <v-spacer></v-spacer>
                        <v-icon @click="closeDialog()">mdi-close</v-icon>
                    </v-row>
                    <v-card-text class="payment-dialog-scroll">
                        <v-text-field outlined required v-model="requestInfo.paymentId" label="환불 주문번호 입력" />
                        <v-textarea
                            outlined
                            required
                            v-model="requestInfo.reason"
                            label="환불 사유"
                            auto-grow
                            :rows="3"
                        />
                    </v-card-text>
                    <v-row class="ma-0 pa-4 align-center">
                        <div style="font-size:16px; font-weight: 700;">환불 금액 : {{ formattedPrice }}원</div>
                        <v-spacer></v-spacer>
                        <v-btn @click="refund()" color="primary">환불</v-btn>
                    </v-row>
                </div>
                <div v-if="serviceType == 'receipt'">
                    <v-row class="ma-0 pa-4 pb-0">
                        <v-card-title class="pa-0">영수증 조회</v-card-title>
                        <v-spacer></v-spacer>
                        <v-icon @click="closeDialog()">mdi-close</v-icon>
                    </v-row>
                    <div class="payment-dialog-scroll">
                        <div class="pa-4">
                            <v-text-field outlined required v-model="requestInfo.paymentId" label="조회 주문번호 입력" />
                        </div>
                        <v-card-text v-if="isReceiptCompleted">
                            <v-text-field outlined disabled v-model="receiptInfo.name" label="상품명"/>
                            <v-text-field outlined disabled v-model="receiptInfo.price" label="가격"/>
                            
                            <!-- receipt 디테일 -->
                            <div v-if="paymentDetail">
                                <v-text-field outlined disabled v-model="receiptInfo.buyerName" label="구매자 성명" />
                                <v-text-field outlined disabled v-model="receiptInfo.buyerEmail" label="구매자 이메일" />
                                <v-text-field outlined disabled v-model="receiptInfo.buyerId" label="구매자 아이디" />
                                <v-text-field outlined disabled v-model="receiptInfo.buyerTel" label="구매자 전화번호" />
                            </div>
                        </v-card-text>
                    </div>

                    <v-row class="ma-0 pa-4">
                        <v-spacer></v-spacer>
                        <v-btn @click="search()" color="primary"> 조회 </v-btn>
                    </v-row>
                </div>   
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
    const axios = require('axios').default;
    // import { v4 as uuidv4 } from 'uuid'; 
    // import $ from 'jquery'; // jQuery를 import
    import * as PortOne from "@portone/browser-sdk";

    export default {
        name: 'payment-service',
        props: {
            serviceType: String,
            requestInfo: {
                type: Object,
                default: () => ({
                    orderId: "",
                    price: 0,
                    name: "",
                    buyerId: "",
                    buyerName: "",
                    buyerTel: "",
                    buyerEmail: "",
                    reason: ""
                })
            },
            paymentDetail: Boolean,
        },
        data: () => ({
            paymentId: `payment-${crypto.randomUUID()}`,
            receiptInfo: {
                name: "",
                price: "",
                buyerName: "",
                buyerEmail: "",
                buyerId: "",
                buyerTel: ""
            },
            isReceiptCompleted: false,
            openDialog: false,
        }),
        async created() {
            // var me = this;
        },
        computed: {
            formattedPrice() {
                return this.requestInfo.price.toLocaleString('ko-KR');
            },
        },
        methods:{
            async pay(){
                var me = this
                const response = await PortOne.requestPayment({                    // Store ID 설정
                    storeId: "store-acb53ee1-ef4e-4966-b341-8c6a36d81096",
                    channelKey: "channel-key-c89519b6-bf42-4aac-b23a-cf92d4a28a6b",
                    paymentId: me.paymentId,
                    orderName: me.requestInfo.name,
                    totalAmount: me.requestInfo.price,
                    currency: "CURRENCY_KRW",
                    payMethod: "CARD",
                    buyer: {
                        id: me.requestInfo.buyerId,
                        name: me.requestInfo.buyerName,
                        email: me.requestInfo.buyerEmail,
                        tel: me.requestInfo.buyerTel
                    }
                });


                if (response.success) {
                    await axios.put(`http://localhost:8088/payments/${me.requestInfo.orderId}/receivepaymentstatus`,
                        {
                            id: me.requestInfo.orderId,
                            paymentId: me.paymentId,
                            status: "SUCCESS",
                            reason: response.message
                        }
                    )
                    alert(`결제 성공`)
                } else {
                    await axios.put(`http://localhost:8088/payments/${me.requestInfo.orderId}/receivepaymentstatus`,
                        {
                            id: me.requestInfo.orderId,
                            paymentId: me.paymentId,
                            status: "SUCCESS",
                            reason: response.message
                        }
                    )
                    alert(`결제 성공`)
                    // await axios.put(`http://localhost:8088/payments/${me.requestInfo.orderId}/receivepaymentstatus`,
                    //     {
                    //         id: me.requestInfo.orderId,
                    //         paymentId: me.paymentId,
                    //         status: "CANCELLED",
                    //         reason: response.message
                    //     }
                    // )
                    // alert(`결제 실패 : ${response.message}`)
                }
            },  
            async refund(){
                var me = this
                try {
                    let response = await axios.post(`https://api.portone.io/payments/${me.requestInfo.paymentId}/cancel`, {'Content-Type': 'application/json'}, {
                        reason: me.requestInfo.reason
                    })
                    if(response.status == 200) {
                        alert("환불 완료")
                    } else {
                        alert("환불 실패")
                    }
                }catch(e) {
                    alert("환불 실패")
                }
            },
            async search(){
                var me = this
                try {
                    me.isReceiptCompleted = false
                    let response = await axios.get(`https://api.portone.io/payments/payments/${me.requestInfo.paymentId}`,{'Content-Type': 'application/json'})
                    if(response.status == 200) {
                        me.receiptInfo.name = response.orderName
                        me.receiptInfo.price = response.amount.paid
                        me.receiptInfo.buyerName = response.customer.name
                        me.receiptInfo.buyerEmail = response.customer.email
                        me.receiptInfo.buyerTel = response.customer.phoneNumber
                        me.isReceiptCompleted = true
                    } else {
                        alert(`조회 실패 : ${response.message}`)
                    }
                   
                }catch(e) {
                    alert("조회 실패")
                }
            },
            closeDialog() {
                this.openDialog = false;
            }
        }
    };
</script>
<style>
    .payment-dialog-scroll {
        max-height: 65vh;
        overflow: auto;
    }
    .sub-title {
        font-size: 12px;
        font-weight: 700;
        width: 100%;
    }
</style>

