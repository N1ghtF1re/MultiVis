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

function makeRequest(startdate, enddate, mode) {
    $.ajax({
        url: "/api/regionsList",
        type: 'POST',
        data: 'startDate='+ startdate + '&endDate='+enddate + '&mode=' + mode,
        async: true,
        success: function(msg) {
            let cities = msg;
            for(let i = 0; i < cities.length; i++) {
                ShowCity(cities[i]['name'], cities[i]['color'], cities[i]['sits'])
            }
            document.getElementById("load").style.display = "none";
        },
        error: function(msg) {
            alert(msg);
        }
    });

}