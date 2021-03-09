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
            // TODO: Flash Message, Module does not exist
            router.go('/modules');
        }
        service.getModulesOverall(store.getUser(), moduleId)
            .then(data => {
                initView($view, data);
            })

        $("[data-action=save]", $view).click(function (event) {
            //TODO
            event.preventDefault();
            // CODE FOR FADE OUT AND SHOWING LOADING BUTTON
            $($view[1]).fadeOut(200).hide().parent().append($($('#tpl-loader')).html()).show().fadeIn(200);
            const data = {};
            data.update = [];
            data.insert = []
            $('input').each(function () {
                const versionNumber = Number($(this).attr("data-version"));
                const rating = {
                    studentId: Number($(this).attr("data-student")),
                    courseId: Number($(this).attr("data-course")),
                    successRate: Number($(this).val()),
                    version: versionNumber
                }
                versionNumber > 0 ? data.update.push(rating) : data.insert.push(rating);
            })
            const promises = [];
            console.log(data)

            if(data.update.length > 0) promises.push(service.updateRatings(store.getUser(), JSON.stringify(data.update)));
            if(data.insert.length > 0) promises.push(service.insertRatings(store.getUser(), JSON.stringify(data.insert)));

            Promise.all(promises)
                .then(function () {
                    $('.hero').fadeOut(200).detach();
                    $($view[1]).fadeIn(200).show();
                    $("[data-action=save]", $view).prop('disabled', false);
                })
                .catch(jqXHR => console.log(jqXHR.status))
        })

        $("[data-action=cancel]").click(function (event) {
            event.preventDefault()
            router.go("/modules")
        })

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
            if(store.getModule(moduleIdentifier).role === "PROFESSOR" || store.getModule(moduleIdentifier).role === "HEAD" ) {
                tr.append(`<td><input data-student="${courseRating.rating.studentId}" data-course="${courseRating.rating.courseId}" data-version="${courseRating.rating.version}" data-weight="${courseRating.course.weight}" class="input-grade" type="number" min="0" max="100" value="${courseRating.rating.successRate}" maxlength="3">%</td>`);
            } else {
                tr.append(`<td><span data-student="${courseRating.rating.studentId}" data-course="${courseRating.rating.courseId}" data-version="${courseRating.rating.version}" data-weight="${courseRating.course.weight}">${courseRating.rating.successRate}</span>%</td>`);
            }
        })
        tr.append(`<td data-field="prelim-grade">${item.preliminaryGrade}%</td>`);
        tr.append(`<td data-field="overall-grade">${item.overallGrade}%</td>`);
        $('tbody', $view).append(tr);
    })

    createFooter($view, firstElement.courseRating);
    //REMOVING COLUMNS FOR PROFESSOR
    if(store.getModule(moduleIdentifier).role === "PROFESSOR") {
        const table = $('table', $view)[0]
        const columnLength = table.rows[0].cells.length;

        $('table tr', $view).find(`td:eq(${columnLength-1}),th:eq(${columnLength-1})`).hide();
        $('table tr', $view).find(`td:eq(${columnLength-2}),th:eq(${columnLength-2})`).hide();

    }

    // TODO: Change to focusout??
    $('input', $view).on('input', function() {
        const value = $(this).val();
        if(!$.isNumeric(value) || value > 100 || value < 0){
            $("[data-action=save]", $view).prop('disabled', true)
            $(this).parent().addClass('has-background-danger-light');
        } else {
            $("[data-action=save]", $view).prop('disabled', false);
            $(this).parent().removeClass('has-background-danger-light');
            updateAllStatistics($view, $(this));
        }
    })

    $('[data-field=students-counter]', $view).html(data.length)

    const $fields = ($('input', $view).length > 0) ? $('input', $view) : $('span', $view);
    $fields.each(function () {
        updateAllStatistics($view, $(this))
    })

}

