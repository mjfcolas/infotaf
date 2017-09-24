Vue.filter('date', function(d, format){
  return utils.formatDate(new Date(d), format);
});
