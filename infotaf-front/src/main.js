// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Vue from 'vue'
import App from './App'
import InitFilters from '@/helpers/customFilters.js'
import loginHelper from '@/helpers/login.js'

Vue.config.productionTip = false

InitFilters.initFilters();
loginHelper.init();

/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<App/>',
  components: { App }
})
