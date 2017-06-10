function Utils(){
}

Utils.prototype.formatDate = function(d, format){

  format = format || "dd/MM/yyyy hh:mm";
  var dd = ("0" + d.getDate()).slice(-2);
  var MM = ("0"+(d.getMonth()+1)).slice(-2);
  var yyyy = d.getFullYear();
  var hh = ("0" + d.getHours()).slice(-2);
  var mm = ("0" + d.getMinutes()).slice(-2);

  var dateString = format.replace("dd", dd);
  dateString = dateString.replace("MM", MM);
  dateString = dateString.replace("yyyy", yyyy);
  dateString = dateString.replace("hh", hh);
  dateString = dateString.replace("mm", mm);

  return dateString;
}

Utils.prototype.displayError = function(e){
  console.error("Erreur d'accès à l'API", e);
}
