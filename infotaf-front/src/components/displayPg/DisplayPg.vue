<template>
  <div class="display-pg-component">
    <div class="row">
      <div class="col-md-12 ROLE_USR">
        <fieldset>
          <legend>PG</legend>
          <form id="form-pg">
            <label for="nums-pg">Num's</label>
            <input type="text" v-model="nums" class="small-input" placeholder="80Li212"/>
            <input type="button" value="Go!" class="btn btn-primary" v-on:click="loadPgInfos"/>
          </form>
          <div id="pg-infos">
            <div v-if="pg.id === 0">
              <div class="pg-infos-legend important">Aucun résultat</div>
            </div>
            <div v-else-if="!isNaN(pg.id)">
              <div class="pg-infos-legend important" id="pg-name">{{pg.firstName}} {{pg.lastName}} {{pg.nums}}{{pg.tbk}}{{pg.proms}}</div>
              <div id="payment" class="pg-infos-legend">
                <div>Débit: <span class="float-right">{{ pg.totalDu | euros }}</span></div>
                <div>Crédit: <span class="float-right">{{ pg.totalPaye | euros }}</span></div>
                <div>Montant à payer: <span class="float-right" :class="{'text-color-green':pg.totalTaf <= 0, 'text-color-red':pg.totalTaf > 0}">{{ pg.totalTaf | euros }}</span></div>
              </div>
            </div>
          </div>
        </fieldset>
      </div>
    </div>
    <div v-if="pg.id === 0 || isNaN(pg.id)" class="am-trads">
      <img id="logo-img" src="static/logo-li212.jpg"/>
    </div>
    <div v-else>
      <div class="row">
        <div class="col-md-12">
          <ul id="info-pg-tabs" class="nav nav-tabs">
            <li id="info-pg-tabs-consos" class="active"><a href="#consos" data-toggle="tab">Consos</a></li>
            <li id="info-pg-tabs-kifekoi"><a href="#kifekoi" data-toggle="tab">Kifékoi</a></li>
          </ul>
        </div>
      </div>
      <div class="row tab-content">
          <div class="tab-pane fade in active" id="consos">
            <div class="col-md-8" id="table-manips">
              <table class="custom-table-style">
                <thead>
                  <tr>
                    <th>Nom</th><th>Quantité</th><th>Prix</th><th>Sous total</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="line in manipTypeOne">
                    <td>{{line.nom}}</td>
                    <td class="number">{{line.quantite}}</td>
                    <td class="number">{{line.prix | euros}}</td>
                    <td class="number">{{line.totalPrice | euros}}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="col-md-4" id="table-cotizs">
              <table class="custom-table-style">
                <thead>
                  <tr>
                    <th>Nom</th><th>Prix</th><th>Payée</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="line in manipTypeTwo">
                    <td>{{line.nom}}</td>
                    <td class="number">{{line.prix | euros}}</td>
                    <td>
                      <div v-if="line.quantite === 0">
                        Non
                      </div>
                      <div v-else>
                        Oui
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="tab-pane fade" id="kifekoi">
            <div class="col-md-12">
              <form>
                <div class="row">
                  <div class="col-xs-2">
                    <label class="float-right">Job:</label>
                  </div>
                  <div class="col-xs-10" v-if="!modifyMode">
                    {{pg.work}}
                  </div>
                  <div v-else class="col-xs-10">
                    <input v-model="formKifekoi.work" class="kifekoi-input" type="text" maxlength=100/>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-2">
                    <label class="float-right">Lieu:</label>
                  </div>
                  <div class="col-xs-10" v-if="!modifyMode">
                    {{pg.workplace}}
                  </div>
                  <div v-else class="col-xs-10">
                    <input v-model="formKifekoi.workplace" class="kifekoi-input" type="text" maxlength=100/>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-2">
                    <label class="float-right">Détails:</label>
                  </div>
                  <div class="col-xs-10" v-if="!modifyMode">
                    {{pg.workDetails}}
                  </div>
                  <div v-else class="col-xs-10">
                    <textarea v-model="formKifekoi.workDetails" class="kifekoi-input" type="text" maxlength=2000></textarea>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-2">
                    <label class="float-right">Adresse:</label>
                  </div>
                  <div class="col-xs-10" v-if="!modifyMode">
                    {{pg.address}}
                  </div>
                  <div v-else class="col-xs-10">
                    <input v-model="formKifekoi.address" class="kifekoi-input" type="text" maxlength=100/>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-2">
                    <button type="button" v-if="!modifyMode && curUserConnected()" @click="modify" class="btn btn-primary">Modifier</button>
                    <button type="button" v-else-if="curUserConnected()" @click="save" class="btn btn-primary">Sauver</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import restAjax from '@/helpers/restAjax.js'
