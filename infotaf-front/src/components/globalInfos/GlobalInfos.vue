<template>
  <div>
    Mise à jour : {{updateDate | date('dd/MM/yyyy')}}
  </div>
</template>

<script>
import utils from '@/helpers/utils.js'
import restAjax from '@/helpers/restAjax.js'

export default {
  name: 'global-infos',
  data () {
    return {
      updateDate: ''
    }
  },
  created: function () {
    this.loadInfos()
  },
  methods: {
    loadInfos: function () {
      var self = this
      restAjax.ajax({
        url: 'Infos',
        success: self.displayInfos,
        error: utils.displayError
      })
    },
    displayInfos: function (data) {
      if (data && data.date) {
        this.updateDate = new Date(data.date)
      }
    }
  }
}
</script>
