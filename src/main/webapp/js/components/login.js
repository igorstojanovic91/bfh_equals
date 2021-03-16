import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from "../util.js";

export default {
    requiresAuth: false,

    getTitle: function () {
        return "Login";
    },

    render: function () {


        if(store.getUser()) {
            router.go("/modules")
            return
        }

        util.showAuthContent(false);

        const $view = $($('#tpl-login').html());
        $('[data-action=login]', $view).on('click', e => {
            e.preventDefault();
            processLogin($view);
        });


        $('input', $view).on("input", function () {
            const data = getFormData()
            if (data.username && data.password) $('[data-action=login]', $view).prop("disabled", false)
        })

        return $view;
    }
};

function processLogin($view) {
    const user = getFormData();
    $('form', $view).fadeOut(300, function () {
        $('form', $view).parent().hide();
        $('div.column:last', $view).append($($('#tpl-loader')).html()).show().fadeIn(150);
    })
    service.getModules(user)
        .then(moduleList => {
            initAfterLogin(user);
            setModules(moduleList);
        })
        .catch(jqXHR => {
            setTimeout(function () {
                let msg = jqXHR.status === 401
                    ? "Wrong username or password, please try again!"
                    : "Ups, something failed!"
                $('[data-field=error]', $view).html(msg);
                $('.hero.is-fullheight', $view).remove();
                $('form', $view).fadeIn(400).show();
            }, 500)
        })
}

function initAfterLogin(userData) {
    if (userData) store.setUser(userData);
}

function setModules(moduleList) {
    store.setModules(moduleList)
    util.showAuthContent(true);
    router.go('/modules');
}

function getFormData() {
    const form = document.forms[0];
    return {
        username: form.username.value,
        password: form.password.value
    };
}
