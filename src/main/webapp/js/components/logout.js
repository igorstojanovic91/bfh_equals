import store from "../store.js";
import util from "../util.js";

export default {
    requiresAuth: true,

    getTitle: function () {
        return "Logout";
    },

    render: function () {
        const $view = $($('#tpl-logout').html());
        $('[data-action=logout]', $view).on('click', e => {
            e.preventDefault();
        });

        store.clear();
        util.showAuthContent(false);
        util.updateViewField('user.userName', '');

        return $view;
    }
}
