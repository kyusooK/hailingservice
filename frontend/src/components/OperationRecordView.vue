<template>

    <v-data-table
        :headers="headers"
        :items="operationRecord"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'OperationRecordView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
                { text: "driverId", value: "driverId" },
                { text: "userId", value: "userId" },
                { text: "startingPoint", value: "startingPoint" },
                { text: "destination", value: "destination" },
            ],
            operationRecord : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/operationRecords'))

            temp.data._embedded.operationRecords.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.operationRecord = temp.data._embedded.operationRecords;
        },
        methods: {
        }
    }
</script>

