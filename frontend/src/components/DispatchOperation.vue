<template>
    <v-card outlined>
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
            <OperationStatus offline label="운행상태" v-model="value.operationStatus" :editMode="editMode" @change="change"/>
            <UserId offline label="사용자 ID" v-model="value.userId" :editMode="editMode" @change="change"/>
            <DriverId v-if="!editMode" offline label="운전자 ID" v-model="value.driverId" :editMode="false" @change="change"/>
            <Number v-if="!editMode" label="운행요금" v-model="value.fee" :editMode="false" :inputUI="''"/>
            <String v-if="!editMode" label="PaymentId" v-model="value.paymentId" :editMode="false" :inputUI="''"/>
            <String v-if="!editMode" label="PaymentStatus" v-model="value.paymentStatus" :editMode="false" :inputUI="''"/>
        </v-card-text>
        <v-card-actions style="background-color: white;">
            <v-spacer></v-spacer>
            <div v-if="!editMode">
                <v-row>
                    <payment-system-app>
                        <payment-system
                            service-type="pay"
                            :request-info="JSON.stringify(paymentData)" 
                            buyer-info-mode="true"
                        ></payment-system>
                    </payment-system-app>
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
                    v-if="!editMode"
                    color="primary"
                    text
                    @click="openCompleteOperation"
                >
                    운행완료
                </v-btn>
                <v-dialog v-model="completeOperationDiagram" width="500">
                    <CompleteOperationCommand
                        @closeDialog="closeCompleteOperation"
                        @completeOperation="completeOperation"
                    ></CompleteOperationCommand>
                </v-dialog>
                <payment-system-app v-if="value.paymentId">
                        <payment-system
                            service-type="receipt"
                            :request-info="JSON.stringify(paymentData)" 
                            buyer-info-mode="true"
                        ></payment-system>
                    </payment-system-app>
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
        
        <v-snackbar
            v-model="snackbar.status"
            :top="true"
            :timeout="snackbar.timeout"
            color="error"
        >
            {{ snackbar.text }}
            <v-btn dark text @click="snackbar.status = false">
                닫기
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
         
            paymentData: null,
            operateDiagram: false,
            completeOperationDiagram: false
        }),
	    async created() {
            if(!this.paymentData){
                this.paymentData = {
                    itemId : this.decode(this.value._links.self.href.split("/")[this.value._links.self.href.split("/").length - 1]),
                    price: this.value.fee,
                    name: "운행요금",
                    buyerId: "user",
                    buyerEmail: "user@gmail.com",
                    buyerTel: "01012345678",
                    buyerName: "user"
                }
            }
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
            async completeOperation(params) {
                try {
                    if(!this.offline) {
                        var temp = await axios.put(axios.fixUrl(this.value._links['operation'].href), params)
                        for(var k in temp.data) {
                            this.value[k]=temp.data[k];
                        }
                    }

                    this.editMode = false;
                    this.closeCompleteOperation();
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
            openOperate() {
                this.operateDiagram = true;
            },
            closeOperate() {
                this.operateDiagram = false;
            },
            openCompleteOperation() {
                this.completeOperationDiagram = true;
            },
            closeCompleteOperation() {
                this.completeOperationDiagram = false;
            },
        },
    }
</script>

