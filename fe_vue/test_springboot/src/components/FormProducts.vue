<template>
  <div class="flex flex-col space-y-2 p-10 bg-white rounded-md shadow-md border-[1px]">
    <h1 class="text-xl w-full text-center font-light mb-3">Create Products</h1>
    <input
      class="h-10 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none"
      v-model="localForm.title"
      type="text"
      placeholder="Enter title..."
    />
    <input
      class="h-10 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none"
      v-model="localForm.price"
      type="text"
      placeholder="Enter price..."
      @keydown="allowOnlyNumbers"
    />
    <div class="w-full grid grid-cols-2 gap-4">
      <input
        class="h-10 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none"
        v-model="localForm.stock"
        type="text"
        placeholder="Enter stock..."
        @keydown="allowOnlyNumbersDecimal"
      />
      <div class="flex items-center justify-center space-x-2">
        <input
          class="h-10 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none"
          v-model="localForm.duration"
          type="text"
          placeholder="Enter duration..."
          @keydown="allowOnlyNumbersDecimal"
        />
        <p class="text-lg font-medium">/days</p>
      </div>
    </div>
    <textarea
      rows="4"
      cols="50"
      class="font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none resize-none"
      v-model="localForm.description"
      @input="limitCharacters"
      placeholder="Enter description..."
    ></textarea>
    <p class="text-sm text-gray-500 mt-1">{{ charCount }}/200 characters</p>

    <div v-if="!isUpdate" class="w-full">
      <div :class="`${imagesList?.length > 0 ? 'grid-cols-2' : 'grid-cols-1'} 'h-fit grid gap-4 mb-2`">
        <select
          v-if="imagesList?.length > 0 && !showImages"
          @change="selectImages"
          v-model="selectedImage"
          id="images"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
        >
          <option disabled value="Choose an Image Template">Choose an Image Template</option>
          <option v-for="item in imagesList" :value="item?.imageName">
            {{ item?.title }}
          </option>
        </select>
        <div
          @click="showAddNewImages"
          v-if="imagesList?.length > 0 && !showImages"
          class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-slate-200 hover:bg-slate-300 duration-300 ease-in-out cursor-pointer"
        >
          Add Another Image
        </div>
      </div>
  
      <div v-if="tempFiles?.length > 0 && showImages" class="w-full h-[300px]">
        <img class="w-full h-full" :src="showImg" alt="tempFiles" />
      </div>
      <div
        v-if="tempFiles?.length === 0 && showImages"
        class="gap-4 flex flex-col items-center w-full bg-white p-5 justify-center text-center border-2 border-slate-300 rounded-md"
        @dragover.prevent
        @drop="onDrop"
      >
        <div
          class="rounded-lg border-[1px] border-neutral-300 w-11 h-11 flex items-center justify-center"
        >
          <div class="w-6 h-6">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path
                d="M12 12.5858L16.2426 16.8284L14.8284 18.2426L13 16.415V22H11V16.413L9.17157 18.2426L7.75736 16.8284L12 12.5858ZM12 2C15.5934 2 18.5544 4.70761 18.9541 8.19395C21.2858 8.83154 23 10.9656 23 13.5C23 16.3688 20.8036 18.7246 18.0006 18.9776L18.0009 16.9644C19.6966 16.7214 21 15.2629 21 13.5C21 11.567 19.433 10 17.5 10C17.2912 10 17.0867 10.0183 16.8887 10.054C16.9616 9.7142 17 9.36158 17 9C17 6.23858 14.7614 4 12 4C9.23858 4 7 6.23858 7 9C7 9.36158 7.03838 9.7142 7.11205 10.0533C6.91331 10.0183 6.70879 10 6.5 10C4.567 10 3 11.567 3 13.5C3 15.2003 4.21241 16.6174 5.81986 16.934L6.00005 16.9646L6.00039 18.9776C3.19696 18.7252 1 16.3692 1 13.5C1 10.9656 2.71424 8.83154 5.04648 8.19411C5.44561 4.70761 8.40661 2 12 2Z"
                fill="#667085"
              />
            </svg>
          </div>
        </div>
        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          class="hidden"
          @change="onFileChange"
          id="fileInput"
        />
        <label for="fileInput" class="cursor-pointer gap-1 flex flex-col sm:w-full w-[200px]">
          <p class="text-[#108CDD] sm:text-sm text-xs font-medium">Click to upload</p>
          <p class="text-neutral-500 font-medium sm:text-sm text-xs">
            You can only upload PNG, JPG, JPEG
          </p>
        </label>
      </div>

      <div
        @click="showAddNewImages"
        v-if="imagesList?.length === 0 && !showCancelChangeImageInput && !showImages"
        class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-slate-200 hover:bg-slate-300 duration-300 ease-in-out cursor-pointer mt-2"
      >
        Add Image
      </div>

      <div
          @click="showCancelChangeImages"
          v-if="showChangeImageInput"
          class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-slate-200 hover:bg-slate-300 duration-300 ease-in-out cursor-pointer mt-2"
        >
          Cancel
        </div>
    </div>

    <div v-if="isUpdate" class="w-full space-y-2">
      <div v-if="!showChangeImageInput" class="w-full h-[300px]">
        <img class="w-full h-full" :src="`http://localhost:8081${localForm.image}`" alt="tempFiles" />
      </div>
      <div
        @click="showChangeImages"
        v-if="!showChangeImageInput"
        class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-slate-200 hover:bg-slate-300 duration-300 ease-in-out cursor-pointer"
      >
        Change Images
      </div>

      <div v-if="showChangeImageInput " class="w-full">
        <div :class="`${imagesList?.length > 0 ? 'grid-cols-2' : 'grid-cols-1'} 'h-fit grid gap-4`">
          <select
            v-if="imagesList?.length > 0 && !showImages"
            @change="selectImages"
            v-model="selectedImage"
            id="images"
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
          >
            <option disabled value="Choose an Image Template">Choose an Image Template</option>
            <option v-for="item in imagesList" :value="item?.imageName">
              {{ item?.title }}
            </option>
          </select>
          <div
            @click="showAddNewImages"
            v-if="!showImages"
            class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-slate-200 hover:bg-slate-300 duration-300 ease-in-out cursor-pointer"
          >
            Add Another Image
          </div>
        </div>
    
        <div v-if="tempFiles?.length > 0 && showImages" class="w-full h-[300px]">
          <img class="w-full h-full" :src="showImg" alt="tempFiles" />
        </div>
        <div
          v-if="tempFiles?.length === 0 && showImages"
          class="gap-4 flex flex-col items-center w-full bg-white p-5 justify-center text-center border-2 border-slate-300 rounded-md"
          @dragover.prevent
          @drop="onDrop"
        >
          <div
            class="rounded-lg border-[1px] border-neutral-300 w-11 h-11 flex items-center justify-center"
          >
            <div class="w-6 h-6">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path
                  d="M12 12.5858L16.2426 16.8284L14.8284 18.2426L13 16.415V22H11V16.413L9.17157 18.2426L7.75736 16.8284L12 12.5858ZM12 2C15.5934 2 18.5544 4.70761 18.9541 8.19395C21.2858 8.83154 23 10.9656 23 13.5C23 16.3688 20.8036 18.7246 18.0006 18.9776L18.0009 16.9644C19.6966 16.7214 21 15.2629 21 13.5C21 11.567 19.433 10 17.5 10C17.2912 10 17.0867 10.0183 16.8887 10.054C16.9616 9.7142 17 9.36158 17 9C17 6.23858 14.7614 4 12 4C9.23858 4 7 6.23858 7 9C7 9.36158 7.03838 9.7142 7.11205 10.0533C6.91331 10.0183 6.70879 10 6.5 10C4.567 10 3 11.567 3 13.5C3 15.2003 4.21241 16.6174 5.81986 16.934L6.00005 16.9646L6.00039 18.9776C3.19696 18.7252 1 16.3692 1 13.5C1 10.9656 2.71424 8.83154 5.04648 8.19411C5.44561 4.70761 8.40661 2 12 2Z"
                  fill="#667085"
                />
              </svg>
            </div>
          </div>
          <input
            ref="fileInput"
            type="file"
            accept="image/*"
            class="hidden"
            @change="onFileChange"
            id="fileInput"
          />
          <label for="fileInput" class="cursor-pointer gap-1 flex flex-col sm:w-full w-[200px]">
            <p class="text-[#108CDD] sm:text-sm text-xs font-medium">Click to upload</p>
            <p class="text-neutral-500 font-medium sm:text-sm text-xs">
              You can only upload PNG, JPG, JPEG
            </p>
          </label>
        </div>
      </div>

      <div
        @click="showCancelChangeImages"
        v-if="showChangeImageInput"
        class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-slate-200 hover:bg-slate-300 duration-300 ease-in-out cursor-pointer"
      >
        Cancel
      </div>
    </div>
    <div
      @click="handleSubmit"
      class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out cursor-pointer"
    >
      Submit
    </div>
  </div>
