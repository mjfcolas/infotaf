<script>
import login from '@/helpers/login.js'

export default {
  name: 'base',
  methods: {
    isGranted: function (role) {
      return login.checkRight(role)
    }
  }
}
</script>
