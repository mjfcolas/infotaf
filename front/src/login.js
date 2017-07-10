function Login(){
  this.user = null;
  this.loggedIn = false;
}

Login.prototype.init = function(){
  var self = this;
  if(localStorage.jwtToken){
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
      error:self.handleConnectionError()
    });
  }
}


Login.prototype.successLoginCallback = function(){
  var self = this;
  return function(data){
    localStorage.setItem('jwtToken', data.token);
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

Login.prototype.handleConnectionError = function(){
  var self = this;
  return function(){
    $("#login-form-password-input").val("");
    var businessStatus = {
      success: false,
      message: "Connexion échouée"
    }
    utils.notifAlert(businessStatus);
  }
}

Login.prototype.retrieveUsername = function(){
  var self = this;
  $.ajax({
    url:properties.serverUrl+'auth/username',
    beforeSend:function(xhr) {
      xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
      xhr.setRequestHeader('X-Authorization', "Bearer " + localStorage.jwtToken)
    },
    dataType:"json",
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
    this.loggedIn = false;
    this.username = "";
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
