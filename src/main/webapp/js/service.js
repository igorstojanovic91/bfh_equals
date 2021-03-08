const BASE_URL = 'http://localhost:8080/equals_war/api/';

export default {

    getPerson: function (user) {
        return $.ajax({
            url: BASE_URL + 'persons',
            type: 'GET',
            headers: {
                'Authorization': getAuthHeader(user),
                'accept': 'application/json'
            }
        }).fail(jqXHR => console.log(`An error occurred: (${jqXHR.status}) ${jqXHR.responseText}`));
    },

    getModules: function (user) {
        return $.ajax({
            url: BASE_URL + 'modules',
            type: 'GET',
            headers: {
                'Authorization': getAuthHeader(user),
                'accept': 'application/json'
            }
        }).fail(jqXHR => console.log(`An error occurred: (${jqXHR.status}) ${jqXHR.responseText}`));
    },

    getModulesOverall: function (user, moduleId) {
        return $.ajax({
            url: BASE_URL + 'modules/overall/' + moduleId,
            type: 'GET',
            headers: {
                'Authorization': getAuthHeader(user),
                'accept': 'application/json'
            }
        }).fail(jqXHR => console.log(`An error occurred: (${jqXHR.status}) ${jqXHR.responseText}`));
    },

    updateRatings: function (user, data) {
        return $.ajax({
            url: BASE_URL + 'ratings',
            type: 'PUT',
            headers: {
                'Authorization': getAuthHeader(user)
            },
            data: data,
            contentType: 'application/json; charset=UTF-8'
        }).fail(jqXHR => console.log(`An error occurred: (${jqXHR.status}) ${jqXHR.responseText}`));
    }

}

function getAuthHeader(user) {
    return 'Basic ' + btoa(user.username + ':' + user.password);
}
