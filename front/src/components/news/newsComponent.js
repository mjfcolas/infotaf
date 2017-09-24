var newsComponent = new Vue({
  el: '#newsCompoment',
  data: {
    news: [],
    formNews: {}
  },
  computed: {
    orderedNews: function () {
      return _.orderBy(this.news, 'date', 'desc');
    }
  },
  created:function(){
    //Chargement des news
    var self = this;
    restAjax.ajax({
      url:'News',
      success:self.populateNews,
      error:utils.displayError
    });
  },
  methods:{
    populateNews: function(news){
      this.news=news;
    },
    //Gestion suppression news
    askDeleteNews: function(id){
      var self = this;
      BootstrapDialog.show({
          message: messages.askDeleteNews,
          buttons: [{
              label: messages.validate,
              action: function(dialog) {
                  self.deleteNews(id)
                  dialog.close();
              }
          }, {
              label: messages.cancel,
              action: function(dialog) {
                  dialog.close();
              }
          }]
      });
    },
    deleteNews: function(id){
      restAjax.authAjax({
        url:'auth/deleteNews',
        data:{
          newsId:id
        },
        success:this.deleteNewsSuccess(id),
        error:utils.displayError
      });
    },
    deleteNewsSuccess: function(id){
      var self = this;
      return function(businessStatus){
        if(businessStatus.success){
          self.news = self.news.filter(function(news){
            return news.id !== id
          });
        }
        utils.notifAlert(businessStatus);
      }
    },
    //Gestion création de news
    createNews: function(){
      var self = this;
      $("#save-button").prop("disabled", "true");
      restAjax.authAjax({
        url:'auth/CreateNews',
        type:'POST',
        data:self.formNews,
        success:self.createNewsSuccess(),
        error:self.createNewsError()
      });
    },
    createNewsSuccess: function(){
      var self = this;
      return function(businessStatus){
        $("#create-news-button").removeAttr("disabled");
        if(businessStatus.success){
          self.news.push(businessStatus.object);
          $('#create-news-modal').modal('hide');
        }
        utils.notifAlert(businessStatus);
      }
    },
    createNewsError: function(){
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
  }
});
