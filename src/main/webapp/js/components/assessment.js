import service from '../service.js';
import router from '../router.js';
import store from '../store.js';

let moduleIdentifier;

export default {
    requiresAuth: true,

    getTitle: function (moduleId) {
        const module = store.getModule(moduleId || 0);
        return module ? module.name : 'Module Overview';
    },

    render: function (moduleId) {
        let $view = $($('#tpl-assessment').html());
        moduleIdentifier = moduleId;
        if (!moduleId) {
            return $('<p>Invalid parameter! Expecting moduleId.</p>'); //TODO: define error class
        }
        const module = store.getModule(moduleId);
        if (module) {
            $('[data-field=title]').html(module.name)
            $('[data-field=sub-title]').html(module.shortName)
        } else {
            // TODO: Flash Message, Module does not exist
            router.go('/modules');
        }
        service.getModulesOverall(store.getUser(), moduleId)
            .then(data => {
                initView($view, data[0]);
            })

        $("[data-action=cancel]").click(function (event) {
            event.preventDefault()
            router.go("/modules")
        })
        return $view;
    }
};




function initView($view, data) {
    let preliminaryWeight = 0;
    let overallWeight = 0;
    let preliminaryGrade = 0;
    let overallGrade = 0;

    data.courseRating.forEach(item => {
        let tr = $('<tr></tr>');
        tr.append(`<td class="has-text-left">${item.course.name}</td>`);
        tr.append(`<td>${item.course.weight}</td>`);
        tr.append(`<td>${item.rating.successRate}</td>`);
        $('tbody', $view).append(tr);

        if (Number(item.rating.successRate) > 0) {
            preliminaryWeight += Number(item.course.weight);
            preliminaryGrade += (Number(item.rating.successRate) * Number(item.course.weight));
        }
        overallWeight += Number(item.course.weight);
        overallGrade += (Number(item.rating.successRate) * Number(item.course.weight));
    });
    const finalPreliminaryGrade = Math.round((preliminaryGrade / preliminaryWeight)) || 0;
    const finalOverallGrade = Math.round((overallGrade / overallWeight)) || 0;

    $('[data-field=prelim-grade]', $view).html(finalPreliminaryGrade);
    $('[data-field=overall-grade]', $view).html(finalOverallGrade);
}
