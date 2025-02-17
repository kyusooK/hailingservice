<template>
    <v-app id="inspire">
        <reservation-notification-app>
            <reservation-notification/>
        </reservation-notification-app>
        <div>
            <v-app-bar color="primary" app clipped-left flat>
                <v-toolbar-title>
                    <span class="second-word font uppercase"
                        style="font-weight:700;"
                    >
                        <v-app-bar-nav-icon
                            @click="openSideBar()"
                            style="z-index:1;
                            height:56px;
                            width:30px;
                            margin-right:10px;
                            font-weight:300;
                            font-size:55px;"
                        >
                            <div style="line-height:100%;">≡</div>
                        </v-app-bar-nav-icon>
                    </span>
                </v-toolbar-title>
                <span v-if="urlPath!=null" 
                    class="mdi mdi-home" 
                    key="" 
                    to="/" 
                    @click="goHome()"
                    style="margin-left:10px; font-size:20px; cursor:pointer;"
		        ></span> 
                <v-spacer></v-spacer>

            </v-app-bar>

            <v-navigation-drawer app clipped flat v-model="sideBar">
                <v-list>
                    <v-list-item
                        class="px-2"
                        key="users"
                        to="/users/users"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700;"
                    >
                        사용자정보
                    </v-list-item>


                    <v-list-item
                        class="px-2"
                        key="operations"
                        to="/dispatches/operations"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700;"
                    >
                        운행정보
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="matchings"
                        to="/dispatches/matchings"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700;"
                    >
                        매칭정보
                    </v-list-item>


                    <v-list-item
                        class="px-2"
                        key="drivers"
                        to="/drivers/drivers"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700;"
                    >
                        운전자정보
                    </v-list-item>



                    <v-list-item
                        class="px-2"
                        key="operationRecords"
                        to="/operationstatistics/operationRecords"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700;"
                    >
                        운행 기록
                    </v-list-item>
                </v-list>
            </v-navigation-drawer>
        </div>

        <v-main>
            <v-container style="padding:0px;" v-if="urlPath" fluid>
                <router-view></router-view>
            </v-container>
            <v-container style="padding:0px;" v-else fluid>
                <div style="width:100%; margin:0px 0px 20px 0px; position: relative;">
                    <v-img style="width:100%; height:300px;"
                        src="/image/hailingservice.png"
                    ></v-img>
                    <div class="App-main-text-overlap"></div>
                    <div class="App-sub-text-overlap"></div>
                </div>
                <v-row class="pa-0 ma-0">
                    <v-col cols="3" class="pa-0 pa-0" v-for="(aggregate, index) in aggregate" :key="index">
                        <div 
                            class="flip-card pa-4"
                        >
                            <v-card
                                :key="aggregate.key"
                                :to="aggregate.route"
                                @click="changeUrl()"
                                class="mx-auto main-card pa-4"
                                style="text-align: center; border-radius: 10px;"
                                outlined
                            >
                                <div class="d-flex justify-center" style="width:120px; height:120px; border-radius: 10px; margin: 0 auto; background-color:white;">
                                    <v-img style="width:100%; height:100%; object-fit:contain; border-radius: 10px;" :src="aggregate.ImageUrl"></v-img>
                                </div>
                                <div style="text-align: center;">
                                    <h2 class="main-card-title">{{ aggregate.title }}</h2>
                                </div>
                            </v-card>
                        </div>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>

export default {
    name: "App",
    data: () => ({
        useComponent: "",
        drawer: true,
        components: [],
        sideBar: true,
        urlPath: null,
        flipped: [],
        ImageUrl: '',
        aggregate: [
            { 
                title: '사용자정보', 
                description: 'User을 관리하는 화면입니다.', 
                key: 'users', 
                route: '/users/users',
                ImageUrl: '/image/id-card.svg',
            },
            { 
                title: '운행정보', 
                description: 'Operation을 관리하는 화면입니다.', 
                key: 'operations', 
                route: '/dispatches/operations', 
                ImageUrl: '/image/map.svg',
            },
            { 
                title: '매칭정보', 
                description: 'Matching을 관리하는 화면입니다.', 
                key: 'matchings',
                route: '/dispatches/matchings',
                ImageUrl: '/image/share.svg',
            },
            { 
                title: '운전자정보', 
                description: 'Driver을 관리하는 화면입니다.', 
                key: 'drivers',
                route: '/drivers/drivers',
                ImageUrl: '/image/customer-review.svg',
            },
            { 
                title: '운행 기록', 
                description: 'OperationRecord을 관리하는 화면입니다.', 
                key: 'operationRecords',
                route: '/operationstatistics/operationRecords',
                ImageUrl: '/image/checklist.svg',
            },
            
        ],
    }),
    
    async created() {
      var path = document.location.href.split("#/")
      this.urlPath = path[1];

    },
    watch: {
        cards(newCards) {
            this.flipped = new Array(newCards.length).fill(false);
        },
    },

    mounted() {
        var me = this;
        me.components = this.$ManagerLists;
    },

    methods: {
        openSideBar(){
            this.sideBar = !this.sideBar
        },
        changeUrl() {
            var path = document.location.href.split("#/")
            this.urlPath = path[1];
            this.flipped.fill(false);
        },
        goHome() {
            this.urlPath = null;
        },
        flipCard(index) {
            this.$set(this.flipped, index, true);
        },
        unflipCard(index) {
            this.$set(this.flipped, index, false);
        },
    }
};
</script>
<style>
.notifications-container {
    position: absolute;
    top: 0;
    right: 10%;
    z-index: 999999999;
    pointer-events: none;
    width: auto;
    height: auto;
}

.mac-notification {
    pointer-events: auto;
    position: fixed;
    right: 20px;
    width: 300px;
    background: rgba(50, 50, 50, 0.95);
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    overflow: hidden;
    margin-bottom: 20px;
    z-index: 999999;
}

.notification-header {
    padding: 12px 15px;
    background: rgba(60, 60, 60, 0.95);
    color: white;
    font-weight: 500;
    display: flex;
    align-items: center;
}

.notification-content {
    padding: 15px;
    color: white;
    font-size: 14px;
}

.close-btn {
    margin-left: auto;
}

.slide-notification-enter-active,
.slide-notification-leave-active {
    transition: all 0.3s ease;
}

.slide-notification-enter-from {
    transform: translateX(100%);
    opacity: 0;
}

.slide-notification-leave-to {
    transform: translateX(100%);
    opacity: 0;
}

.slide-notification-move {
    transition: transform 0.3s ease;
}

.notification-time {
    font-size: 12px;
    color: #999;
    margin-top: 8px;
    text-align: right;
}
</style>
