<script setup lang="ts">
import { VueFinalModal } from 'vue-final-modal'
import Transaction from './modal/transaction.vue';
import { onMounted, ref } from 'vue';

defineProps<{
    responseMessage?: string
    isShowButton?: boolean
    title?: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: boolean): void
}>()

const isButtonDisabled = ref(true);
onMounted(() => {
  setTimeout(() => {
    isButtonDisabled.value = false;
  }, 5000); // Tombol akan dinonaktifkan selama 5 detik
});
</script>

<template>
  <VueFinalModal
    class="flex justify-center items-center"
    @update:model-value="val => emit('update:modelValue', val)"
  >
    <div class="w-fit h-fit p-20 rounded-md bg-white relative flex flex-col items-center justify-center">
        <button v-if="!isShowButton" class="absolute top-5 right-5 text-xl text-slate-500 font-bold duration-300 ease-in-out hover:text-slate-800" @click="emit('update:modelValue', false)">X</button>
        <Transaction :title="title" :responseMessage="responseMessage"/>

        <div v-if="isShowButton" class="w-full h-fit mt-10">
            <button 
            :disabled="isButtonDisabled"
            :class="isButtonDisabled 
                ? 'w-full h-14 p-5 text-2xl flex items-center justify-center rounded-md font-medium text-slate-300 bg-slate-200 border-2 border-slate-500 duration-300 ease-in-out tracking-widest'
                : 'w-full h-14 p-5 text-2xl flex items-center justify-center rounded-md font-medium text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out tracking-widest'"
            @click="emit('update:modelValue', false)"
            >
            I Acknowledge and Agree
            </button>
        </div>
    </div>
  </VueFinalModal>
</template>