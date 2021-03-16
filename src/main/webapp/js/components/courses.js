import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from "../util.js";

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
        util.showAuthContent(true);

        moduleIdentifier = moduleId;
        if (!moduleId) {
            store.setNotification('Invalid parameter! Expecting moduleId.');
            router.go("/modules")
            return;
        }
        const module = store.getModule(moduleId);
        if (module) {
            $('[data-field=title]').html(module.name)
            $('[data-field=sub-title]').html(module.shortName)
        } else {
            store.setNotification('The desired module was not found!');
            router.go('/modules');
            return;
        }
        service.getModulesOverall(store.getUser(), moduleId)
            .then(data => {
                initView($view, data);
            })

        $("[data-action=save]", $view).click(function (event) {
            event.preventDefault();
            $($view[1]).hide().parent().append($($('#tpl-loader')).html()).show();
            const data = getInputData();
            const promises = [];

            if(data.update.length > 0) promises.push(service.updateRatings(store.getUser(), JSON.stringify(data.update)));
            if(data.insert.length > 0) promises.push(service.insertRatings(store.getUser(), JSON.stringify(data.insert)));

            Promise.all(promises)
                .then(function () {
                    $('.hero.is-fullheight').remove();
                    $($view[1]).show();
                    $("[data-action=save]", $view).prop('disabled', true);
                    increaseVersions();
                    $('.notification-message').remove();
                    $('div .notification')
                        .removeClass('is-hidden')
                        .removeClass("is-warning")
                        .addClass("is-success")
                        .append('<p class="notification-message">Thank you! We saved all grades in the database!</p>')

                })
                .catch(jqXHR =>  {

                    $('.hero.is-fullheight').remove();
                    $($view[1]).show();
                    // Refetch Data with http error 400, the service then re-inits the data
                    if (jqXHR.status === 400) {
                        service.getModulesOverall(store.getUser(), moduleId)
                            .then(data => {
                                resetTable($view)
                                $('.notification-message').remove();
                                $('div .notification')
                                    .removeClass('is-hidden')
                                    .removeClass("is-success")
                                    .addClass("is-warning")
                                    .append('<p class="notification-message">Something went wrong. We reloaded the data for you. Please try again!</p>')
                                initView($view, data);
                            })
                    }
                })
        })

        $("[data-action=cancel]").click(function (event) {
            event.preventDefault()
            router.go("/modules")
        })

        $('.delete', $view).on("click", function () {
            $('div .notification').addClass("is-hidden").children("p").remove();
        })

        return $view;
    }
};




