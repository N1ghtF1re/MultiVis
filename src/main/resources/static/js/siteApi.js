"use strict";


function getSitsList() {
    $.ajax({
        url: "/api/sitsList",
        type: 'POST',
        async: false,
        success: function(msg) {
            sitsList = msg
        }
    });
}

function getBasicColors() {
    $.ajax({
        url: "/api/basicColors",
        type: 'POST',
        async: false,
        success: function(msg) {
            basicColors = msg
        }
    });
}
function getSitName(index) {
    console.log(sitsList[index-1]['name']);
    return sitsList[index-1]['name'];
}

function makeRequest() {
    $.ajax({
        url: "/api/regionsList",
        type: 'POST',
        data: 'startDate=2016-01-01&endDate=2017-01-01',
        async: false,
        success: function(msg) {
            let cities = JSON.parse(msg);
            for(let i = 0; i < cities.length; i++) {
                ShowCity(cities[i]['name'], cities[i]['color'], cities[i]['sits'])
            }
        },
        error: function(msg) {
            alert(msg);
        }
    });
}