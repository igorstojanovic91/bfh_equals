import store from "../store";
import util from "../util.js";

export default {
    requiresAuth: true,

    render: function () {
        store.clear();
        util.showAuthContent(false);
        util.updateViewField('user.name', '');
        return $($('#tpl-logout').html());

        const $view = $($('#tpl-logut').html());
        $('[data-action=logout]', $view).on('click', e => {
            e.preventDefault();
        });
        return $view;
    }
}