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
import CreateProducts from './views/admin/CreateProducts.vue'
import ListProducts from './views/admin/ListProducts.vue'
import UpdateProducts from './views/admin/UpdateProducts.vue'

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
  // dashboard
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
  // admin
  {
    path: '/admin',
    name: 'admin',
    // beforeEnter: auth, 
    children: [
      {
        path: '',
        name: 'list-products',
        component: ListProducts
      },
      {
        path: 'create-product',
        name: 'create-product',
        component: CreateProducts
      },
      {
        path: 'update-product/:id',
        name: 'update-product',
        component: UpdateProducts
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
