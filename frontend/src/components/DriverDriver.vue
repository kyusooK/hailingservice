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
            운전자정보 # {{decode(value._links.self.href.split("/")[value._links.self.href.split("/").length - 1])}}
        </v-card-title >
        <v-card-title v-else>
            운전자정보
        </v-card-title >        

        <v-card-text class="pb-0" style="background-color: white;">
            <div v-if="editMode">   
                <String label="이메일" v-model="value.email" :editMode="editMode" :inputUI="''"/>
                <String label="운전면허증" v-model="value.driverLicenseNumber" :editMode="editMode" :inputUI="''"/>
            </div>
            <div v-else>
                <div>
                    <v-divider class="my-2"></v-divider>
                    <div class="sub-title">운전자 정보</div>
                    <v-row class="sub-text ma-0 pa-0">
                        <div style="font-weight: 500;">이메일</div>
                        <v-spacer></v-spacer>
                        <div>{{ value.email }}</div>
                    </v-row>
                    <v-row class="sub-text ma-0 pa-0">
                        <div style="font-weight: 500;">운전면허증</div>
                        <v-spacer></v-spacer>
                        <div>{{ value.driverLicenseNumber }}</div>
                    </v-row>
                    <v-divider class="my-2"></v-divider>
                </div>

                <div>
                    <div>
                        <div class="sub-title">운전자 상태</div>
                        <v-row class="sub-text ma-0 pa-0">
                            <div style="font-weight: 500;">운전자 승인여부</div>
                            <v-spacer></v-spacer>
                            <Boolean label="" v-model="value.isApproved" :editMode="false" :inputUI="''"/>
                        </v-row>
                        <v-row class="sub-text ma-0 pa-0">
                            <div style="font-weight: 500;">호출요청 가능여부</div>
                            <v-spacer></v-spacer>
                            <Boolean label="" v-model="value.isHailing" :editMode="false" :inputUI="''"/>
                        </v-row>

                        <v-row class="ma-0 pa-0">
                            <v-spacer></v-spacer>
                            <v-btn
                                v-if="!editMode"
                                color="primary"
                                text
                                @click="confirmLicense"
                            >
                                운전자확인
                            </v-btn>
                            <v-btn
                                v-if="!editMode"
                                color="primary"
                                text
                                @click="openChangeOperationstatus"
                            >
                                운행상태변경
                            </v-btn>
                        </v-row>
                    </div>
                    <v-divider class="my-2"></v-divider>
                </div>
                <div>
                    <div class="sub-title">운행 정보</div>
                    <v-row class="sub-text ma-0 pa-0">
                        <div style="font-weight: 500;">현재 위치</div>
                        <v-spacer></v-spacer>
                        <div>{{ value.driverLocation }}</div>
                    </v-row>
                    <v-row class="sub-text ma-0 pa-0">
                        <div style="font-weight: 500;">호출 요청 ID</div>
                        <v-spacer></v-spacer>
                        <div>{{ value.operationRequestId }}</div>
                    </v-row>
                    <div class="sub-text ma-0 pa-0">
                        <v-divider class="my-2"></v-divider>
                        <div style="font-weight: 500;">호출 요청서</div>
                        <v-spacer></v-spacer>
                        <div v-html="formattedOperationRequestForm"></div>
                        <v-divider class="my-2"></v-divider>
                    </div>
                    <v-row class="sub-text ma-0 pa-0">
                        <div style="font-weight: 500;">운행 정보</div>
                        <v-spacer></v-spacer>
                        <div>{{ value.operationInfo }}</div>
                    </v-row>
                    <v-divider class="my-2"></v-divider>
                </div>
            </div>
        </v-card-text>

        <v-card-actions style="background-color: white;">
            <v-spacer></v-spacer>
            <div v-if="!editMode">
                <v-btn
                    v-if="!editMode"
                    color="primary"
                    text
                    @click="acceptCarhailing"
                >
                    차량호출 수락
                </v-btn>
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
            </div>
            <div v-else>
                <v-btn
                    color="primary"
                    text
                    @click="save"
                >
                    운전자정보등록
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
            <v-dialog v-model="changeOperationstatusDiagram" width="500">
                <ChangeOperationstatusCommand
                    @closeDialog="closeChangeOperationstatus"
                    @changeOperationstatus="changeOperationstatus"
                ></ChangeOperationstatusCommand>
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
        name: 'DriverDriver',
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
            changeOperationstatusDiagram: false,
        }),
	async created() {
            if(!this.reviewData.itemId){
                this.reviewData.itemId = this.decode(this.value._links.self.href.split("/")[this.value._links.self.href.split("/").length - 1])
            }
        },
        computed: {
            formattedOperationRequestForm() {
                return this.value.operationRequestForm.replace(/\n/g, '<br>');
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
                            temp = await axios.post(axios.fixUrl('/drivers'), this.value)
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
            async confirmLicense() {
                try {
                    if(!this.offline) {
                        var temp = await axios.put(axios.fixUrl(this.value._links['confirmlicense'].href))
                        for(var k in temp.data) {
                            this.value[k]=temp.data[k];
                        }
                    }

                    this.editMode = false;
                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            async acceptCarhailing() {
                try {
                    if(!this.offline) {
                        var temp = await axios.put(axios.fixUrl(this.value._links['acceptcarhailing'].href))
                        for(var k in temp.data) {
                            this.value[k]=temp.data[k];
                        }
                    }

                    this.editMode = false;
                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            async changeOperationstatus(params) {
                try {
                    if(!this.offline) {
                        var temp = await axios.put(axios.fixUrl(this.value._links['changeoperationstatus'].href), params)
                        for(var k in temp.data) {
                            this.value[k]=temp.data[k];
                        }
                    }

                    this.editMode = false;
                    this.closeChangeOperationstatus();
                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            openChangeOperationstatus() {
                this.changeOperationstatusDiagram = true;
            },
            closeChangeOperationstatus() {
                this.changeOperationstatusDiagram = false;
            },
        },
    }
</script>