import utils from '@/helpers/utils.js'
import Base from '@/components/base/Base'
import loginHelper from '@/helpers/login.js'
import $ from 'jquery'
import { EventBus } from '@/helpers/eventBus.js'

export default {
  name: 'display-pg',
  extends: Base,
  data () {
    return {
      nums: '',
      pg: {},
      modifyMode: false,
      formKifekoi: {
        workplace: '',
        work: '',
        address: '',
        workDetails: ''
      }
    }
  },
  mounted: function () {
    EventBus.$on('login-changed', this.changeLoginState)
  },
  computed: {
    manipTypeOne: function () {
      return this.pg.manips.filter(function (manip) {
        return manip.type === 1
      })
    },
    manipTypeTwo: function () {
      return this.pg.manips.filter(function (cotiz) {
        return cotiz.type === 2
      })
    },
    manipTypeThree: function () {
      return this.pg.manips.filter(function (payment) {
        return payment.type === 3
      })
    }
  },
  methods: {
    changeLoginState: function () {
      this.pg = {}
    },
    curUserConnected: function () {
      return loginHelper.username.toUpperCase() === (this.pg.nums + this.pg.tbk + this.pg.proms).toUpperCase()
    },
    modify: function () {
      this.formKifekoi.workplace = this.pg.workplace
      this.formKifekoi.work = this.pg.work
      this.formKifekoi.address = this.pg.address
      this.formKifekoi.workDetails = this.pg.workDetails
      this.modifyMode = true
    },
    save: function () {
      var self = this
      $('#save-button').prop('disabled', 'true')
      restAjax.authAjax({
        url: 'auth/SaveKifekoi',
        type: 'POST',
        data: utils.addUserToObject(self.formKifekoi, loginHelper.username),
        success: self.saveKifekoiSuccess,
        error: utils.displayErrorAndEnableButton('#save-button')
      })
    },
    saveKifekoiSuccess: function (businessStatus) {
      if (businessStatus.success) {
        this.pg.workplace = this.formKifekoi.workplace
        this.pg.address = this.formKifekoi.address
        this.pg.work = this.formKifekoi.work
        this.pg.workDetails = this.formKifekoi.workDetails
      }
      utils.notifAlert(businessStatus)
      this.modifyMode = false
      $('#save-button').removeAttr('disabled')
    },
    loadPgInfos: function () {
      var self = this
      if (this.nums !== '') {
        restAjax.authAjax({
          url: 'auth/Pg',
          data: {
            pg: self.nums
          },
          success: self.populateInfos,
          error: utils.displayError
        })
      } else {
        this.pg = {}
      }
    },
    populateInfos: function (data) {
      this.modifyMode = false
      console.log(data)
      this.pg = data
    }
  }
}
</script>

<style scoped>
#pg-infos{
  display:inline-block;
  vertical-align:center;
}
#form-pg{
  padding-right:30px;
  display:inline-block;
}
#payment{
  padding-left:25px;
  vertical-align: center;
}
#logo-img{
  width:50%;
  display: block;
  margin: auto;
}
.pg-infos-legend.important{
  display:inline-block;
  font-size: 125%;
  font-weight: bold;
}

.pg-infos-legend{
  display:inline-block;
}
.number{
  text-align:right;
}
.kifekoi-input{
    width:100%;
}
.kifekoi-textarea{
    width:100%;
}
</style>
