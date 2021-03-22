import util from "../util.js";
import router from "../router.js";

let errorTitle = 'Error';

export default {

    getTitle: function () {
        return errorTitle;
    },

    render: function (error) {
        const $view = $($('#tpl-error').html());
        util.showAuthContent(false);

        errorTitle = error.title;
        $('[data-field=error-title]', $view).html(error.title);
        $('[data-field=error-message]', $view).html(error.message);

        const isUnauthorized = error.title.split(' ')[0] === '401';

        $('[data-action=go-back]', $view).on('click', e => {
            e.preventDefault();
            isUnauthorized ? router.go('#/') : window.history.back();
        });

        if (isUnauthorized) $('[data-action=go-back]', $view).html('Login');


        return $view;
    }
}
