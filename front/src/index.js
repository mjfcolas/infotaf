var utils = new Utils();
var login = new Login();
var displayTaf = new DisplayTaf();
var displayInfos = new DisplayInfos();
//var displayNews = new DisplayNews();
var account = new Account();
var restAjax = new RestAjax();
var administration = new Administration();
var calendar = new Calendar("#calendar")
$(document).ready(function(){
  w3.includeHTML();
  login.init();
  displayTaf.init();
  displayInfos.init();
  //displayNews.init();
  account.init();
  administration.init();
  //calendar.init();
});
