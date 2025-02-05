<template>

    <v-data-table
        :headers="headers"
        :items="paymentReceipt"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'PaymentReceiptView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            paymentReceipt : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/paymentReceipts'))

            temp.data._embedded.paymentReceipts.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.paymentReceipt = temp.data._embedded.paymentReceipts;
        },
        methods: {
        }
    }
</script>

