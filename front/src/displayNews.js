function DisplayNews(){
}

DisplayNews.prototype.init = function(){
  this.loadNews();
  var self = this;
  $(document).on("shown.bs.modal", "#create-news-modal", function(){
    $("#create-news-button").on("click", self.createNews());
  });
  $(document).on("hidden.bs.modal", "#create-news-modal", function(){
    $("#create-news-button").off();
  });
}

DisplayNews.prototype.loadNews = function(){
  var self = this;
  restAjax.ajax({
    url:'News',
    success:self.displayNews,
    error:utils.displayError
  });
}

DisplayNews.prototype.displayNews = function(news){
  if(news){
    var newsDiv = $("#news");
    $("#news").html("");
    for(var i = news.length - 1; i >= 0; i--){
      var date = new Date(news[i].date);
      newsDiv.append("<h3>" + news[i].title + " "+ utils.formatDate(date, "dd/MM/yyyy") + "</h3>");
      newsDiv.append("<p>" + news[i].content + "</p>");
    }
  }
}

DisplayNews.prototype.createNews = function(){
  var self = this;
  return function(){
    $("#save-button").prop("disabled", "true");
    restAjax.authAjax({
      url:'auth/CreateNews',
      type:'POST',
      data:$("#news-form").serialize(),
      success:self.createNewsSuccess(),
      error:self.createNewsError()
    });
  }
}

DisplayNews.prototype.createNewsSuccess = function(){
  var self = this;
  return function(businessStatus){
    if(businessStatus.success){
      $('#create-news-modal').modal('hide');
    }
    utils.notifAlert(businessStatus);
    self.loadNews();
  }
}
DisplayNews.prototype.createNewsError = function(){
  var self = this;
  return function(){
    $("#create-news-button").removeAttr("disabled");
    var businessStatus = {
      success:false,
      message:messages.unknownError
    }
    utils.notifAlert(businessStatus);
  }
}
