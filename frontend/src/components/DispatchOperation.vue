<template>
    <v-card style="width:450px; height:100%;" outlined>
        <template slot="progress">
            <v-progress-linear
                    color="primary-darker-1"
                    height="10"
                    indeterminate
            ></v-progress-linear>
        </template>

        <v-card-title v-if="value._links">
            운행정보 # {{decode(value._links.self.href.split("/")[value._links.self.href.split("/").length - 1])}}
        </v-card-title >
        <v-card-title v-else>
            운행정보
        </v-card-title >        

        <v-card-text style="background-color: white;">
            <String label="승객현재위치" v-model="value.passengerLocation" :editMode="editMode" :inputUI="''"/>
            <String label="목적지" v-model="value.destination" :editMode="editMode" :inputUI="''"/>
            <OperationStatus offline label="OperationStatus" v-model="value.operationStatus" :editMode="editMode" @change="change"/>
            <UserId offline label="userId" v-model="value.userId" :editMode="editMode" @change="change"/>
            <DriverId v-if="!editMode" offline label="driverId" v-model="value.driverId" :editMode="false" @change="change"/>
            <Number v-if="!editMode" label="운행요금" v-model="value.fee" :editMode="false" :inputUI="''"/>
        </v-card-text>
        <v-card-actions style="background-color: white;">
            <v-spacer></v-spacer>
            <div v-if="!editMode">
                <v-row>
                    <payment v-if="!editMode" serviceType="pay" :paymentDetail="false" :editMode="true" :requestInfo="receiptInfo"/>
                <v-btn
                color="primary"
                text
                @click="edit"
                >
                    수정
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="remove"
                >
                    삭제
                </v-btn>
                <v-btn
                    v-if="!editMode"
                    color="primary"
                    text
                    @click="openOperate"
                >
                    운행
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="save"
                >
                    운행 완료
                </v-btn>
                </v-row>
            </div>
            <div v-else>
                <v-btn
                    color="primary"
                    text
                    @click="save"
                >
                    차량호출
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="editMode = false"
                    v-if="editMode && !isNew"
                >
                    취소
                </v-btn>
            </div>
        </v-card-actions>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-dialog v-model="operateDiagram" width="500">
                <OperateCommand
                    @closeDialog="closeOperate"
                    @operate="operate"
                ></OperateCommand>
            </v-dialog>
        </v-card-actions>
        
        <review-app>
            <review-review-cards show-reviews="true" show-review-input="true" detail-mode="true" :value="JSON.stringify(reviewData)"></review-review-cards>
        </review-app>

        <v-snackbar
            v-model="snackbar.status"
            :top="true"
            :timeout="snackbar.timeout"
            color="error"
        >
            {{ snackbar.text }}
            <v-btn dark text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
    </v-card>

</template>

<script>
    const axios = require('axios').default;


    export default {
        name: 'DispatchOperation',
        components:{
        },
        props: {
            value: [Object, String, Number, Boolean, Array],
            editMode: Boolean,
            isNew: Boolean,
            offline: Boolean,
        },
        data: () => ({
            snackbar: {
                status: false,
                timeout: 5000,
                text: '',
            },
            reviewData: {
                userId: "사용자"
            },
            receiptInfo: {
                name: "운행요금",
                
            },
            operateDiagram: false,
        }),
	async created() {
            if(!this.reviewData.itemId){
                this.reviewData.itemId = this.decode(this.value._links.self.href.split("/")[this.value._links.self.href.split("/").length - 1])
            }
            if(!this.receiptInfo.price){
                this.receiptInfo.price = 10000
            }
            console.log(this.reviewData.itemId)
        },
        methods: {
            decode(value) {
                return decodeURIComponent(value);
            },
            selectFile(){
                if(this.editMode == false) {
                    return false;
                }
                var me = this
                var input = document.createElement("input");
                input.type = "file";
                input.accept = "image/*";
                input.id = "uploadInput";
                
                input.click();
                input.onchange = function (event) {
                    var file = event.target.files[0]
                    var reader = new FileReader();

                    reader.onload = function () {
                        var result = reader.result;
                        me.imageUrl = result;
                        me.value.photo = result;
                        
                    };
                    reader.readAsDataURL( file );
                };
            },
            edit() {
                this.editMode = true;
            },
            async save(){
                try {
                    var temp = null;

                    if(!this.offline) {
                        if(this.isNew) {
                            temp = await axios.post(axios.fixUrl('/operations'), this.value)
                        } else {
                            temp = await axios.put(axios.fixUrl(this.value._links.self.href), this.value)
                        }
                    }

                    if(this.value!=null) {
                        for(var k in temp.data) this.value[k]=temp.data[k];
                    } else {
                        this.value = temp.data;
                    }

                    this.editMode = false;
                    this.$emit('input', this.value);

                    if (this.isNew) {
                        this.$emit('add', this.value);
                    } else {
                        this.$emit('edit', this.value);
                    }

                    location.reload()

                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
                
            },
            async remove(){
                try {
                    if (!this.offline) {
                        await axios.delete(axios.fixUrl(this.value._links.self.href))
                    }

                    this.editMode = false;
                    this.isDeleted = true;

                    this.$emit('input', this.value);
                    this.$emit('delete', this.value);

                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            change(){
                this.$emit('input', this.value);
            },
            async operate(params) {
                try {
                    if(!this.offline) {
                        var temp = await axios.put(axios.fixUrl(this.value._links['operation'].href), params)
                        for(var k in temp.data) {
                            this.value[k]=temp.data[k];
                        }
                    }

                    this.editMode = false;
                    this.closeOperate();
                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            openOperate() {
                this.operateDiagram = true;
            },
            closeOperate() {
                this.operateDiagram = false;
            },
        },
    }
</script>

