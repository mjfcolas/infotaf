import $ from 'jquery'
import notify from 'notifyjs-browser'
import { messages } from '@/resources/messages.js'

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

Utils.prototype.displayErrorAndEnableButton = function(buttonToEnable){
  var self = this;
  return function(e){
    $(buttonToEnable).removeAttr("disabled");
    var businessStatus = {
      success:false,
      message:messages.unknownError
    }
    self.notifAlert(businessStatus);
    console.error("Erreur d'accès à l'API", e);
  }
}

Utils.prototype.notifAlert = function(businessStatus){
  $.notify(businessStatus.message,
    {
      className : businessStatus.success ? "success" : "error",
      position : "right-bottom"
    });
}

Utils.prototype.parsePg = function(username){
  var result = {
    nums:"",
    tbk:"",
    proms:""
  };
  var firstCut = null;
  var secondCut = null;
  for (var i=0; i<username.length; i++) {
    if(firstCut == null){
      if(username.charAt(i) !== '-'
      && isNaN(parseInt(username.charAt(i)))){
        firstCut = i;
      }
    }else{
      if(!isNaN(parseInt(username.charAt(i)))){
        secondCut = i;
        break;
      }
    }
  }

  if(firstCut === null){
    firstCut = 0;
  }
  if(secondCut === null){
    secondCut = 0;
  }
  result.nums=username.substring(0, firstCut);
  result.tbk=username.substring(firstCut, secondCut);
  result.proms=username.substring(secondCut, username.length);

  return result
}

Utils.prototype.serializeFormWithUser = function(jForm, username){
  var serializedForm = "";
  if(jForm){
    serializedForm = $(jForm).serialize();
  }
  var parsedPg = utils.parsePg(username);
  serializedForm += "&nums=" + encodeURIComponent(parsedPg.nums);
  serializedForm += "&tbk=" + encodeURIComponent(parsedPg.tbk);
  serializedForm += "&proms=" + encodeURIComponent(parsedPg.proms);

  return serializedForm;
}

Utils.prototype.addUserToObject = function(object, username){
  var parsedPg = this.parsePg(username);
  object.nums=parsedPg.nums;
  object.tbk=parsedPg.tbk;
  object.proms=parsedPg.proms;
  return object;
}

export default new Utils();
