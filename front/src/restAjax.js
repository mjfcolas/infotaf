//Classe permettant d'encapsuler les appels ajax spécifiques à l'API avec refresh des tokens
function RestAjax(){
}

//Appel ajax classique incorporant les headers de l'authentification
RestAjax.prototype.authAjax = function(params){
  $.ajax({
    url:properties.serverUrl+params.url,
    dataType:"json",
    type:params.type,
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
  });
}
//Appel ajax classique sans headers de l'authentification
RestAjax.prototype.ajax = function(params){
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
  });
}
