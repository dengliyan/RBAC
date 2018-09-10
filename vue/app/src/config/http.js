import axios from 'axios'
import router from '@/router/'
//import store from '../store/index'
import qs from 'qs'

axios.defaults.timeout = 30000;
axios.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';

// http request 拦截器
axios.interceptors.request.use(
    config => {
        config.headers['token'] ='---';
        return config;
    },
    err => {
        return Promise.reject(err);
});

// http response 拦截器
axios.interceptors.response.use(response => {return response;},error => {
    if (error.response) {
        switch (error.response.status) {
            case 401:               
        }
    }
    return Promise.reject(error.response.data)
});


export default {    
    get(url, params = {}) {              
        return new Promise((resolve, reject) => {
            axios.get(url, {params: params}).then(response => {
                resolve(response.data);
            }, err => {
                reject(err);
            });            
        })
    },
    post(url, data = {},form=true) {     
        return new Promise((resolve, reject) => {            
            if(!form){
                axios.post(url,  data, {headers: {'Content-Type': 'application/json',}}).then(response => {
                    resolve(response.data);
                }, err => {
                    reject(err);
                });
            }else{         
                let param=qs.stringify(data); 
                axios.post(url,  param,{headers: {'Content-Type': 'application/x-www-form-urlencoded',}}).then(response => {
                    resolve(response.data);
                }, err => {
                    reject(err);
                });
            }
        })
    }
};
