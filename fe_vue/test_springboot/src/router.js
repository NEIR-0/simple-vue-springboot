import { createRouter, createWebHistory } from 'vue-router'
import About from './views/about/about.vue'
import Homepage from './views/Homepage.vue'
import Dashboard from './views/dashboard/Dashboard.vue'
import Login from './views/login/login.vue'
import Register from './views/login/Register.vue'
import WildCard from './views/WildCard.vue'
import Wallet from './views/wallet/wallet.vue'

const auth = (to, from, next) => {
  const token = localStorage.getItem('token');
  if (token) {
    next();
  } else {
    next('/login');
  }
}

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Homepage
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/dashboard',
    name: 'dashboardLayout',
    beforeEnter: auth, 
    children: [
      {
        path: '',
        name: 'dashboard',
        component: Dashboard
      },
      {
        path: 'about',
        name: 'about',
        component: About
      },
      {
        path: 'wallet',
        name: 'wallet',
        component: Wallet
      }
    ]
  },

   // Wildcard untuk 404
   {
    path: '/:pathMatch(.*)*', // Ini menangkap semua route yang tidak terdefinisi
    name: 'NotFound',
    component: WildCard // Komponen untuk halaman 404
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
