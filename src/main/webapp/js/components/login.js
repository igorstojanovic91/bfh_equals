import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from "../util.js";


export default {

    getTitle: function() {
        return "Login";
    },

    render: function() {
        const $view = $($('#tpl-login').html());

        $('[data-action=login]', $view).click(e => {
            e.preventDefault();
            $('[data-field=error]', $view).empty();
            processLogin($view);
        });

        return $view;
    }
};


function processLogin($view) {
    const user = getFormData();
    $('form', $view).fadeOut(100, function () {
        $('form', $view).empty().append($($('#tpl-loader')).html()).fadeIn(100)
    })

    service.authenticate(user)
        .then(data => {
            initAfterLogin(data)

            return service.getModules(user)
        })
        .then(moduleList => {if(moduleList) setModules(moduleList)})
        .catch(jqXHR => {
            let msg =  jqXHR.status === 401
                ? "Wrong username or password, please try again!"
                : "Ups, something failed!"
            $('[data-field=error]', $view).html(msg);
        });

}


function initAfterLogin(userData) {
    store.setUser(userData);
}

function  setModules(moduleList) {
    store.setModules(moduleList)
    util.showAuthContent(true);
    router.go('/modules'); //TODO: REMOVE THIS
}

function getFormData() {
    const form = document.forms[0];
    console.log(form);
    return {
        username: form.username.value,
        password: form.password.value
    };
}
