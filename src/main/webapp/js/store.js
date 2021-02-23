import util from './util.js';

let data = {};

export default {
    setUser: function(userData) {
        data.user = userData;
        console.log(data)
        console.log(userData)
        console.log(userData.userName);
        util.updateViewField('user.userName', userData.userName);
    },
    getUser: function() {
        return data.user;

    },

    setModules: function(modules) {
        data.modules = modules;
    },
    getModules: function() {
        return data.modules;
    },

    clear: function() {
        data = {};
    }
};
