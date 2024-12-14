<template>
    <form @submit.prevent="onSubmit" class="flex flex-col space-y-2">
        <input v-model="localForm.title" type="text" placeholder="Enter title" />
        <input v-model="localForm.description" type="text" placeholder="Enter description" />
        <input v-model="localForm.price" type="text" placeholder="Enter price" />
        <input v-model="localForm.image" type="text" placeholder="Enter image" />
        <button type="submit">Submit</button>
    </form>
</template>

<script>
export default {
  props: {
    formData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      localForm: { ...this.formData },
    };
  },
  methods: {
    onSubmit() {
      this.$emit('submit-form', this.localForm);
    },
  },
  watch: {
    formData: {
      handler(newValue) {
        this.localForm = { ...newValue };
      },
      deep: true,
    },
  },
};
</script>