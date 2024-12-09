import { createRouter, createWebHistory } from 'vue-router'
import Homepage from './views/Homepage.vue'
import Dashboard from './views/dashboard/Dashboard.vue'
import WildCard from './views/WildCard.vue'
import Wallet from './views/wallet/wallet.vue'
import MessageComponent from './views/msgTest/MessageComponent.vue'
import Login from './views/auth/login.vue'
import Register from './views/auth/Register.vue'
import About from './views/about/About.vue'
import Etalase from './views/etalase/etalase.vue'

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
    path: '/testmsg',
    name: 'testmsg',
    component: MessageComponent
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
      },
      {
        path: 'etalase',
        name: 'etalase',
        component: Etalase
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
