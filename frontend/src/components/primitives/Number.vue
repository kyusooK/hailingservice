<template>
    <div>
        <div v-if="editMode" style="margin-top:-20px;">
            <v-text-field type="number" :label="label" v-model="value" @change="change"/>
        </div>
        <div v-else>
            {{ label }} : {{ formattedValue }}
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Number',
        props: {
            value: {
                type: Number,
                default: 0
            },
            editMode: Boolean,
            label: String
        },
        computed: {
            formattedValue() {
                if (this.label.includes('시간')) {
                    // 초를 시간 단위로 변환
                    const hours = Math.floor(this.value / 3600);
                    const minutes = Math.floor((this.value % 3600) / 60);
                    const seconds = this.value % 60;
                    let result = '';
                    if (hours > 0) {
                        result += `${hours}시간 `;
                    }
                    if (minutes > 0 || hours > 0) {
                        result += `${minutes}분 `;
                    }
                    result += `${seconds}초`;
                    return result.trim();
                } else if (this.label.includes('거리')) {
                    // 거리를 km 단위로 변환 (예: 2984 -> 2.984 km)
                    return `${(this.value / 1000).toFixed(3)} km`;
                }
                return this.value;
            }
        },
        methods: {
            change() {
                this.$emit("input", Number(this.value));
            }
        }
    }
</script>
