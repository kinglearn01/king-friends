import {createApp} from 'vue'
import App from './App.vue'
import router from '../src/config/index.ts'
import axios from 'axios'
import 'vant/lib/index.css';
import {createPinia} from 'pinia'

import piniaPersist from 'pinia-plugin-persist'

const pinia = createPinia();
pinia.use(piniaPersist);

axios.defaults.withCredentials = true


createApp(App).use(router).use(pinia).mount('#app')
