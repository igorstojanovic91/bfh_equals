
export default {
    showAuthContent: function(visible) {
        if (visible) {
            $('[data-auth=true]').fadeIn(500);
        } else {
            $('[data-auth=true]').fadeOut(500);
        }
    },

    updateViewField(key, value) {
        $(`[data-field="${key}"]`).html(value);
    }
}
