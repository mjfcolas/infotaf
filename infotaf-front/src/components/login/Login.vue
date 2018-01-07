<template>
  <span class="login">
    <div id="login-modal" class="modal fade" role="dialog">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">Connexion</h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <form id="login-form">
                <div class="row">
                  <div class="col-md-4">
                    <label for="login-form-nums-label">Num's</label>
                  </div>
                  <div class="col-md-8">
                    <input type="text" v-model="formLogin.username" id="login-form-nums-input" placeholder="80Li212"/>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-4">
                    <label for="login-form-password-label">Mot de passe</label>
                  </div>
                  <div class="col-md-8">
                    <input type="password" v-model="formLogin.password" id="login-form-password-input" />
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" @click="login">Se connecter</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
          </div>
        </div>
      </div>
    </div>

    <div>
      <span id="login-info">{{loggedUser}}</span>
    </div>
    <span class="login-buttons">
      <span :class="{'hidden':!isGranted('')}">
        <input type="button" value="Logout" class="btn btn-primary" @click="logout"/>
        <input type="button" value="Compte" class="btn btn-primary" id="account-button" data-toggle="modal" data-target="#account-modal"/>
      </span>
      <span :class="{'hidden':isGranted('')}">
        <input type="button" value="Login" class="btn btn-primary" id="open-login-button" data-toggle="modal" data-target="#login-modal"/>
      </span>
    </span>
  </span>
</template>

<script>
import loginHelper from '@/helpers/login.js'
import Base from '@/components/base/Base'
import { EventBus } from '@/helpers/eventBus.js'
import $ from 'jquery'

export default {
  name: 'login',
  extends: Base,
  data () {
    return {
      formLogin: {
        username: '',
        password: ''
      },
      formAccount: {
        password: '',
        passwordConfirm: ''
      },
      user: ''
    }
  },
  mounted: function () {
    // Récupération de l'état si jamais loginHelper a terminé avant
    this.changeLoginState(!loginHelper.loggedIn)
    // Event pour les changements de login
    EventBus.$on('login-changed', this.changeLoginState)
  },
  computed: {
    loggedUser: function () {
      var userToUse = this.user
      if (loginHelper.loggedIn) {
        return 'Connecté: ' + userToUse
      } else {
        return 'Non connecté'
      }
    }
  },
  methods: {
    login: function () {
      loginHelper.login(this.formLogin)
    },
    logout: function () {
      loginHelper.logout()
    },
    changeLoginState: function (logout) {
      console.log('LOGINOUT', logout)
      this.user = loginHelper.username
      if (!logout) {
        this.formLogin.username = ''
        this.formLogin.password = ''
        $('#login-modal').modal('hide')
      }
    }
  }
}
</script>
