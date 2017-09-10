function Account(){
}

Account.prototype.init = function(){
  var self = this;
  $(document).on("shown.bs.modal", "#account-modal", function(){
    $("#apply-account-button").on("click", self.saveAccountInfos());
  });
  $(document).on("hidden.bs.modal", "#account-modal", function(){
    $("#apply-account-button").off();
  });
}

Account.prototype.saveAccountInfos = function(){
  var self = this;
  return function(){
    if($("#account-form-password-input").val() === $("#account-form-password-confirm-input").val()){
      restAjax.authAjax({
        url:'auth/ChangePassword',
        type : 'POST',
        data:utils.serializeFormWithUser($("#account-form"), login.username),
        success:self.saveAccountInfosSuccess(),
        error:utils.displayError
      });
    }else{
      var businessStatus = {
        success:false,
        message:messages.differentPasswords
      }
      utils.notifAlert(businessStatus);
    }
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
