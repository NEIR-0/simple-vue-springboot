import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './style.css'
import { createVfm } from 'vue-final-modal'
import 'vue-final-modal/style.css'

const app = createApp(App)
const vfm = createVfm()

app.use(router)
app.use(vfm)
app.mount('#app')