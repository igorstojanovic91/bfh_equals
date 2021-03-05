import service from '../service.js';
import router from '../router.js';
import store from '../store.js';

let moduleIdentifier;
export default {
    getTitle: function () {
        return "Courses"; //TODO: dynamic title
    },

    render: function (moduleId) {
        let $statistics = $($('#tpl-course-statistics').html());
        let $table = $($('#tpl-courses').html());
        let $view = $statistics.add($table)
        moduleIdentifier = moduleId;
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

        $("[data-action=save]").click(function () {
            //TODO
        })

        $("[data-action=cancel]").click(function () {
            //TODO implement reading modules from store
            e.preventDefault()
            router.go("/modules")
        })



        return $view;
    }
};


function initStatics($view, data) {
    $('[data-field=students-counter]', $view).html(data.length)

    let bestGrade = 0;
    let worstGrade = 100;
    let gradeCounter = 0;
    let passedStudents = 0;
    let averageGrade;

    if(!(store.getModule(moduleIdentifier).role === "PROFESSOR")) {
        data.forEach(student => {
            if(student.overallGrade > bestGrade) bestGrade = student.overallGrade;
            if(student.overallGrade < worstGrade) worstGrade = student.overallGrade;
            if(student.overallGrade >= 50) passedStudents++;
            gradeCounter+=student.overallGrade;
        })
    } else {
        let grades = 0;
        let divider = data[0].courseRating.length * data.length;
        data.forEach(student => {
            let singleGrades = 0;
            student.courseRating.forEach(courseRating => {
                grades += courseRating.rating.successRate
                if(courseRating.rating.successRate > bestGrade) bestGrade = courseRating.rating.successRate;
                if(courseRating.rating.successRate < worstGrade) worstGrade = courseRating.rating.successRate;
            })
            passedStudents = (singleGrades / student.courseRating.length >= 50) ? passedStudents++ : passedStudents+=0;
        })
        averageGrade = grades / divider;
    }
    $('[data-field=students-passed]', $view).html(passedStudents) // TODO
    $('[data-field=average-grade]', $view).html(averageGrade ? averageGrade : gradeCounter / data.length) // TODO
    $('[data-field=best-grade]', $view).html(bestGrade) // TODO
    $('[data-field=worst-grade]', $view).html(worstGrade) // TODO

}

function initView($view, data) {
    initStatics($($view[0]), data);
    const firstElement = data[0];
    createHeader($view, firstElement.courseRating);

    data.forEach(item => {
        let tr = $('<tr></tr>');
        tr.append(`<td>${item.name}</td>`);
        item.courseRating.forEach(courseRating => {
            if(store.getModule(moduleIdentifier).role === "PROFESSOR" || store.getModule(moduleIdentifier).role === "HEAD" ) {
                tr.append(`<td><input class="input-grade" type="number" min="0" max="100" value="${courseRating.rating.successRate}" maxlength="3">%</td>`);
            } else {
                tr.append(`<td>${courseRating.rating.successRate}%</td>`);
            }

        })
        tr.append(`<td>${item.preliminaryGrade}%</td>`);
        tr.append(`<td>${item.overallGrade}%</td>`);
        $('tbody', $view).append(tr);
    })

    createFooter($view, firstElement.courseRating);
    console.log($('table', $view)[0].rows[0].cells);
    //REMOVING COLUMNS FOR PROFESSOR
    if(store.getModule(moduleIdentifier).role === "PROFESSOR") {
        const table = $('table', $view)[0]
        const columnLength = table.rows[0].cells.length;

        $('table tr', $view).find(`td:eq(${columnLength-1}),th:eq(${columnLength-1})`).remove();
        $('table tr', $view).find(`td:eq(${columnLength-2}),th:eq(${columnLength-2})`).remove();

    }
}

function createHeader($view, courseRating) {
    courseRating.reverse().forEach(item => {
        $('thead tr', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('thead tr', $view).prepend($('<th>Student</th>'));
}

function createFooter($view, courseRating) {
    courseRating.forEach( (item, index) => {
        $('tfoot tr:first', $view).prepend($(`<th>${calcCourseAverage(index+2, $view)}</th>`));
        $('tfoot tr:last', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('tfoot tr:last', $view).prepend($('<th>&nbsp;</th>'));
    $('tfoot tr:first', $view).prepend($('<th>Average</th>'))

}

function calcCourseAverage(columnIndex, $view) {
    let avg = 0, amount = 0;
    console.log($(`tr td:nth-of-type(${columnIndex})`, $view))
    $(`tr td:nth-of-type(${columnIndex})`, $view).each(function(){
        let value = $(this).find('input').attr("value") || parseInt($(this).text())
        console.log(value)
        avg += value;
        amount++;
    });
    console.log(avg)
    return avg / amount;
}
