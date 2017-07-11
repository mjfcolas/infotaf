function Login(){
  this.user = null;
  this.loggedIn = false;
  this.roles = [];
  this.expiration = null;
  this.refreshExpiration = null;
}

Login.prototype.init = function(){
  var self = this;
  if(localStorage.jwtToken && localStorage.jwtToken !== "null"){
    this.storeToken();
    this.retrieveUsername();
  }
  $("#logout-button").on("click", this.logout());

  $(document).on("shown.bs.modal", function(){
    $("#login-button").on("click", self.login());
  });
  $(document).on("hidden.bs.modal", function(){
    $("#login-button").off();
  });
}

Login.prototype.logout = function(){
  var self = this;
  return function(){
    var businessStatus = {
      success: true,
      message: "Déconnexion réussie"
    }
    utils.notifAlert(businessStatus);
    self.manageLoggedUser(true);
  }
}

Login.prototype.login = function(){
  var self = this;
  return function(){
    $.ajax({
      url:properties.serverUrl+'login',
      beforeSend:function(xhr) {
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
      },
      type : 'POST',
      dataType:"json",
      contentType: 'application/json',
      data:JSON.stringify({
        username:$("#login-form-nums-input").val(),
        password:$("#login-form-password-input").val()
      }),
      success:self.successLoginCallback(),
      error:self.handleConnectionError("Connexion échouée")
    });
  }
}

Login.prototype.refresh = function(){
  var self = this;
  if(localStorage.jwtRefreshToken !== "undefined"){
    $.ajax({
      url:properties.serverUrl+'auth/token',
      beforeSend:function(xhr) {
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
        xhr.setRequestHeader('X-Authorization', "Bearer " + localStorage.jwtRefreshToken);
      },
      type : 'GET',
      dataType:"json",
      success:self.successRefreshCallback(),
      error:self.handleConnectionError("Actualisation du token échouée")
    });
  }
}

Login.prototype.checkRefresh = function(){
  var self = this;
  $(document).one("got-server-timestamp", function(e, ts){
    console.log("TS SERVEUR", ts, "TS EXP", self.expiration);
    if(self.expiration < ts + 60 && self.expiration > ts){//Si l'expiration du jeton arrive dans moins d'une minute, mais n'est pas passé on le refresh
      self.refresh();
    }else if(self.expiration < ts){//Déconnexion si l'expiration du jeton a été atteinte
      self.manageLoggedUser(true);
    }
  })
  this.serverTimestamp();
}

Login.prototype.serverTimestamp = function(){
  var self = this;
  $.ajax({
    url:properties.serverUrl+"Timestamp",
    dataType:"json",
    success:function(businessStatus){
      var ts = parseInt(businessStatus.value);
      $(document).trigger("got-server-timestamp", [ts]);
    },
    error:utils.displayError
  });
}

Login.prototype.storeToken = function(token, refreshToken){
  if(token){
    localStorage.setItem('jwtToken', token);
  }
  if(refreshToken){
    localStorage.setItem('jwtRefreshToken', refreshToken);
  }
  if(localStorage.jwtToken && localStorage.jwtToken !== "null"){
    var splitedToken = localStorage.jwtToken.split(".");
    var datas = JSON.parse(atob(splitedToken[1]));
    this.roles = datas.scopes;
    this.expiration = datas.exp;
  }
  if(localStorage.jwtRefreshToken && localStorage.jwtRefreshToken !== "null"){
    var splitedRefreshToken = localStorage.jwtRefreshToken.split(".");
    var datas = JSON.parse(atob(splitedRefreshToken[1]));
    this.refreshExpiration = datas.exp;
  }
}

Login.prototype.successRefreshCallback = function(){
  var self = this;
  return function(data){
    self.storeToken(data.token);
  }
}

Login.prototype.successLoginCallback = function(){
  var self = this;
  return function(data){
    self.storeToken(data.token, data.refreshToken);
    self.retrieveUsername();
    $("#login-form-nums-input").val("");
    $("#login-form-password-input").val("");
    $('#login-modal').modal('hide');
    var businessStatus = {
      success: true,
      message: "Connexion réussie"
    }
    utils.notifAlert(businessStatus);
  }
}

Login.prototype.handleConnectionError = function(message){
  var self = this;
  return function(){
    $("#login-form-password-input").val("");
    self.loggedIn = false;
    var businessStatus = {
      success: false,
      message: message
    }
    utils.notifAlert(businessStatus);
  }
}

Login.prototype.retrieveUsername = function(){
  var self = this;
  restAjax.authAjax({
    url:'auth/username',
    success:self.retrieveUsernameCallback(false),
    error:self.retrieveUsernameCallback(true)
  });
}

Login.prototype.retrieveUsernameCallback = function(logout){
  var self=this;
  return function(data){
    self.manageLoggedUser(logout, data.username);
  }
}

Login.prototype.manageLoggedUser = function(logout, username){
  if(logout || !username){
    localStorage.setItem('jwtToken', null);
    localStorage.setItem('jwtRefreshToken', null);
    this.loggedIn = false;
    this.username = "";
    this.expiration = null;
    this.refreshExpiration = null;
    this.roles = [];
    $("#login-info").text("Non connecté");
    $(".logged-out").show();
    $(".logged-in").hide();
  }else{
    this.loggedIn = true;
    this.username = username;
    $("#login-info").text("Connecté : " + login.username);
    $(".logged-out").hide();
    $(".logged-in").show();
  }
  displayTaf.renderKifekoi();
}