function createHeader($view, courseRating) {
    const courseRatingReversed = [...courseRating].reverse();
    courseRatingReversed.forEach(item => {
        $('thead tr', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('thead tr', $view).prepend($('<th>Student</th>'));
}

function createFooter($view, courseRating) {
    const courseRatingReversed = [...courseRating].reverse();
    courseRatingReversed.forEach( (item, index) => {
        $('tfoot tr:first', $view).prepend($(`<th data-average="${item.course.courseId}">${calcCourseAverage(index+2, $view)}%</th>`));
        $('tfoot tr:last', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('tfoot tr:first', $view).prepend($('<th>Average</th>'));
    $('tfoot tr:first', $view).append($(`<th data-average="prelim">${calcCourseAverage(courseRating.length + 1, $view)}%</th>`));
    $('tfoot tr:first', $view).append($(`<th data-average="overall">${calcCourseAverage(courseRating.length + 2, $view)}%</th>`));
    $('tfoot tr:last', $view).prepend($('<th>&nbsp;</th>'));
}

function updateAllStatistics($view, $field) {

    const $row = $field.parent().parent();
    const $fields = ($('input', $row).length > 0) ? $('input', $row) : $('span', $row);

    // x-axis
    let preliminaryWeight = 0;
    let overallWeight = 0;
    let preliminaryGrade = 0;
    let overallGrade = 0;

    $fields.each(function() {

        if (getValue($(this)) > 0) {
            preliminaryWeight += Number($(this).attr('data-weight'));
            preliminaryGrade += (getValue($(this)) * Number($(this).attr('data-weight')));
        }
        overallWeight += Number($(this).attr('data-weight'));
        overallGrade += (getValue($(this)) * Number($(this).attr('data-weight')));
    });
    console.log("weight " +overallWeight + " grade" + overallGrade);
    const finalPreliminaryGrade = Math.ceil((preliminaryGrade / preliminaryWeight)) || 0;
    const finalOverallGrade = Math.ceil((overallGrade / overallWeight)) || 0;

    $('[data-field=prelim-grade]', $row).html(finalPreliminaryGrade  + '%');
    $('[data-field=overall-grade]', $row).html(finalOverallGrade + '%');
    (finalOverallGrade >= 50)
        ? $('[data-field=overall-grade]', $row).addClass('has-background-success-light').parent().removeClass('has-background-warning-light')
        : $('[data-field=overall-grade]', $row).removeClass('has-background-success-light').parent().addClass('has-background-warning-light');


    // y-axis
    const courseId = $field.attr('data-course');
    const numberOfStudents = $(`[data-course=${courseId}]`, $view).length;
    let sumOfCourseGrades = 0;
    $(`[data-course=${courseId}]`, $view).each(function() {
        sumOfCourseGrades += getValue($(this)) || 0;
    });
    const averageGrade = Math.ceil(sumOfCourseGrades / numberOfStudents);
    $(`[data-average=${courseId}]`, $view).html(averageGrade + '%');

    // Average Prelim
    let sumOfPrelimGrades = 0;
    $('[data-field=prelim-grade]', $view).each(function() {
        sumOfPrelimGrades += parseInt($(this).html());
    });
    const averagePrelim = Math.ceil(sumOfPrelimGrades / numberOfStudents);
    $('[data-average=prelim]', $view).html(averagePrelim + '%');


    // Average Overall & Top Statistics
    let sumOfOverallGrades = 0;
    let studentsPassed = 0;
    let bestGrade = 0;
    let worstGrade = 0;

    $('[data-field=overall-grade]', $view).each(function(index) {
        index === 0 ? worstGrade = parseInt($(this).html()) : 0;
        let thisValue = parseInt($(this).html());
        sumOfOverallGrades += thisValue;
        if (thisValue >= 50) studentsPassed++;
        if (thisValue > bestGrade) bestGrade = thisValue;
        if (thisValue < worstGrade && thisValue > 0) worstGrade = thisValue;
    });
    const averageOverall = Math.ceil(sumOfOverallGrades / numberOfStudents);
    $('[data-average=overall]', $view).html(averageOverall + '%');
    $('[data-field=students-passed]', $view).html(studentsPassed);
    $('[data-field=average-grade]', $view).html(averageOverall + '%');
    $('[data-field=best-grade]', $view).html(bestGrade + '%');
    $('[data-field=worst-grade]', $view).html(worstGrade + '%');
}

function calcCourseAverage(columnIndex, $view) {
    let avg = 0, amount = 0;
    $(`tr td:nth-of-type(${columnIndex})`, $view).each(function(){
        let value = $(this).find('input').attr("value") || parseInt($(this).text())
        avg += value;
        amount++;
    });
    return avg / amount;
}

function getValue($this){
    return Number($this.val()) || Number($this.html());
}

