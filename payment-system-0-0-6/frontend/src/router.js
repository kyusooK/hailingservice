
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import RequestPaymentPaymentManager from "./components/listers/RequestPaymentPaymentCards"
import RequestPaymentPaymentDetail from "./components/listers/RequestPaymentPaymentDetail"
import RequestPayment from "./components/listers/RequestPayment.vue"
import RefundPayment from "./components/listers/RefundPayment.vue"
import SearchReceipt from "./components/listers/SearchReceipt.vue"



export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/requestPayments/payments',
                name: 'RequestPaymentPaymentManager',
                component: RequestPaymentPaymentManager
            },
            {
                path: '/requestPayments/payments/:id',
                name: 'RequestPaymentPaymentDetail',
                component: RequestPaymentPaymentDetail
            },
            {
                path: '/requestPayments/pay/:id',
                name: 'RequestPayment',
                component: RequestPayment
            },
            {
                path: '/requestPayments/refund/:id',
                name: 'RefundPayment',
                component: RefundPayment
            },
            {
                path: '/requestPayments/receipt/:id',
                name: 'SearchReceipt',
                component: SearchReceipt
            },





    ]
})
