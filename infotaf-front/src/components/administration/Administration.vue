<template>
  <span id="administrationComponent">
    <div id="administration-modal" class="modal fade" role="dialog">
      <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">Administration</h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="row">
                <div class="col-md-12">
                  <fieldset>
                    <div class="row">
                      <legend><a data-toggle="collapse" href="#manage-admins" class="text-color-black">Gestion des admins</a></legend>
                    </div>
                    <div class="row collapse" id="manage-admins">
                      <div class="col-md-6">
                        <fieldset>
                          <legend>Liste des admin</legend>
                          <div v-for="pg in admins">
                            <div class="row">
                              {{pg.firstName}} {{pg.lastName}} - {{pg.nums}}{{pg.tbk}}{{pg.proms}}
                              <button type="button" v-on:click="deleteAdmin(pg.id)" class="close" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                          </div>
                        </fieldset>
                      </div>
                      <div class="col-md-6">
                        <fieldset>
                          <legend>Ajouter un admin</legend>
                          <form id="add-admin-form">
                            <label for="new-admin-input">Num's</label>
                            <input type="text" id="new-admin-input" v-model="formAdmin.pg" class="small-input" placeholder="80Li212"/>
                            <input type="button" v-on:click="addAdmin" value="Go!" class="btn btn-primary" id="new-admin-button"/>
                          </form>
                        </fieldset>
                      </div>
                    </div>
                  </fieldset>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <fieldset>
                    <div class="row">
                      <legend><a data-toggle="collapse" href="#relaunch-pg" class="text-color-black">Relancer les PGs</a></legend>
                    </div>
                    <div class="collapse" id="relaunch-pg">
                      <div class="row">
                        <div class="small-text">
                          Les PGs avec un négat's supérieur au montant ci dessous recevront un mail
                        </div>
                        <div v-if="sending" class="color-red">
                          Envoi en cours
                        </div>
                      </div>
                      <div class="row">
                        <form id="send-relaunch-mail-form">
                          <div class="col-md-4">
                            <label for="relaunch-amount">Montant</label>
                            <input type="text" id="relaunch-amount" v-model="formRelaunch.amount" class="small-input" placeholder="80"/> €
                          </div>
                          <div class="col-md-3">
                            <label for="relaunch-amount">Simuler</label>
                            <input type="checkbox" v-model="formRelaunch.isSimulation"/>
                          </div>
                          <div class="col-md-3">
                            <input type="button" v-on:click="relaunchPg" value="Envoyer les mails!" class="btn btn-primary" id="send-relaunch-mail-button"/>
                          </div>
                        </form>
                      </div>
                    </div>
                  </fieldset>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <fieldset>
                    <div class="row">
                      <legend><a data-toggle="collapse" href="#manage-mail" class="text-color-black">Paramétrage du mail</a></legend>
                    </div>
                    <div class="collapse" id="manage-mail">
                      <div class="row">
                        <div class="small-text">
                          Vous pouvez utiliser deux variables: { {NAME}} affichera le prénom du PG, et { {AMOUNT}} affichera le TAF du PG
                        </div>
                      </div>
                      <div class="row">
                        <form id="template-mail-form">
                          <div class="row">
                            <div class="col-md-12">
                              <textarea v-model="formMail.mail" class="mail-input" type="text" maxlength=5000></textarea>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-md-12">
                              <input type="button" v-on:click="saveMail" value="Sauvegarder le template" class="btn btn-primary" id="save-template-mail-button"/>
                            </div>
                          </div>
                        </form>
                      </div>
                    </div>
                  </fieldset>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <fieldset>
                    <div class="row">
                      <legend><a data-toggle="collapse" href="#import-taf" class="text-color-black">Importer un fichier de TAF</a></legend>
                    </div>
                    <div class="collapse" id="import-taf">
                      <div class="row">
                        <div v-if="uploading" class="color-red">
                          Upload en cours
                        </div>
                        <form id="import-file-form" enctype="multipart/form-data">
                          <div class="col-md-4">
                            <label class="btn btn-default btn-file">
                              Sélectionner le fichier <input type="file" style="display: none;" @change="processFile($event)">
                            </label>
                          </div>
                          <div class="col-md-4">
                            {{formFile.fileName}}
                          </div>
                          <div class="col-md-4">
                            <input type="button" v-on:click="importFile" value="Importer le fichier" class="btn btn-primary" id="import-file-button"/>
                          </div>
                        </form>
                      </div>
                    </div>
                  </fieldset>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
          </div>
        </div>
      </div>
    </div>
    <span id="administration-buttons">
      <input type="button" value="Administration" @click="loadAdministrationPopin" class="btn btn-primary ROLE_ADM" id="admin-button" data-toggle="modal" data-target="#administration-modal"/>
    </span>
  </span>
</template>

<script>
import restAjax from '@/helpers/restAjax.js'
import utils from '@/helpers/utils.js'
import $ from 'jquery'
import Base from '@/components/base/Base'
import BootstrapDialog from 'bootstrap3-dialog'
import { messages } from '@/resources/messages.js'

