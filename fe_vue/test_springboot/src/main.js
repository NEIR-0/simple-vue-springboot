import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './style.css'
import { createVfm } from 'vue-final-modal'
import 'vue-final-modal/style.css'
import ToastPlugin from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-bootstrap.css';


const app = createApp(App)
const vfm = createVfm()

app.use(router)
app.use(vfm)
app.use(ToastPlugin);
app.mount('#app')