import { createApp } from 'vue'
import ArcoVue from '@arco-design/web-vue';
import '@arco-design/web-vue/dist/arco.css';
import App from './App.vue';
import router from './router';
import store from './store';
import '@/plugins/axios';
import '@/access/index';

const app = createApp(App);
app.use(store);
app.use(router);
app.use(ArcoVue);
app.mount('#app');