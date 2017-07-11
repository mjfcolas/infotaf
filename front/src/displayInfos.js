function DisplayInfos(){
}

DisplayInfos.prototype.init = function(){
  this.loadInfos();
}

DisplayInfos.prototype.loadInfos = function(){
  var self = this;
  restAjax.ajax({
    url:'Infos',
    success:self.displayInfos,
    error:utils.displayError
  });
}

DisplayInfos.prototype.displayInfos = function(data){
  if(data && data.date){
    var date = new Date(data.date);
    $("#update-date").text(utils.formatDate(date));
    if(login.loggedIn){
      $("#login-info").text("Connecté : " + login.username);
    }
    else{
      $("#login-info").text("Non connecté");
    }
  }
}
