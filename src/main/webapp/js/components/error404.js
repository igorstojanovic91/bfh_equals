import util from "../util.js";

export default {

    getTitle: function() {
        return 'Error 404';
    },

    render: function() {
        const $view = $($('#tpl-404').html());
        util.showAuthContent(false);


        $('[data-action=go-back]', $view).on('click', e => {
            e.preventDefault();
            window.history.back();
        });

        return $view;
    }
}
