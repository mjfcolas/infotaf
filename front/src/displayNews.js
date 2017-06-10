function DisplayNews(){
}

DisplayNews.prototype.init = function(){
  this.loadNews();
}

DisplayNews.prototype.loadNews = function(){
  var self = this;
  $.ajax({
    url:properties.serverUrl+'News',
    crossDomain:true,
    dataType:"json",
    success:self.displayNews,
    error:utils.displayError
  });
}

DisplayNews.prototype.displayNews = function(news){
  if(news){
    var newsDiv = $("#news");
    for(var i=0; i < news.length; i++){
      var date = new Date(news[i].date);
      newsDiv.append("<h3>" + news[i].title + " "+ utils.formatDate(date, "dd/MM/yyyy") + "</h3>");
      newsDiv.append("<p>" + news[i].content + "</p>");
    }
  }
}
