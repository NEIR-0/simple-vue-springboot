import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import Form from './views/form.vue'
import Update from './views/Update.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/form',
    name: 'Form',
    component: Form
  },
  {
    path: '/update/:id',
    name: 'Update',
    component: Update
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
