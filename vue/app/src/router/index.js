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
          path: '/system/category',
          name: 'category',
          component: resolve => require(["@/views/category/list.vue"], resolve),
        },
        {
          path: '/system/dept',
          name: 'dept',
          component: resolve => require(["@/views/dept/list.vue"], resolve),
        },
        {
          path: '/system/role',
          name: 'role',
          component: resolve => require(["@/views/role/list.vue"], resolve),
        },
      ]
    },  
    {
      path: '/login',
      name: 'login',
      component: resolve => require(["@/views/login.vue"], resolve),
    },
    
  ]
});
