<template>
  <div class="news">
    <div id="create-news-modal" class="modal fade" role="dialog">
      <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">Ecrire un billet</h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <form id="news-form">
                <div class="row">
                  <div class="col-md-2">
                    <label for="news-form-title-label">Titre</label>
                  </div>
                  <div class="col-md-10">
                    <input type="text" v-model="formNews.title" id="news-form-title-input"/>
                  </div>
                </div>
                <div class="row row-form">
                  <div class="col-md-2">
                    <label for="news-form-content-label">Contenu</label>
                  </div>
                  <div class="col-md-10">
                    <textarea id="news-form-content-textarea" class="big-textarea" v-model="formNews.content" maxlength="2000"></textarea>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" v-on:click="createNews" class="btn btn-default" id="create-news-button">Sauvegarder</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
          </div>
        </div>
      </div>
    </div>
    <div class="news-panel">
      <fieldset>
        <legend>Informations</legend>
        <div id="admin-news" class="ROLE_ADM">
          <button type="button" id="new-news-button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#create-news-modal">Nouveau billet</button>
        </div>
        <div v-for="curNews in orderedNews">
          <h3>
            {{curNews.title}} {{curNews.date | date('dd/MM/yyyy')}}
            <button v-on:click="askDeleteNews(curNews.id)" type="button" :class="{'granted':isGranted('ROLE_ADM')}" class="ROLE_ADM close" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </h3>
          <p>{{curNews.content}}</p>
        </div>
        <div id="news">
        </div>
      </fieldset>
    </div>
  </div>
</template>

<script>
import _ from 'lodash'
import $ from 'jquery'
import BootstrapDialog from 'bootstrap-dialog'
import restAjax from '@/helpers/restAjax.js'
import utils from '@/helpers/utils.js'
import { messages } from '@/resources/messages.js'
import Base from '@/components/base/Base'

export default {
  name: 'news',
  extends: Base,
  data () {
    return {
      news: [],
      formNews: {}
    }
  },
  computed: {
    orderedNews: function () {
      return _.orderBy(this.news, 'date', 'desc')
    }
  },
  created: function () {
    // Chargement des news
    var self = this
    restAjax.ajax({
      url: 'News',
      success: self.populateNews,
      error: utils.displayError
    })
  },
  methods: {
    populateNews: function (news) {
      this.news = news
    },
    // Gestion suppression news
    askDeleteNews: function (id) {
      var self = this
      BootstrapDialog.show({
        message: messages.askDeleteNews,
        buttons: [{
          label: messages.validate,
          action: function (dialog) {
            self.deleteNews(id)
            dialog.close()
          }
        }, {
          label: messages.cancel,
          action: function (dialog) {
            dialog.close()
          }
        }]
      })
    },
    deleteNews: function (id) {
      restAjax.authAjax({
        url: 'auth/deleteNews',
        data: {
          newsId: id
        },
        success: this.deleteNewsSuccess(id),
        error: utils.displayError
      })
    },
    deleteNewsSuccess: function (id) {
      var self = this
      return function (businessStatus) {
        if (businessStatus.success) {
          self.news = self.news.filter(function (news) {
            return news.id !== id
          })
        }
        utils.notifAlert(businessStatus)
      }
    },
    // Gestion création de news
    createNews: function () {
      var self = this
      $('#save-button').prop('disabled', 'true')
      restAjax.authAjax({
        url: 'auth/CreateNews',
        type: 'POST',
        data: self.formNews,
        success: self.createNewsSuccess(),
        error: self.createNewsError()
      })
    },
    createNewsSuccess: function () {
      var self = this
      return function (businessStatus) {
        $('#create-news-button').removeAttr('disabled')
        if (businessStatus.success) {
          self.news.push(businessStatus.object)
          $('#create-news-modal').modal('hide')
        }
        utils.notifAlert(businessStatus)
      }
    },
    createNewsError: function () {
      return function () {
        $('#create-news-button').removeAttr('disabled')
        var businessStatus = {
          success: false,
          message: messages.unknownError
        }
        utils.notifAlert(businessStatus)
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
