export default {
    showAuthContent: function (visible) {
        if (visible) {
            $('[data-auth=true]').fadeIn(200);
        } else {
            $('[data-auth=true]').hide();
        }
    },

    updateViewField(key, value) {
        $(`[data-field="${key}"]`).html(value);
    }
}
