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

function getPolygon(region) {
    let result;
    $.ajax({
        url: "/api/polygons/get",
        type: 'GET',
        data: "region=" + region,
        async: false,
        success: function(msg) {
            result = msg
        }
    });
    return result;
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

function makeRequest(startdate, enddate) {
    $.ajax({
        url: "/api/regionsList",
        type: 'POST',
        data: 'startDate='+ startdate + '&endDate='+enddate,
        async: true,
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