<template>
    <div>
        <!-- <v-card
            class="mx-auto"
            outlined
            color="primary"
            style="padding:10px 0px 10px 0px; margin-bottom:40px;"
        >
            <v-row>
                <v-list-item class="d-flex" style="background-color: white;">
                    <h1 class="align-self-center ml-3">영수증 조회</h1>
                </v-list-item>
            </v-row>
        </v-card>
        <v-col style="margin-bottom:40px;">
            <payment serviceType="receipt" :requestInfo="info"></payment>
        </v-col> -->
    </div>
</template>

<script>
    // import Payment from './Payment.vue';

    export default {
        name: 'search-receipt',
        components: {
            // 'payment': Payment
        },
        data: () => ({
            // info: {} 
        }),
        async created() {
            // this.info = {
            //     id : "2", // 결제 번호
            // }
        },
    };
</script>
