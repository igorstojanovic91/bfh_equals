import service from '../service.js';
import router from '../router.js';
import store from '../store.js';


export default {

    getTitle: function() {
        return "Modules";
    },

    render: function() {
        const $view = $($('#successful').html());

        return $view;
    }
};
