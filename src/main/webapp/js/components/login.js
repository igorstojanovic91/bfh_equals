import service from '../service.js';
import router from '../router.js';
import store from '../store.js';


export default {

    getTitle: function() {
        return "Login";
    },

    render: function() {
        const $view = $($('#tpl-login').html());

        $('[data-action=login]', $view).click(e => {
            e.preventDefault();
            processLogin($view);
        });

        return $view;
    }
};


function processLogin($view) {
    const user = getFormData();
    service.authenticate(user)
        .then(data =>  initAfterLogin(user, data))
        .catch(jqXHR => {
            let msg =  jqXHR.status === 401
                ? "Wrong username or password, please try again!"
                : "Ups, something failed!"
            $('[data-field=error]', $view).html(msg);
        });
}


function initAfterLogin(user) {
    store.setUser(user);
    router.go('/modules');
}

function getFormData() {
    const form = document.forms[0];
    console.log(form);
    return {
        username: form.username.value,
        password: form.password.value
    };
}
