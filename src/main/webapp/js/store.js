let data = {};

export default {
    setUser: function(user) {
        data.user = user;
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
