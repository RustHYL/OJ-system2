import { createApp } from 'vue'
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import ArcoVue from '@arco-design/web-vue';
import '@arco-design/web-vue/dist/arco.css';
import App from './App.vue';
import router from './router';
import store from './store';
import '@/plugins/axios';
import '@/access/index';
import 'bytemd/dist/index.css';

const app = createApp(App);
app.use(store);
app.use(router);
app.use(ArcoVue);
app.use(ArcoVueIcon);
app.mount('#app');