function Administration(){
}

Administration.prototype.init = function(){
  var self = this;
  $(document).on("shown.bs.modal", "#administration-modal", function(){
    self.loadAdmins();
    $("#new-admin-button").on("click", self.addAdmin());
    $("#admin-list").on("click", ".delete-pg", self.deleteAdmin());
  });
  $(document).on("hidden.bs.modal", "#administration-modal", function(){
    $("#admin-list").html("");
    $("#admin-list").off();
    $("#new-admin-button").off();
  });
}

Administration.prototype.deleteAdmin = function(){
  var self = this;
  return function(){
    restAjax.authAjax({
      url:'auth/deleteAdmin',
      data:{
        pgId:$(this).attr("data-pg-id")
      },
      success:self.deleteAdminSuccess($(this).attr("data-pg-id")),
      error:utils.displayError
    });
  }
}

Administration.prototype.addAdmin = function(){
  var self = this;
  return function(){
    $("#new-admin-button").prop("disabled", "true");
    restAjax.authAjax({
      url:'auth/addAdmin',
      type:'POST',
      data:$("#add-admin-form").serialize(),
      success:self.addAdminSuccess(),
      error:utils.displayErrorAndEnableButton("#new-admin-button")
    });
  }
}

Administration.prototype.addAdminSuccess = function(){
  var self = this;
  return function(businessStatus){
    $("#new-admin-button").removeAttr("disabled");
    if(businessStatus.success){
      self.addAdminLine(businessStatus.object);
    }
    utils.notifAlert(businessStatus);
  }
}

Administration.prototype.deleteAdminSuccess = function(pgId){
  var self = this;
  return function(businessStatus){
    if(businessStatus.success){
      $("[data-pg-id=" + pgId + "]").parent().remove();
    }
    utils.notifAlert(businessStatus);
  }
}

Administration.prototype.loadAdmins = function(){
  var self = this;
  restAjax.authAjax({
    url:'auth/getAdmins',
    data:{
      role:"ADM"
    },
    success:self.displayAdmins(),
    error:utils.displayError
  });
}

Administration.prototype.displayAdmins = function(){
  var self = this
  return function(data){
    for(var i = 0; i < data.length; i++){
      self.addAdminLine(data[i]);
    }
  }
}

Administration.prototype.addAdminLine = function(pg){
  var formatedPg = pg.firstName + " " + pg.lastName + " - " + pg.nums + pg.tbk + pg.proms;
  var pgId = pg.id;
  var divToInclude = '<div class="row">'
  divToInclude    += formatedPg + '<button type="button" class="close delete-pg" data-pg-id="' + pgId + '" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
  divToInclude    += '</div>'
  $("#admin-list").append(divToInclude);
}
