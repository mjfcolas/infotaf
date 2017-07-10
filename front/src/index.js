var utils = new Utils();
var login = new Login();
var displayTaf = new DisplayTaf();
var displayInfos = new DisplayInfos();
var displayNews = new DisplayNews();
var account = new Account();
$(document).ready(function(){
  w3.includeHTML();
  login.init();
  displayTaf.init();
  displayInfos.init();
  displayNews.init();
  account.init();
});