function initView($view, data) {
    const firstElement = data[0];
    createHeader($view, firstElement.courseRating);

    let coursesToNotify = [];
    let openGradesCounter = 0;
    data.forEach(item => {
        let tr = $('<tr></tr>');
        tr.append(`<td>${item.name}</td>`);
        item.courseRating.forEach((courseRating, index) => {
            if(store.getModule(moduleIdentifier).role === "PROFESSOR" || store.getModule(moduleIdentifier).role === "HEAD" ) {
                tr.append(`<td><input data-student="${courseRating.rating.studentId}" data-course="${courseRating.rating.courseId}" data-version="${courseRating.rating.version}" data-weight="${courseRating.course.weight}" class="input-grade" type="number" min="0" max="100" value="${courseRating.rating.successRate}" maxlength="3">%</td>`);
            } else {
                const notifyClass = courseRating.rating.successRate === 0 ? "has-background-danger-light" : "";
                tr.append(`<td class="${notifyClass}"><span data-student="${courseRating.rating.studentId}" data-course="${courseRating.rating.courseId}" data-version="${courseRating.rating.version}" data-weight="${courseRating.course.weight}">${courseRating.rating.successRate}</span>%</td>`);
                if(notifyClass){
                    !coursesToNotify.includes(index) ? coursesToNotify.push(index) : "";
                }
            }
            if(courseRating.rating.successRate === 0) openGradesCounter++;
        })
        store.setModuleHasOpenGrades(moduleIdentifier, openGradesCounter);
        tr.append(`<td data-field="prelim-grade">${item.preliminaryGrade}%</td>`);
        tr.append(`<td data-field="overall-grade">${item.overallGrade}%</td>`);
        $('tbody', $view).append(tr);
    })

    createFooter($view, firstElement.courseRating, coursesToNotify);
    //REMOVING COLUMNS FOR PROFESSOR
    if(store.getModule(moduleIdentifier).role === "PROFESSOR") {
        const table = $('table', $view)[0]
        const columnLength = table.rows[0].cells.length;

        $('table tr', $view).find(`td:eq(${columnLength-1}),th:eq(${columnLength-1})`).hide();
        $('table tr', $view).find(`td:eq(${columnLength-2}),th:eq(${columnLength-2})`).hide();
    }

    if (store.getModule(moduleIdentifier).role === "ASSISTANT") {
        $("[data-action=save]", $view).prop('disabled', true).text('Print');
    }

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

    // Highlight row and column corresponding the focused form input field
    $('input', $view).on('focus', function() {
        $(this).parent().parent().addClass('is-focused');
        $(`[data-course=${$(this).attr('data-course')}]`).parent().addClass('is-focused');
    })
    $('input', $view).on('focusout', function() {
        $(this).parent().parent().removeClass('is-focused');
        $(`[data-course=${$(this).attr('data-course')}]`).parent().removeClass('is-focused');
    })

    $('[data-action=notify]', $view).on("click", function (event) {
        const professorId = $(this).data("professorid")
        console.log(professorId)
        event.preventDefault();
        service.getPerson(store.getUser(), professorId)
            .then(professor => {
                store.setPersonToNotify(professor)
                router.go("/notify")
            })
            .catch( jqXHR => {
                console.log(jqXHR)
            })
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

function createFooter($view, courseRating, coursesToNotify) {
    const courseRatingReversed = [...courseRating].reverse();
    courseRatingReversed.forEach( (item, index) => {
        // append notifying Row for assistant
        let notifyButton = "";
        if(coursesToNotify.length && coursesToNotify.includes(courseRatingReversed.length - index -1)){
            notifyButton = $(`<br><a class="button is-danger is-light mt-2" title="Notify Professor by Mail" data-professorId="${item.course.professorId}" data-action="notify" href="#/notify">Notify</a>`);
        }
        $('tfoot tr:first', $view).prepend($(`<th data-average="${item.course.courseId}">&nbsp;</th>`));
        $('tfoot tr:last', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`).append(notifyButton));

    })
    $('tfoot tr:first', $view).prepend($('<th>Average</th>'));
    $('tfoot tr:first', $view).append($(`<th data-average="prelim">&nbsp;</th>`));
    $('tfoot tr:first', $view).append($(`<th data-average="overall">&nbsp;</th>`));
    $('tfoot tr:last', $view).prepend($('<th>&nbsp;</th>'));
}

function updateAllStatistics($view, $field) {
    const $row = $field.parent().parent();
    const $fields = ($('input', $row).length > 0) ? $('input', $row) : $('span', $row);
    updateXaxis($fields, $row);
    updateYaxis($field, $view);
    updateStatisticsBar($field, $view);
}


function updateXaxis($fields, $row) {
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
    const finalPreliminaryGrade = Math.round((preliminaryGrade / preliminaryWeight)) || 0;
    const finalOverallGrade = Math.round((overallGrade / overallWeight)) || 0;

    $('[data-field=prelim-grade]', $row).html(finalPreliminaryGrade  + '%');
    $('[data-field=overall-grade]', $row).html(finalOverallGrade + '%');
    (finalOverallGrade >= 50)
        ? $('[data-field=overall-grade]', $row).addClass('has-background-success-light').parent().removeClass('has-background-warning-light')
        : $('[data-field=overall-grade]', $row).removeClass('has-background-success-light').parent().addClass('has-background-warning-light');
}

function updateYaxis($field, $view) {
    const courseId = $field.attr('data-course');
    const numberOfStudents = $(`[data-course=${courseId}]`, $view).length;
    let sumOfCourseGrades = 0;
    $(`[data-course=${courseId}]`, $view).each(function() {
        sumOfCourseGrades += getValue($(this)) || 0;
    });
    const averageGrade = (sumOfCourseGrades / numberOfStudents).toFixed(2);
    $(`[data-average=${courseId}]`, $view).html(averageGrade + '%');

    // Average Prelim
    let sumOfPrelimGrades = 0;
    $('[data-field=prelim-grade]', $view).each(function() {
        sumOfPrelimGrades += parseInt($(this).html());
    });
    const averagePrelim = (sumOfPrelimGrades / numberOfStudents).toFixed(2);
    $('[data-average=prelim]', $view).html(averagePrelim + '%');
}

function updateStatisticsBar($field, $view) {
    const courseId = $field.attr('data-course');
    const numberOfStudents = $(`[data-course=${courseId}]`, $view).length;
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
    const averageOverall = (sumOfOverallGrades / numberOfStudents).toFixed(2);
    $('[data-average=overall]', $view).html(averageOverall + '%');
    $('[data-field=students-passed]', $view).html(studentsPassed);
    $('[data-field=average-grade]', $view).html(Math.round(sumOfOverallGrades / numberOfStudents) + '%');
    $('[data-field=best-grade]', $view).html(bestGrade + '%');
    $('[data-field=worst-grade]', $view).html(worstGrade + '%');
}



function getValue($this){
    return Number($this.val()) || Number($this.html());
}

function increaseVersions() {
    $('input').each(function () {
        let currentVersion = Number($(this).attr('data-version')) + 1;
        console.log('currentVersion: ' + currentVersion)
        $(this).attr('data-version', currentVersion);
    });
}

function getInputData() {
    const data = {};
    data.update = [];
    data.insert = [];
    let openGradesCounter = 0;
    $('input').each(function () {
        const versionNumber = Number($(this).attr("data-version"));
        const rating = {
            studentId: Number($(this).attr("data-student")),
            courseId: Number($(this).attr("data-course")),
            successRate: Number($(this).val()),
            version: versionNumber
        }
        versionNumber > 0 ? data.update.push(rating) : data.insert.push(rating);
        if(Number($(this).val()) === 0) openGradesCounter++;
    })
    store.setModuleHasOpenGrades(moduleIdentifier, openGradesCounter);
    return data;
}

function resetTable($view) {
    $('tbody', $view).empty();

    const columnLength = $('thead tr th').length
    $(`thead tr th:nth-child(-n+${columnLength-2})`).each(function () {
        $(this).remove();
    })

    $('tfoot tr:first').empty()

    $('tfoot tr:last').each(function () {
        $(`th:nth-child(-n+${columnLength-2})`, $(this)).remove();
    })

    $("[data-action=save]", $view).prop('disabled', true);
}
