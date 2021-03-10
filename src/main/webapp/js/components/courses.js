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
            $('[data-field=title]').html(module.name)
            $('[data-field=sub-title]').html(module.shortName)
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

            // TODO: Flash message for success & failure
            Promise.all(promises)
                .then(function () {
                    $('.hero.is-fullheight').fadeOut(200).remove();
                    $($view[1]).fadeIn(200).show();
                    $("[data-action=save]", $view).prop('disabled', true);
                    increaseVersions();
                    $('div .notification').toggleClass('is-hidden').addClass("is-success").append("<p>Thank you! We saved all grades in the database!</p>")
                })
                .catch(jqXHR =>  {
                    console.log(jqXHR.status);
                    $('.hero.is-fullheight').fadeOut(200).remove();
                    $($view[1]).fadeIn(200).show();
                    // Refetch Data with http error 400, the service then re-inits the data
                    if (jqXHR.status === 400) {
                        service.getModulesOverall(store.getUser(), moduleId)
                            .then(data => {
                                $('tbody', $view).empty();
                                initView($view, data);
                                $('div .notification').toggleClass('is-hidden').addClass("is-warning").append("<p>Something went wrong. We reloaded the data for you. Please try again!</p>")
                            })
                    }
                })
        })

        $("[data-action=cancel]").click(function (event) {
            event.preventDefault()
            router.go("/modules")
        })

        $('.delete', $view).on("click", function () {
            $('div .notification').toggleClass("is-hidden").children("p").remove();
        })

        return $view;
    }
};




function initView($view, data) {
    const firstElement = data[0];
    createHeader($view, firstElement.courseRating);

    let coursesToNotify = [];
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
        })
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

function createFooter($view, courseRating, coursesToNotify) {
    const courseRatingReversed = [...courseRating].reverse();
    courseRatingReversed.forEach( (item, index) => {
        // append notifying Row for assistant
        let notifyButton = "";
        if(coursesToNotify.length && coursesToNotify.includes(courseRatingReversed.length - index -1)){
            notifyButton = $('<br><a class="button is-danger is-light mt-2" title="Notify Professor by Mail">Notify</a>');
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
    const finalPreliminaryGrade = Math.round((preliminaryGrade / preliminaryWeight)) || 0;
    const finalOverallGrade = Math.round((overallGrade / overallWeight)) || 0;

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
    const averageGrade = Math.round(sumOfCourseGrades / numberOfStudents);
    $(`[data-average=${courseId}]`, $view).html(averageGrade + '%');

    // Average Prelim
    let sumOfPrelimGrades = 0;
    $('[data-field=prelim-grade]', $view).each(function() {
        sumOfPrelimGrades += parseInt($(this).html());
    });
    const averagePrelim = Math.round(sumOfPrelimGrades / numberOfStudents);
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
    const averageOverall = Math.round(sumOfOverallGrades / numberOfStudents);
    $('[data-average=overall]', $view).html(averageOverall + '%');
    $('[data-field=students-passed]', $view).html(studentsPassed);
    $('[data-field=average-grade]', $view).html(averageOverall + '%');
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
