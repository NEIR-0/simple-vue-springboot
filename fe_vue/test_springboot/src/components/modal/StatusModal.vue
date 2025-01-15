<script setup lang="ts">
import { VueFinalModal } from 'vue-final-modal'

defineProps<{
    title?: string,
    responseMessage?: string
    isShowButton?: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', modelValue: boolean): void
}>()
</script>

<template>
  <VueFinalModal
    class="flex justify-center items-center"
    @update:model-value="val => emit('update:modelValue', val)"
  >
    <div class="w-fit h-fit p-20 rounded-md bg-white relative flex flex-col items-center justify-center">
        <button v-if="!isShowButton" class="absolute top-5 right-5 text-xl text-slate-500 font-bold duration-300 ease-in-out hover:text-slate-800" @click="emit('update:modelValue', false)">X</button>
        <div class="w-[250px] h-[250px] flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" width="100%" height="100%">
            <g transform="translate(50, 50)">
              <circle
                cx="0"
                cy="0"
                r="40"
                stroke="#4caf50"
                stroke-width="8"
                fill="none"
                stroke-dasharray="200"
                stroke-dashoffset="0"
                stroke-linecap="round"
              >
                <animateTransform
                  attributeName="transform"
                  type="rotate"
                  from="0 0 0"
                  to="360 0 0"
                  dur="1s"
                  repeatCount="indefinite"
                />
              </circle>
            </g>
          </svg>
        </div>
        <div class="w-full h-10 flex flex-col items-center justify-center space-y-2 mt-10">
          <h1 class="text-lg font-semibold text-slate-600">{{ title }}</h1>
          <p class="text-sm font-light text-slate-600">{{responseMessage}}</p>
        </div>
    </div>
  </VueFinalModal>
</template>