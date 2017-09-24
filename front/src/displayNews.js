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

  $("#news").on("click", ".delete-news", self.askDeleteNews());
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
      var deleteButton = '<button type="button" class="ROLE_ADM close delete-news" data-news-id="' + news[i].id + '" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
      var toAppend = ""
      toAppend += "<div data-news-id='" + news[i].id + "'>";
      toAppend += "<h3>";
      toAppend += news[i].title + " " + utils.formatDate(date, "dd/MM/yyyy");
      toAppend += deleteButton;
      toAppend += "</h3>"
      toAppend += "<p>" + news[i].content + "</p>";
      toAppend += "</div>";
      newsDiv.append(toAppend);
      var jDeleteButton = $("[data-news-id='" + news[i].id + "']");
      login.toggleDomElement(jDeleteButton);
    }
  }
}

DisplayNews.prototype.createNews = function(){
  var self = this;
  return function(){
    // $("#save-button").prop("disabled", "true");
    // restAjax.authAjax({
    //   url:'auth/CreateNews',
    //   type:'POST',
    //   data:$("#news-form").serialize(),
    //   success:self.createNewsSuccess(),
    //   error:self.createNewsError()
    // });
  }
}

DisplayNews.prototype.askDeleteNews = function(){
  var self = this;
  return function(){
    var jDomElement = $(this);
    BootstrapDialog.show({
        message: "Voulez vous supprimer cette news ?",
        buttons: [{
            label: 'Valider',
            action: function(dialog) {
                self.deleteNews(jDomElement)
                dialog.close();
            }
        }, {
            label: 'Annuler',
            action: function(dialog) {
                dialog.close();
            }
        }]
    });
  }
}

DisplayNews.prototype.deleteNews = function(jDomElement){
  restAjax.authAjax({
    url:'auth/deleteNews',
    data:{
      newsId:jDomElement.attr("data-news-id")
    },
    success:this.deleteNewsSuccess(jDomElement.attr("data-news-id")),
    error:utils.displayError
  });
}

DisplayNews.prototype.deleteNewsSuccess = function(newsId){
  var self = this;
  return function(businessStatus){
    if(businessStatus.success){
      $("[data-news-id=" + newsId + "]").remove();
    }
    utils.notifAlert(businessStatus);
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
