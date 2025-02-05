<template>

    <v-card outlined>
        <v-card-title>
            OperationRecord # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <String label="DriverId" v-model="item.driverId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="UserId" v-model="item.userId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="StartingPoint" v-model="item.startingPoint" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="Destination" v-model="item.destination" :editMode="editMode" @change="change" />
            </div>
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'OperationRecordViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/operationRecords/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>

