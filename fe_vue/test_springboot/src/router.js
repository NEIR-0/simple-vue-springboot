import { createRouter, createWebHistory } from 'vue-router'
import WildCard from './views/WildCard.vue'
import Wallet from './views/wallet/wallet.vue'
import Register from './views/auth/Register.vue'
import About from './views/about/About.vue'
import CreateProducts from './views/admin/CreateProducts.vue'
import ListProducts from './views/admin/ListProducts.vue'
import UpdateProducts from './views/admin/UpdateProducts.vue'
import CustomerService from './views/admin/CustomerService.vue'
import Etalase from './views/etalase/Etalase.vue'
import Login from './views/auth/Login.vue'
import Tokenisasi from './views/tokenisasi/tokenisasi.vue'
import ListToken from './views/tokenisasi/ListToken.vue'
import DetailToken from './views/tokenisasi/DetailToken.vue'

const auth = (to, from, next) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  if (token && role) {
    next();
  } else {
    next('/login');
  }
}

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Etalase
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/about',
    name: 'about',
    component: About
  },
  {
    path: '/wallet',
    name: 'Wallet',
    component: Wallet
  },
  {
    path: '/token',
    name: 'token',
    beforeEnter: auth, 
    children: [
      {
        path: '',
        name: 'list-token',
        component: ListToken
      },
      {
        path: 'detail/:address',
        name: 'detail-token',
        component: DetailToken
      },
      {
        path: 'create',
        name: 'create-token',
        component: Tokenisasi
      },
    ]
  },
  // admin
  {
    path: '/admin',
    name: 'admin',
    beforeEnter: auth, 
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
      },
      {
        path: 'customer',
        name: 'customer',
        component: CustomerService
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
