import store from './store.js';
import error404 from "./components/error404.js";
import util from "./util.js";

const $main = $('main');

const routes = Object.create(null);

function replaceView($view) {
    $main.fadeOut(200, function () {
        $main.empty().append($view).fadeIn(300);
    });
}

function render() {
    const hash = location.hash.replace('#/', '').split('/');
    const path = '/' + (hash[0] || '');

    const component = routes[path] ? routes[path] : error404;

    if (component.requiresAuth && !store.getUser()) {
        replaceView($('<h2>401 Unauthorized</h2><p>Please login first!</p>'));
        return;
    }

    const param = hash.length > 1 ? hash[1] : null;
    const $view = component.render(param);
    replaceView($view);
    document.title = "EQualS" + (component.getTitle ? " - " + component.getTitle(param) : "");
}

$(window).on('hashchange', render);

export default {
    register: function (path, component) {
        if (!component['render'] || !component['render'] instanceof Function) {
            throw `Component '${path}' must contain a render function!`;
        }
        routes[path] = component;
    },

    go: function (path, param) {
        path += param ? '/' + param : '';
        if (location.hash !== '#' + path) {
            location.hash = path;
        } else {
            render();
        }
    }
}
