import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [   
    {
      path: '/login',
      name: 'login',
      component: resolve => require(["@/views/user/login.vue"], resolve),
    },

  ]
});