</template>

<script>
export default {
  props: {
    formData: {
      type: Object,
      required: true,
    },
    imagesList: {
      type: Array,
      required: true,
    },
    isUpdate: {
      type: Boolean,
    },
  },
  data() {
    return {
      localForm: { ...this.formData },
      tempFiles: [],
      selectedImage: "Choose an Image Template",
      showImg: "",
      maxSize: 4 * 1024 * 1024,
      acceptedFormats: ["image/jpeg", "image/jpg", "image/png", "image/gif"],
      showImages: false,
      showChangeImageInput: false,
      showCancelChangeImageInput: false,
    };
  },
  methods: {
    handleSubmit() {
      this.$emit("submit-form", {
        ...this.localForm,
        files: this.tempFiles,
      });
    },
    showAddNewImages() {
      this.showImages = !this.showImages;
      this.showChangeImageInput = !this.showChangeImageInput
      if (this.showImages) {
        this.localForm.imageStoreName = ""
      }
    },
    showChangeImages() {
      this.showChangeImageInput = !this.showChangeImageInput;
    },
    showCancelChangeImages() {
      this.showChangeImageInput = !this.showChangeImageInput;
      this.showImages = false
      this.tempFiles = [];
    },
    selectImages(event) {
      const selectedValue = event.target.value;
      this.localForm.imageStoreName = selectedValue;
    },
    handleFiles(files) {
      files.forEach((file) => {
        if (!this.acceptedFormats.includes(file.type)) {
          alert(`File type ${file.type} is not supported.`);
          return;
        }
        if (file.size > this.maxSize) {
          alert(`File size exceeds 4MB.`);
          return;
        }
        this.tempFiles = [file];

        const reader = new FileReader();
        reader.onload = (e) => {
          this.showImg = e.target.result;
        };
        reader.readAsDataURL(file);
      });
    },
    allowOnlyNumbers(e) {
      const key = e.key;
      if (!/[\d\s.]/.test(key) && !["Backspace", "Delete", "ArrowLeft", "ArrowRight", "Tab"].includes(key)) {
        e.preventDefault();
      }
    },
    allowOnlyNumbersDecimal(e) {
      const key = e.key;
      if (!/[\d\s]/.test(key) && !["Backspace", "Delete", "ArrowLeft", "ArrowRight", "Tab"].includes(key)) {
        e.preventDefault();
      }
    },
    limitCharacters() {
      if (this.localForm.description.length > 200) {
        this.localForm.description = this.localForm.description.slice(0, 200);
      }
    },
    onDrop(event) {
      event.preventDefault();
      const droppedFiles = Array.from(event.dataTransfer.files);
      this.handleFiles(droppedFiles);
    },
    onFileChange(event) {
      const selectedFiles = Array.from(event.target.files);
      this.handleFiles(selectedFiles);
    },
  },
  computed: {
    charCount() {
      return this.localForm.description.length;
    },
  },
  watch: {
    formData: {
      handler(newValue) {
        this.localForm = {
          ...newValue,
          imageStoreName: this.localForm.imageStoreName,
          files: this.localForm.files,
        };
      },
      deep: true,
    },
  },
};
</script>