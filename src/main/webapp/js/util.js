
export default {
    showAuthContent: function(visible) {
        if (visible) {
            $('[data-auth=true]').fadeIn(400);
        } else {
            $('[data-auth=true]').fadeOut(400);
        }
    },

    updateViewField(key, value) {
        $(`[data-field="${key}"]`).html(value);
    }
}
