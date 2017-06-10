function DisplayInfos(){
}

DisplayInfos.prototype.init = function(){
  this.loadInfos();
}

DisplayInfos.prototype.loadInfos = function(){
  var self = this;
  $.ajax({
    url:properties.serverUrl+'Infos',
    crossDomain:true,
    dataType:"json",
    success:self.displayInfos,
    error:utils.displayError
  });
}

DisplayInfos.prototype.displayInfos = function(data){
  if(data && data.date){
    var date = new Date(data.date);
    $("#update-date").text(utils.formatDate(date));
  }
}
