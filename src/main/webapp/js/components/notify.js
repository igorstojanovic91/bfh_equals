import service from '../service.js';
import router from '../router.js';
import store from '../store.js';


export default {
    requiresAuth: true,

    getTitle: function () {
        return "Notify Professor";
    },

    render: function () {
        const $view = $($('#tpl-notify').html());


        const professor = store.getPersonToNotify()

        $('[name=professor]', $view).val(`${professor.firstName} ${professor.lastName}`)
        $('[name=email]', $view).val(`${professor.userName}@bfh.ch`)

        $('[data-action=save]', $view).on('click', e => {
            e.preventDefault();
            const form = document.forms[0];
            const emailAdress = form.email.value;
            const subject = form.subject.value;
            const message = form.message.value;
            window.open(`mailto:${emailAdress}?subject=${subject}&body=${message}`);
            store.deletePersonToNoftiy();
            $('form', $view).remove();
            router.go("/modules");
        });

        $('[data-action=cancel]', $view).on('click', e => {
            e.preventDefault();
            store.deletePersonToNoftiy()
            router.go("/modules")
        });

        return $view;
    }
};



