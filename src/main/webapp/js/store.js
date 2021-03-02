import util from './util.js';

let data = {};

export default {
    setUser: function (userData) {
        data.user = userData;
        util.updateViewField('user.userName', userData.username);
    },
    getUser: function () {
        return data.user;

    },

    setModules: function (modules) {
        data.modules = modules;
    },
    getModules: function () {
        return data.modules;
    },
    getModule: function(id) {
        return data.modules.find(module => module.moduleId === Number(id));
    },

    clear: function () {
        data = {};
    }
};
