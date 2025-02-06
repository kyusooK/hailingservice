
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import UserUserManager from "./components/listers/UserUserCards"
import UserUserDetail from "./components/listers/UserUserDetail"

import DispatchOperationManager from "./components/listers/DispatchOperationCards"
import DispatchOperationDetail from "./components/listers/DispatchOperationDetail"
import DispatchMatchingManager from "./components/listers/DispatchMatchingCards"
import DispatchMatchingDetail from "./components/listers/DispatchMatchingDetail"

import DriverDriverManager from "./components/listers/DriverDriverCards"
import DriverDriverDetail from "./components/listers/DriverDriverDetail"


import OperationRecordView from "./components/OperationRecordView"
import OperationRecordViewDetail from "./components/OperationRecordViewDetail"

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/users/users',
                name: 'UserUserManager',
                component: UserUserManager
            },
            {
                path: '/users/users/:id',
                name: 'UserUserDetail',
                component: UserUserDetail
            },

            {
                path: '/dispatches/operations',
                name: 'DispatchOperationManager',
                component: DispatchOperationManager
            },
            {
                path: '/dispatches/operations/:id',
                name: 'DispatchOperationDetail',
                component: DispatchOperationDetail
            },
            {
                path: '/dispatches/matchings',
                name: 'DispatchMatchingManager',
                component: DispatchMatchingManager
            },
            {
                path: '/dispatches/matchings/:id',
                name: 'DispatchMatchingDetail',
                component: DispatchMatchingDetail
            },

            {
                path: '/drivers/drivers',
                name: 'DriverDriverManager',
                component: DriverDriverManager
            },
            {
                path: '/drivers/drivers/:id',
                name: 'DriverDriverDetail',
                component: DriverDriverDetail
            },


            {
                path: '/operationstatistics/operationRecords',
                name: 'OperationRecordView',
                component: OperationRecordView
            },
            {
                path: '/operationstatistics/operationRecords/:id',
                name: 'OperationRecordViewDetail',
                component: OperationRecordViewDetail
            },


    ]
})
