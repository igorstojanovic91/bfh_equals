import service from '../service.js';
import router from '../router.js';
import store from '../store.js';


export default {

    getTitle: function() {
        return "Modules";
    },

    render: function() {

        let $view = $($('#tpl-module-container').html())

        for(let i = 0; i < store.getModules().length; i++) {
            const module = store.getModules()[i];
            console.log(module)
            /**
            if(i % 3 === 0 && i > 0) {
                console.log("got in modulo")
                $view.append($($('#tpl-module-container').html()));
            }
             */
            console.log("render")
            renderCardItem($view, module)
        }


        return $view;
    }
};


function renderCardItem($view, module) {
    const $item = $($('#tpl-module-item').html());
    const imgName = "img/" + module.shortName.split("-")[0].toLowerCase() + ".jpg"

    $('img', $item).attr("src", imgName);
    $('img', $item).attr("alt", module.name);

    $('p.title', $item).text(module.name);
    $('p.subtitle', $item).text(module.shortName);
    let tag;
    switch(module.role) {
        case "PROFESSOR": tag = "is-warning"; break;
        case "HEAD": tag = "is-success"; break;
        case "ASSISTANT": tag = "is-info"; break;
        case "STUDENT" : tag = "is-link"; break;
    }
    console.log(module.role)
    console.log(tag)
    $('span.tag', $item).addClass(tag);
    $('span.tag', $item).text(capitalize(module.role));
    $('date', $item).first().text(module.startDate.join("-"))
    $('date', $item).first().attr("datetime",module.startDate.join("-"))
    $('date', $item).last().text(module.endDate.join("-"))
    $('date', $item).last().attr("datetime",module.endDate.join("-"))

    $view.append($item);
}

function capitalize(str) {
    const lower = str.toLowerCase();
    return str.charAt(0).toUpperCase() + lower.slice(1);
}