export default {
  name: 'administration',
  extends: Base,
  data () {
    return {
      admins: [],
      formAdmin: {
        pg: ''
      },
      formRelaunch: {
        amount: '',
        isSimulation: false
      },
      formMail: {
        mail: ''
      },
      formFile: {
        fileName: '',
        file: null
      },
      sending: false,
      uploading: false
    }
  },
  methods: {
    loadAdministrationPopin: function () {
      this.loadFormMail()
      this.loadAdmins()
      this.formFile.formData = new FormData()
    },
    processFile: function (e) {
      if (!e.target.files.length) return
      this.formFile.fileName = e.target.files[0].name
      this.formFile.file = e.target.files[0]
    },
    loadFormMail: function () {
      // Chargement des admins
      var self = this
      restAjax.authAjax({
        url: 'auth/getMailTemplate',
        success: self.populateMailTemplate,
        error: utils.displayError
      })
    },
    populateMailTemplate: function (mail) {
      this.formMail.mail = mail.object.value
    },
    loadAdmins: function () {
      // Chargement des admins
      var self = this
      restAjax.authAjax({
        url: 'auth/getAdmins',
        data: {
          role: 'ADM'
        },
        success: self.populateAdmins,
        error: utils.displayError
      })
    },
    populateAdmins: function (admins) {
      this.admins = admins
    },
    deleteAdmin: function (id) {
      restAjax.authAjax({
        url: 'auth/deleteAdmin',
        data: {
          pgId: id
        },
        success: this.deleteAdminSuccess(id),
        error: utils.displayError
      })
    },
    deleteAdminSuccess: function (id) {
      var self = this
      return function (businessStatus) {
        if (businessStatus.success) {
          self.admins = self.admins.filter(function (admin) {
            return admin.id !== id
          })
        }
        utils.notifAlert(businessStatus)
      }
    },
    addAdmin: function () {
      var self = this
      $('#new-admin-button').prop('disabled', 'true')
      restAjax.authAjax({
        url: 'auth/addAdmin',
        type: 'POST',
        data: self.formAdmin,
        success: this.addAdminSuccess(),
        error: utils.displayErrorAndEnableButton('#new-admin-button')
      })
    },
    addAdminSuccess: function () {
      var self = this
      return function (businessStatus) {
        $('#new-admin-button').removeAttr('disabled')
        if (businessStatus.success) {
          self.admins.push(businessStatus.object)
        }
        utils.notifAlert(businessStatus)
      }
    },
    relaunchPg: function () {
      $('#send-relaunch-mail-button').prop('disabled', 'true')
      var self = this
      if (isNaN(self.formRelaunch.amount) || self.formRelaunch.amount <= 0) {
        var businessStatus = {
          success: false,
          message: messages.strictPositiveAmount
        }
        $('#send-relaunch-mail-button').removeAttr('disabled')
        utils.notifAlert(businessStatus)
      } else {
        this.sending = true
        restAjax.authAjax({
          url: 'auth/relaunchPg',
          type: 'POST',
          data: self.formRelaunch,
          success: this.relaunchPgSuccess(),
          error: utils.displayErrorAndEnableButton('#send-relaunch-mail-button'),
          always: self.relaunchPgAlwaysCallback()
        })
      }
    },
    relaunchPgAlwaysCallback: function () {
      var self = this
      return function () {
        self.sending = false
      }
    },
    relaunchPgSuccess: function () {
      // var self = this
      return function (businessStatus) {
        $('#send-relaunch-mail-button').removeAttr('disabled')
        if (businessStatus.success) {
          var message = ''
          if (businessStatus.object && !businessStatus.object.success) {
            message += messages.errorsInRelaunch + '</BR>'
            if (businessStatus.object.addressInError && businessStatus.object.addressInError.length > 0) {
              message += messages.errorsInRelaunchConcernedAddress + '</BR>'
              for (var i = 0; i < businessStatus.object.addressInError.length; i++) {
                message += businessStatus.object.addressInError[i] + '</BR>'
              }
            }
          }
          message += businessStatus.message
          BootstrapDialog.alert(message)
        } else {
          utils.notifAlert(businessStatus)
        }
      }
    },
    saveMail: function () {
      $('#save-template-mail-button').prop('disabled', 'true')
      restAjax.authAjax({
        url: 'auth/saveMailTemplate',
        type: 'POST',
        data: this.formMail,
        success: this.saveMailSuccess(),
        error: utils.displayErrorAndEnableButton('#save-template-mail-button')
      })
    },
    saveMailSuccess: function () {
      return function (businessStatus) {
        $('#save-template-mail-button').removeAttr('disabled')
        utils.notifAlert(businessStatus)
      }
    },
    importFile: function () {
      this.uploading = true
      $('#import-file-button').prop('disabled', 'true')
      console.log(this.formFile.fileName)

      var file = new FormData()
      if (this.formFile.file) {
        file.append(this.formFile.fileName, this.formFile.file, this.formFile.fileName)
      }

      restAjax.authAjax({
        url: 'auth/uploadTaf',
        processData: false,
        contentType: false,
        type: 'POST',
        data: file,
        success: this.uploadTafSuccess(),
        error: utils.displayErrorAndEnableButton('#import-file-button'),
        always: this.uploadTafAlwaysCallback()
      })
    },
    uploadTafSuccess: function () {
      return function (businessStatus) {
        $('#import-file-button').removeAttr('disabled')
        if (businessStatus && businessStatus.message) {
          BootstrapDialog.alert(businessStatus.message)
        }
      }
    },
    uploadTafAlwaysCallback: function () {
      var self = this
      return function () {
        self.uploading = false
      }
    }
  }
}
</script>
<style scoped>
.small-text{
  font-size: 80%;
}
textarea{
  width:100%;
  height:200px;
}
</style>
