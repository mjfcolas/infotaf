function DisplayTaf(){
  this.tabs = new Tabs("info-pg-tabs", ["consos", "kifekoi"])
  this.currentUser = null;
  this.saveMode = false;
  this.kifekoi = {};
}

DisplayTaf.prototype.init = function(){
  var self=this;
  this.tabs.init("consos");
  $("#send-button").on("click", this.loadTaf());
  $("#modify-button").on("click", this.enableEdit());
  $("#save-button").on("click", this.saveKifekoi());
  $('#form').keypress(function (e) {
    if (e.which == 13) {
      setTimeout(self.loadTaf(), 0)
      return false;
    }
  });
}

DisplayTaf.prototype.loadTaf = function(){
  var self = this;
  return function(){
    restAjax.ajax({
      url:'Pg',
      data:{
        pg:$("#nums-pg").val()
      },
      success:self.renderInfos(),
      error:utils.displayError
    });
  }
}

DisplayTaf.prototype.renderInfos = function(){
  var self = this
  return function(data){
    if(!data || !data.nums){
      $(".result").hide();
      $(".no-result").show();
    }else{
      $(".result").css("display", "inline");
      $(".no-result").hide();

      self.currentUser = data.nums + data.tbk + data.proms
      self.displayGlobalPgInfos(data);
      self.renderPGConsos(data);

      self.cacheDatasKifekoi(data);
      self.renderKifekoi();

    }
  }
}

DisplayTaf.prototype.cacheDatasKifekoi = function(data){
  this.kifekoi.workplace = data.workplace? data.workplace : "";
  this.kifekoi.address = data.address? data.address : "";
  this.kifekoi.work = data.work? data.work : "";
  this.kifekoi.workDetails = data.workDetails? data.workDetails : "";
}

DisplayTaf.prototype.renderKifekoi = function(){

  $("#pg-job").text(this.kifekoi.work);
  $("#pg-place").text(this.kifekoi.workplace);
  $("#pg-job-details").text(this.kifekoi.workDetails);
  $("#pg-address").text(this.kifekoi.address);

  this.saveMode = false;
  this.displaySaveAndModify();
}

DisplayTaf.prototype.displaySaveAndModify = function(enableEdit){
  if(!this.saveMode || !enableEdit){
    $("#save-button").hide();
  }else{
    $("#save-button").show();
  }
  if(!enableEdit && login.username && this.currentUser && login.username.toUpperCase() === this.currentUser.toUpperCase() ){
    $("#modify-button").show();
  }else{
    $("#modify-button").hide();
  }
}

DisplayTaf.prototype.enableEdit = function(){
  var self = this;
  return function(){
    $("#pg-place").html('<input id="pg-place-input" name="workplace" class="kifekoi-input" type="text" maxlength=100 value="' + self.kifekoi.workplace + '"/>');
    $("#pg-address").html('<input id="pg-address-input" name="address" class="kifekoi-input" type="text" maxlength=100 value="' + self.kifekoi.address + '"/>');
    $("#pg-job").html('<input id="pg-job-input" name="work" class="kifekoi-input" type="text" maxlength=100 value="' + self.kifekoi.work + '"/>');
    $("#pg-job-details").html('<textarea id="pg-job-details-textarea" name="workDetails" class="kifekoi-textarea" maxlength=2000/>');
    $("#pg-job-details-textarea").val(self.kifekoi.workDetails);

    self.saveMode = true;
    self.displaySaveAndModify(true);
  }
}

DisplayTaf.prototype.disableEdit = function(){
  $("#save-button").removeAttr("disabled");
  this.displaySaveAndModify(false);
  this.renderKifekoi();
}

DisplayTaf.prototype.saveKifekoi = function(){
  var self = this;
  return function(){
    $("#save-button").prop("disabled", "true");
    restAjax.authAjax({
      url:'auth/SaveKifekoi',
      type:'POST',
      data:utils.serializeFormWithUser($("#kifekoi-form"), login.username),
      success:self.saveKifekoiSuccess()
    });
  }
}

DisplayTaf.prototype.saveKifekoiSuccess = function(){
  var self = this;
  return function(businessStatus){

    self.kifekoi.workplace = $("#pg-place input").val();
    self.kifekoi.address = $("#pg-address input").val();
    self.kifekoi.work = $("#pg-job input").val();
    self.kifekoi.workDetails = $("#pg-job-details-textarea").val();

    utils.notifAlert(businessStatus);
    self.disableEdit();
  }
}

DisplayTaf.prototype.renderPGConsos = function(data){
  if(data.manips){
    var manipTable = $("#table-manips tbody");
    var cotizTable = $("#table-cotizs tbody");
    var totalPaye = $("#total-paye");
    var manips = data.manips;
    manipTable.html("");
    cotizTable.html("");
    totalPaye.html("");
    if(manips){
      for(var i=0; i < manips.length; i++){
        var name=manips[i].nom;
        var qte=manips[i].quantite;
        var price=manips[i].prix;
        var total=manips[i].totalPrice;
        if(manips[i].type == 1){
          manipTable.append("<tr><td>"+name+"</td><td class='number'>"+qte+"</td><td class='number'>"+price.toFixed(2)+" €</td><td class='number'>"+total.toFixed(2)+" €</td></tr>");
        }else if(manips[i].type == 2){
          var paid = "Non";
          if(qte !== 0){
            paid = "Oui"
          }
          cotizTable.append("<tr><td>"+name+"</td><td class='number'>"+price.toFixed(2)+" €</td><td>"+paid+"</td></tr>");
        }else if(manips[i].type == 3){
          totalPaye.append((total*-1).toFixed(2) + " €");
        }
      }
    }
  }
}

DisplayTaf.prototype.displayGlobalPgInfos = function(data){
  var firstName = data.firstName? data.firstName : "";
  var lastName = data.lastName? data.lastName : "";
  var nums = data.nums? data.nums : "";
  var tbk = data.tbk? data.tbk : "";
  var proms = data.proms? data.proms : "";
  var totalTaf = data.totalTaf || data.totalTaf === 0 ? data.totalTaf.toFixed(2) + " €" : "";
  var totalDu  = data.totalDu || data.totalDu === 0 ? data.totalDu.toFixed(2) + " €" : "";
  $("#pg-name").text(firstName + " " + lastName + " " + nums + tbk + proms);
  $("#total-taf").text(totalTaf);
  $("#a-payer").text(totalDu);

  if(data.totalTaf <= 0){
    $("#total-taf").addClass("text-color-green");
    $("#total-taf").removeClass("text-color-red");
  }else{
    $("#total-taf").addClass("text-color-red");
    $("#total-taf").removeClass("text-color-green");
  }
}
