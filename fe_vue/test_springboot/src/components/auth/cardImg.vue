<template>
    <div class="w-[200px] h-[200px] flex-col overflow-hidden backdrop-blur-sm bg-white/40 rounded-lg flex items-center justify-center duration-300 ease-in-out group hover:bg-white">
        <img 
        :src="imageSource" 
        :alt="title" 
        alt="netflix" class="scale-75 duration-300 ease-in-out group-hover:scale-50">
        <p class="text-center font-bold text-xl capitalize duration-300 ease-in-out absolute -bottom-10 group-hover:bottom-5">{{ formattedTitle }}</p>
    </div>
</template>

<script>
export default {
    props: {
        title: {
        type: String,
        required: true,
        },
    },
    computed: {
        formattedTitle() {
            return this.title.replace(/_/g, ' ');
        },
        imageSource() {
        const images = {
            netflix: new URL('../../assets/netflix.png', import.meta.url).href,
            disney_hotstar: new URL('../../assets/disney_hotstar.png', import.meta.url).href,
            prime_video: new URL('../../assets/prime_video.png', import.meta.url).href,
            spotify: new URL('../../assets/spotify.png', import.meta.url).href,
            youtube: new URL('../../assets/youtube.png', import.meta.url).href,
        };
        return images[this.title];
        },
    },
};
</script>