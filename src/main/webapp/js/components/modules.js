import store from '../store.js';
import util from "../util.js";

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

        isFilteredForMissingGrades = false;
        isFilteredBySemester = "all"

        let $view = initContainers();
        const modules = store.getModules();
        util.showAuthContent(true);

        // Display filters
        const isStudent = modules.find(mod => mod.role === "STUDENT");
        if (!isStudent) {
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

            updateButton(modules, $view)
        }

        // Display flash messages
        if (store.getNotification()) {
            let $notification = $($('#tpl-notification').html());
            $('[data-field=notification]', $notification).append(`<p class="notification-message">${store.getNotification()}</p>`);
            store.clearNotification();
            $view = $notification.add($view);
        }
        $('.delete', $view).on("click", function () {
            $('[data-field=notification]').parent().remove();
        })

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

    const startDate = [...module.startDate].reverse();
    const endDate = [...module.endDate].reverse();

    if (Date.parse(module.endDate) < new Date($.now())) {
        $('.card', $item).addClass('module-has-ended');
    }

    $('div.tags', $item).append(`<span class="tag is-light ${tag}">${capitalize(module.role)}</span>`);
    $('time', $item).first().text(startDate.join("."));
    $('time', $item).first().attr("datetime", startDate.join("-"));
    $('time', $item).last().text(endDate.join("."));
    $('time', $item).last().attr("datetime", endDate.join("-"));

    if (module.role !== 'STUDENT') {
        if (module.hasOpenGrades) {
            $('div.tags', $item).append(`<span class="tag is-danger">Missing grades</span>`);
        } else {
            $('div.tags', $item).append(`<span class="tag is-success">Grades completed</span>`);
        }
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
    if (isFilteredForMissingGrades) {
        moduleList = moduleList.filter(mod => mod.hasOpenGrades)
    }
    if (isFilteredBySemester !== "all") {
        moduleList = moduleList.filter(mod => mod.shortName.split("-")[1] === isFilteredBySemester)
    }
    updateButton(moduleList);
    return moduleList;
}

function initContainers() {
    const moduleList = filterModules();
    const $template = $('#tpl-module-container');
    let $view = $($template.html());
    let additionalRows = Math.floor(moduleList.length / 3);
    for (let i = 0; i < additionalRows; i++) {
        $view = $view.add($($template.html()))
    }
    for (let i = 0; i < moduleList.length; i++) {
        const module = moduleList[i];
        const row = Math.floor(i / 3);
        renderCardItem($view[row], module);
    }

    return $view;
}

function updateButton(modules, $view) {
    const $filterButton = $view ? $('[data-action=missing-grades]', $view) : $('[data-action=missing-grades]');
    modules.every(mod => !mod.hasOpenGrades)
        ? $filterButton.prop('disabled', true).text("No Missing Grades")
        : $filterButton.prop('disabled', false).text("Missing Grades");
}
