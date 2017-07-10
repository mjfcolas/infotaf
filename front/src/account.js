function Account(){
}

Account.prototype.init = function(){
  var self = this;
  $(document).on("shown.bs.modal", function(){
    $("#apply-account-button").on("click", self.saveAccountInfos());
  });
  $(document).on("hidden.bs.modal", function(){
    $("#apply-account-button").off();
  });
}

Account.prototype.saveAccountInfos = function(){
  var self = this;
  return function(){
    $.ajax({
      url:properties.serverUrl+'auth/ChangePassword',
      beforeSend:function(xhr) {
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
        xhr.setRequestHeader('X-Authorization', "Bearer " + localStorage.jwtToken);
      },
      type : 'POST',
      dataType:"json",
      data:utils.serializeFormWithUser($("#account-form"), login.username),
      success:self.saveAccountInfosSuccess(),
      error:utils.displayError
    });
  }
}

Account.prototype.saveAccountInfosSuccess = function(){
  var self = this;
  return function(businessStatus){
    if(businessStatus.success){
      $("#account-form-password-input").val("");
      $("#account-form-password-confirm-input").val("");
    }
    utils.notifAlert(businessStatus);
  }
}
