function DisplayTaf(){
}

DisplayTaf.prototype.init = function(){
  this.loadInfos();
  $("#send-button").on("click", this.loadTaf());
}

DisplayTaf.prototype.loadTaf = function(){
  var self = this;
  return function(){
    $.ajax({
      url:'http://localhost:8080/Pg',
      crossDomain:true,
      dataType:"json",
      data:{
        pg:$("#nums-pg").val()
      },
      success:self.displayTafForPg,
      error:self.displayError
    });
  }
}

DisplayTaf.prototype.loadInfos = function(){
  var self = this;
  $.ajax({
    url:'http://localhost:8080/Infos',
    crossDomain:true,
    dataType:"json",
    success:self.displayInfos,
    error:self.displayError
  });
}

DisplayTaf.prototype.displayInfos = function(data){
  if(data && data.date){
    var date = new Date(data.date);
    $("#update-date").text(utils.formatDate(date));
  }
}

DisplayTaf.prototype.displayTafForPg = function(data){
  if(!data){
    $("#result").hide();
    $("#no-result").show();
  }else{
    $("#result").show();
    $("#no-result").hide();
    var firstName = data.firstName? data.firstName : "";
    var lastName = data.lastName? data.lastName : "";
    var nums = data.nums? data.nums : "";
    var tbk = data.tbk? data.tbk : "";
    var proms = data.proms? data.proms : "";
    var totalTaf = data.totalTaf || data.totalTaf === 0 ? data.totalTaf.toFixed(2) + " €" : "";
    $("#pg-name").text(firstName + " " + lastName + " " + nums + tbk + proms)
    $("#total-taf").text(totalTaf)
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
}

DisplayTaf.prototype.displayError = function(e){
  console.error("Erreur d'accès à l'API", e);
}

var utils = new Utils();
var displayTaf = new DisplayTaf();
$(document).ready(function(){
  displayTaf.init();
});
