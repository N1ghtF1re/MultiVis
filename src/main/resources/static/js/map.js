"use strict";

let emergencyMap;

// Дождёмся загрузки API и готовности DOM.
ymaps.ready(init);

function init () {
    // Создание экземпляра карты и его привязка к контейнеру с
    // заданным id ("map").

    let map,
        center = [28.2, 53.9],
        zoom = 6.5;

    emergencyMap = new ymaps.Map('map', {
        center: center,
        zoom: zoom,
        controls: []
    }, {
        minZoom: 6,
        suppressMapOpenBlock: true
    });

}


function ShowCity(Region, color, desc) {
    Region += ", Беларусь";
    // Получаем координаты полигона
    $.getJSON("https://open.mapquestapi.com/nominatim/v1/search.php?key=wqkGmne4e6ySwEdeJWrRw82vHArzkAgR&q=" + Region + "&format=json&polygon_geojson=1&limit=1")
        .then(function (data) {

            $.each(data, function(ix, place) {
                if ("relation" === place.osm_type) {
                    // Создаем полигон с нужными координатами
                    let p;
                    if ((place.geojson.coordinates[1] === undefined) || ( place.geojson.coordinates[1][1] !==  undefined)) {
                        p = new ymaps.Polygon(place.geojson.coordinates, {
                            hintContent: Region
                        }, {
                            fillColor: color,
                            strokeColor: color,
                            // Делаем полигон прозрачным для событий карты.
                            interactivityModel: 'default#transparent',
                            strokeWidth: 2,
                            opacity: 0.7
                        });
                    } else {
                        p = new ymaps.Polygon(place.geojson.coordinates[0], {
                            hintContent: Region
                        }, {
                            fillColor: color,
                            strokeColor: color,
                            // Делаем полигон прозрачным для событий карты.
                            interactivityModel: 'default#transparent',
                            strokeWidth: 2,
                            opacity: 0.7
                        });
                    }

                    // Добавляем полигон на карту
                    p.events.add('click', function () {
                        showSituatuinsPopup(Region, desc, sitsList, basicColors);
                    });
                    emergencyMap.geoObjects.add(p);
                }
            });
        }, function (err) {
            console.log(err);
        });
}

