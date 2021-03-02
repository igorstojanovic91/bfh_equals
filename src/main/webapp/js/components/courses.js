import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from "../util.js";

export default {
    getTitle: function() {
        return "Courses"; //TODO: dynamic title
    },

    render: function(moduleId) {
        let $view = $($('#tpl-courses').html());
        if(!moduleId){
            return $('<p>Invalid parameter! Expecting moduleId.</p>'); //TODO: define error class
        }
        service.getModulesOverall(store.getUser(), moduleId)
            .then(data => initView($view, data))
        return $view;
    }
};

function initView($view, data){
    const firstElement = data[0];
    createHeader($view, firstElement.courseRating);
    data.forEach(item => {

        let tr = $('<tr></tr>');
        tr.append(`<td>${item.name}</td>`);
        item.courseRating.forEach(courseRating => {
            tr.append(`<td>${courseRating.rating.successRate} %</td>`);
        })
        $('tbody', $view).append(tr);
    })
}

function createHeader($view, courseRating){
    courseRating.reverse().forEach(item => {
        $('thead tr', $view).prepend($(`<th><abbr title="${item.course.name}">${item.course.shortName}</abbr></th>`));
    })
    $('thead tr', $view).prepend($('<th>Student</th>'));
}