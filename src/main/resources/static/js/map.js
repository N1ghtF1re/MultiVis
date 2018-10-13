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


async function ShowCity(Region, color, desc) {
    Region += ", Беларусь";
    let p;
    let coordinates
    try{
        coordinates = JSON.parse(getPolygon(Region));
    } catch (err) {
        console.log(getPolygon(Region));
        return;
    }
    p = new ymaps.Polygon(coordinates, {
        hintContent: Region
    }, {
        fillColor: color,
        strokeColor: color,
        // Делаем полигон прозрачным для событий карты.
        interactivityModel: 'default#transparent',
        strokeWidth: 2,
        opacity: 0.7
    });

    // Добавляем полигон на карту
    p.events.add('click', function () {
        showSituatuinsPopup(Region, desc, sitsList, basicColors);
    });
    emergencyMap.geoObjects.add(p);
}

