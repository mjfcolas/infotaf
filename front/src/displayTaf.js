function DisplayTaf(){
  this.tabs = new Tabs("info-pg-tabs", ["consos", "kifekoi"])
}

DisplayTaf.prototype.init = function(){
  var self=this;
  this.tabs.init("consos");
  $("#send-button").on("click", this.loadTaf());
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
    $.ajax({
      url:properties.serverUrl+'Pg',
      crossDomain:true,
      dataType:"json",
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
    if(!data){
      $(".result").hide();
      $(".no-result").show();
    }else{
      $(".result").css("display", "inline");
      $(".no-result").hide();

      self.displayGlobalPgInfos(data);
      self.renderPGConsos(data);
      self.renderKifekoi(data);
    }
  }
}

DisplayTaf.prototype.renderKifekoi = function(data){
  var workplace = data.workplace? data.workplace : "";
  var address = data.address? data.address : "";
  var work = data.work? data.work : "";
  var workDetails = data.workDetails? data.workDetails : "";

  $("#pg-job").text(work);
  $("#pg-place").text(workplace);
  $("#pg-job-details").text(workDetails);
  $("#pg-address").text(address);
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