import store from '../store.js';

export default {
    requiresAuth: true,

    getTitle: function () {
        return "Modules";
    },

    render: function () {

        let $view = $($('#tpl-module-container').html());

        $('[data-field=title]').html('Your Module Overview:');
        $('[data-field=sub-title]').empty();
        let modules = store.getModules();
        let additionalRows = Math.floor(modules.length / 3);
        for (let i = 0; i < additionalRows; i++) {
            $view = $view.add($($('#tpl-module-container').html()))
        }

        for (let i = 0; i < modules.length; i++) {
            const module = modules[i];
            const row = Math.floor(i / 3);
            renderCardItem($view[row], module);
        }

        return $view;
    }
};

function renderCardItem($view, module) {
    const $item = $($('#tpl-module-item').html());
    const imgName = "img/" + module.shortName.split("-")[0].toLowerCase() + ".jpg"
    let link = "#/module-overview/" + module.moduleId;
    let tag;

    switch (module.role) {
        case "PROFESSOR":
            tag = "is-warning";
            break;
        case "HEAD":
            tag = "is-success";
            break;
        case "ASSISTANT":
            tag = "is-info";
            break;
        case "STUDENT" :
            tag = "is-link";
            link = "#/assessment/" + module.moduleId;
            break;
    }

    $('a', $item).attr('href', link);
    $('img', $item).attr("src", imgName);
    $('img', $item).attr("alt", module.name);

    $('p.title', $item).text(module.name);
    $('p.subtitle', $item).text(module.shortName);

    $('span.tag', $item).addClass(tag);
    $('span.tag', $item).text(capitalize(module.role));
    $('date', $item).first().text(module.startDate.join("-"));
    $('date', $item).first().attr("datetime", module.startDate.join("-"));
    $('date', $item).last().text(module.endDate.join("-"));
    $('date', $item).last().attr("datetime", module.endDate.join("-"));

    $('div:empty:first', $view).append($item);
}

function capitalize(str) {
    const lower = str.toLowerCase();
    return str.charAt(0).toUpperCase() + lower.slice(1);
}
