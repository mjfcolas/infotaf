import { properties } from '@/resources/properties.js'
import { messages } from '@/resources/messages.js'
import login from '@/helpers/login.js'
import utils from '@/helpers/utils.js'

//Classe permettant d'encapsuler les appels ajax spécifiques à l'API avec refresh des tokens
function RestAjax(){
}

//Appel ajax classique incorporant les headers de l'authentification
RestAjax.prototype.authAjax = function(params){
  if(!params.always){
    params.always = function(){};
  }
  if(!localStorage.jwtToken || localStorage.jwtToken == "null" || localStorage.jwtToken == "undefined"){
    var businessStatus = {
      success:false,
      message:messages.notConnected
    }
    utils.notifAlert(businessStatus);
    params.always();
    return;
  }
  $.ajax({
    url:properties.serverUrl+params.url,
    dataType:"json",
    type:params.type,
    contentType:params.contentType,
    processData:params.processData,
    beforeSend:function(xhr) {
      xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
      xhr.setRequestHeader('X-Authorization', "Bearer " + localStorage.jwtToken);
    },
    data:params.data,
    success:function(data){
      if(params.success){
        params.success(data);
      }
      login.checkRefresh();
    },
    error:function(error){
      if(params.error){
        params.error(error);
      }
      login.checkRefresh();
    }
  }).always(params.always);
}
//Appel ajax classique sans headers de l'authentification
RestAjax.prototype.ajax = function(params){
  if(!params.always){
    params.always = function(){};
  }
  $.ajax({
    url:properties.serverUrl+params.url,
    dataType:"json",
    type:params.type,
    data:params.data,
    success:function(data){
      if(params.success){
        params.success(data);
      }
      if(login.loggedIn){
        login.checkRefresh();
      }
    },
    error:function(error){
      if(params.error){
        params.error(error);
      }
      login.checkRefresh();
    }
  }).always(params.always);;
}

export default new RestAjax();
