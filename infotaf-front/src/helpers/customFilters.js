import Vue from 'vue'
import utils from '@/helpers/utils.js'

function Filters(){
}

Filters.prototype.initFilters = function(){
  Vue.filter('date', function(d, format){
    return utils.formatDate(new Date(d), format);
  });

  Vue.filter('euros', function(val){
    return val || val === 0 ? val.toFixed(2) + " €" : "";
  });
}

export default new Filters();
