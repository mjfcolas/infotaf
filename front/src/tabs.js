function Tabs(id, tabList){
  this.id = id;
  this.tabList = tabList;
  this.currenTab;
}

Tabs.prototype.init = function(initTab){
  var self = this;
  this.toggleTab(initTab);
  $("#" + this.id + " li a").on("click", function(){
    self.toggleTab($(this).attr("href").slice(1));
  });
}

Tabs.prototype.toggleTab = function(tabName){
  $("#" + this.id + " li").removeClass("active");
  $("#" + this.id + "-" + tabName).addClass("active");

  $("." + this.id + "-" + "content" ).hide();
  $("#" + this.id + "-" + "content" + "-" + tabName).show();
  this.currentTab = tabName;
}
