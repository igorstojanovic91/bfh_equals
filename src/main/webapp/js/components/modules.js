import store from '../store.js';

let isFilteredForMissingGrades = false;
let isFilteredBySemester = "all"

export default {
    requiresAuth: true,

    getTitle: function () {
        return "Modules";
    },

    render: function () {


        $('[data-field=title]').html('Your Module Overview:');
        $('[data-field=sub-title]').empty();


        let $view = initContainers();
        const modules = store.getModules();
        const isStudent = modules.find(mod => mod.role === "STUDENT");

        if(!isStudent) {
            let $filter = $($('#tpl-filter').html())
            initFilter($filter)
            $view = $filter.add($view)

            $('[data-action=missing-grades]', $view).on("click", function () {
                isFilteredForMissingGrades = !isFilteredForMissingGrades
                $('[data-action=missing-grades]').toggleClass('is-light');
                renderCards()
            })

            $('select', $view).on("change", function () {
                isFilteredBySemester = $(this).val();
                renderCards();
            })


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

    $('div.tags', $item).append(`<span class="tag is-light ${tag}">${capitalize(module.role)}</span>`);
    $('date', $item).first().text(module.startDate.join("-"));
    $('date', $item).first().attr("datetime", module.startDate.join("-"));
    $('date', $item).last().text(module.endDate.join("-"));
    $('date', $item).last().attr("datetime", module.endDate.join("-"));

    if (module.role !== 'STUDENT') {
        if(module.hasOpenGrades){
            $('div.tags', $item).append(`<span class="tag is-danger">Missing grades</span>`);
        } else {
            $('div.tags', $item).append(`<span class="tag is-success">Grades completed</span>`);
        }
    }

    if (Date.parse(module.endDate) < new Date($.now())) {
        $('.card', $item).addClass('module-has-ended');
    }

    $('div:empty:first', $view).append($item);
}

function capitalize(str) {
    const lower = str.toLowerCase();
    return str.charAt(0).toUpperCase() + lower.slice(1);
}

function initFilter($view) {
    const modules = store.getModules();
    const semesters = [...new Set(modules.map(item => item.shortName.split("-")[1]))];
    semesters.forEach(semester => {
        $('select', $view).append(`<option>${semester}</option>`)
    })
}

function renderCards() {
    $('.columns').remove()
    const $view = initContainers()
    $('main').append($view);
}


function filterModules() {
    let moduleList = store.getModules();
    if(isFilteredForMissingGrades) {
        moduleList = moduleList.filter(mod => mod.hasOpenGrades)
    }
    if(isFilteredBySemester !== "all") {
        moduleList = moduleList.filter(mod => mod.shortName.split("-")[1] === isFilteredBySemester)
    }
    return moduleList;
}

function initContainers() {
    const moduleList = filterModules();
    let $view = $($('#tpl-module-container').html());
    let additionalRows = Math.floor(moduleList.length / 3);
    for (let i = 0; i < additionalRows; i++) {
        $view = $view.add($($('#tpl-module-container').html()))
    }
    for (let i = 0; i < moduleList.length; i++) {
        const module = moduleList[i];
        const row = Math.floor(i / 3);
        renderCardItem($view[row], module);
    }
    return $view;
}
