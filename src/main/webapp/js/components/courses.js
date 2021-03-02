import service from '../service.js';
import router from '../router.js';
import store from '../store.js';

export default {
    getTitle: function () {
        return "Courses"; //TODO: dynamic title
    },

    render: function (moduleId) {
        let $view = $($('#tpl-courses').html());
        if (!moduleId) {
            return $('<p>Invalid parameter! Expecting moduleId.</p>'); //TODO: define error class
        }
        const module = store.getModule(moduleId);
        if (module) {
            $('[data-field=title]').html(module.name);
        } else {
            router.go('/modules');
        }

        service.getModulesOverall(store.getUser(), moduleId)
            .then(data => initView($view, data))
        return $view;
    }
};

function initView($view, data) {
    const firstElement = data[0];
    createHeader($view, firstElement.courseRating);
    data.forEach(item => {

        let tr = $('<tr></tr>');
        tr.append(`<td>${item.name}</td>`);
        item.courseRating.forEach(courseRating => {
            tr.append(`<td>${courseRating.rating.successRate}%</td>`);
        })
        tr.append(`<td>${item.preliminaryGrade}%</td>`);
        tr.append(`<td>${item.overallGrade}%</td>`);
        $('tbody', $view).append(tr);
    })
    createFooter($view, firstElement.courseRating);
}

function createHeader($view, courseRating) {
    courseRating.reverse().forEach(item => {
        $('thead tr', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('thead tr', $view).prepend($('<th>Student</th>'));
}

function createFooter($view, courseRating) {
    courseRating.forEach(item => {
        $('tfoot tr:last', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('tfoot tr:last', $view).prepend($('<th>&nbsp;</th>'));
}
