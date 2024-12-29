<template>
    <div class="w-fit h-fit flex flex-col items-center justify-center">
        <div class="w-full flex flex-col">
            <h1 class="font-bold text-3xl text-center capitalize text-green-500">{{title}}</h1>
            <div class="w-full px-20">
                <div class="w-full h-1 bg-red-500 mt-2"></div>
            </div>
            <!-- code -->
            <div class="w-full flex items-center justify-center my-20">
                <h1 class="font-extrabold text-7xl">{{responseMessage}}</h1>
            </div>
            <p class="float-right text-left capitalize">disclaimer:</p>
            <div class="w-full">
                <ul class="list-disc">
                    <li class="font-light text-base">Make sure to write down or use the code immediately, as it will only be displayed once.</li>
                    <li class="font-light text-base">To simplify future transactions, it is recommended to log in first.</li>
                    <li class="font-light text-base">If you encounter any issues, please contact our customer service via chat.</li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  props: {
        responseMessage: {
            type: String,
        },
        title: {
            type: String,
        },
    }
}
</script>
