import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [ 
    {
      path: '/',
      name: 'default',
      component: resolve => require(["@/views/layout.vue"], resolve),
      children:[

        {
          path: '/category',
          name: 'category',
          component: resolve => require(["@/views/category/list.vue"], resolve),
        },
      ]
    },  
    {
      path: '/login',
      name: 'login',
      component: resolve => require(["@/views/user/login.vue"], resolve),
    },
    
  ]
});
