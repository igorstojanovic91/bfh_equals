import service from '../service.js';
import router from '../router.js';
import store from '../store.js';

export default {
    getTitle: function () {
        return "Courses"; //TODO: dynamic title
    },

    render: function (moduleId) {
        let $statistics = $($('#tpl-course-statistics').html());
        let $table = $($('#tpl-courses').html());
        let $view = $statistics.add($table)
        if (!moduleId) {
            return $('<p>Invalid parameter! Expecting moduleId.</p>'); //TODO: define error class
        }
        const module = store.getModule(moduleId);
        if (module) {
            $('[data-field=title]').empty()
            $('[data-field=module-title]', $view).html(module.name)
        } else {
            router.go('/modules');
        }
        service.getModulesOverall(store.getUser(), moduleId)
            .then(data => initView($view, data))
        return $view;
    }
};

function initStatics($view, data) {
    console.log($view)
    $('[data-field=students-counter]', $view).html(data.length)
    let bestGrade = 0;
    let worstGrade = 100;
    let gradeCounter = 0;
    let passedStudents = 0;
    data.forEach(student => {
        if(student.overallGrade > bestGrade) bestGrade = student.overallGrade;
        if(student.overallGrade < worstGrade) worstGrade = student.overallGrade;
        if(student.overallGrade >= 50) passedStudents++;
        console.log(student.overallGrade)
        gradeCounter+=student.overallGrade;
    })
    $('[data-field=students-passed]', $view).html(passedStudents) // TODO
    $('[data-field=average-grade]', $view).html(gradeCounter / data.length) // TODO
    $('[data-field=best-grade]', $view).html(bestGrade) // TODO
    $('[data-field=worst-grade]', $view).html(worstGrade) // TODO
}

function initView($view, data) {
    console.log($view)
    initStatics($($view[0]), data);
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
