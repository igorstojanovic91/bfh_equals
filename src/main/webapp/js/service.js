const BASE_URL = 'https://locher.ti.bfh.ch/todo/api/';

export default {

    authenticate: function (user) {
        return $.ajax({
            url: BASE_URL + 'authenticate',
            type: 'GET',
            headers: {
                'Authorization': getAuthHeader(user),
                'accept': 'application/json'
            }
        }).fail(jqXHR => console.log(`An error occurred: (${jqXHR.status}) ${jqXHR.responseText}`));
    },

    getModules: function(user) {
        return $.ajax({
            url: BASE_URL + 'modules',
            type: 'GET',
            headers: {
                'Authorization': getAuthHeader(user),
                'accept': 'application/json'
            }
        }).fail(jqXHR => console.log(`An error occurred: (${jqXHR.status}) ${jqXHR.responseText}`));
    }

}

function getAuthHeader(user) {
    return 'Basic ' + btoa(user.username + ':' + user.password);
}