<template>
  <span class="account">
    <div id="account-modal" class="modal fade" role="dialog">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">Ton compte</h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <form id="account-form">
                <div class="row">
                  <div class="col-md-12">
                    <h4>Modifier le mot de passe</h4>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-4">
                    <label for="account-form-password-label">Nouveau mot de passe</label>
                  </div>
                  <div class="col-md-8">
                    <input type="password" v-model="formAccount.password"/>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-4">
                    <label for="account-form-password-confirm-label">Confirmer</label>
                  </div>
                  <div class="col-md-8">
                    <input type="password" v-model="formAccount.passwordConfirm"/>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-12">
                    <h4>Modifier l'adresse mail de contact</h4>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-4">
                    <label for="account-form-password-confirm-label">Adresse</label>
                  </div>
                  <div class="col-md-8">
                    <input v-model="formAccount.mail"/>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" @click="saveAccountInfos">Appliquer</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
          </div>
        </div>
      </div>
    </div>
  </span>
</template>

<script>
import restAjax from '@/helpers/restAjax.js'
import loginHelper from '@/helpers/login.js'
import Base from '@/components/base/Base'
import utils from '@/helpers/utils.js'
import { messages } from '@/resources/messages.js'
import { EventBus } from '@/helpers/eventBus.js'
import $ from 'jquery'

export default {
  name: 'account',
  extends: Base,
  data () {
    return {
      formAccount: {
        password: '',
        passwordConfirm: '',
        mail: ''
      }
    }
  },
  mounted: function () {
    // Event pour les changements de login
    EventBus.$on('login-changed', this.loadDatas)
  },
  methods: {
    loadDatas: function () {
      if (loginHelper.loggedIn) {
        var self = this
        console.log(loginHelper.username)
        restAjax.authAjax({
          url: 'auth/SimplePg',
          data: {
            pg: loginHelper.username
          },
          success: self.populateDatas,
          error: utils.displayError
        })
      }
    },
    populateDatas: function (data) {
      this.formAccount.mail = data.mail
    },
    saveAccountInfos: function () {
      var self = this
      var error = false

      // Erreur sur le mdp
      if (this.formAccount.password !== '' && this.formAccount.password !== this.formAccount.passwordConfirm) {
        var bsPassword = {
          success: false,
          message: messages.differentPasswords
        }
        error = true
        utils.notifAlert(bsPassword)
      }

      // Erreur sur l'adresse mail
      var mailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]{2,}\.[a-zA-Z]{2,4}$/
      if (!mailRegex.test(this.formAccount.mail)) {
        var bsEmail = {
          success: false,
          message: messages.badEmailAddress
        }
        error = true
        utils.notifAlert(bsEmail)
      }

      // Enregistrement si OK, message sinon
      if (!error) {
        restAjax.authAjax({
          url: 'auth/savePgAccount',
          type: 'POST',
          data: utils.addUserToObject(self.formAccount, loginHelper.username),
          success: self.saveAccountInfosSuccess,
          error: utils.displayError
        })
      }
    },
    saveAccountInfosSuccess: function (businessStatus) {
      if (businessStatus.success) {
        this.formAccount.password = ''
        this.formAccount.passwordConfirm = ''
        $('#account-modal').modal('hide')
      }
      utils.notifAlert(businessStatus)
    }
  }
}
</script>
