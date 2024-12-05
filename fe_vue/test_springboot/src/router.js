import { createRouter, createWebHistory } from 'vue-router'
import About from './views/about/about.vue'
import Homepage from './views/Homepage.vue'
import Dashboard from './views/dashboard/Dashboard.vue'
import Login from './views/login/login.vue'
import Register from './views/login/Register.vue'

const auth = (to, from, next) => {
  const token = localStorage.getItem('token');
  console.log(token, "!@>#>!@#>!");
  
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
    // beforeEnter: auth, 
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
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
