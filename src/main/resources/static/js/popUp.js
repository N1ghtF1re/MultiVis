"use strict";

window.onload = function() {
    var popup = document.createElement('div');
    popup.id = 'popup-window';
    popup.style = "display: none; opacity: 0";

    var title = document.createElement('h1');
    title.id="popup-title";

    popup.appendChild(title);

    var content = document.createElement('p');
    content.id="popup-content";

    popup.appendChild(content);

    document.body.appendChild(popup);
}

function showPopup(content, title) {
    let popupWindow = document.getElementById("popup-window");
    popupWindow.style = "display: block; opacity:1";

    let contentEl = document.getElementById('popup-content');
    contentEl.innerHTML = content;

    let titleEl = document.getElementById('popup-title');
    titleEl.innerHTML = title
}

function showSituatuinsPopup(regionName, sitArr, sitNames, basicColors) {
    let popupWindow = document.getElementById("popup-window");

    let contentEl = document.getElementById('popup-content');
    contentEl.innerHTML = '';

    let contentUl = document.createElement("ul");
    for(var i = 0; i < sitArr.length; i++) {
        let newLi = document.createElement("li");

        let colorBlock = document.createElement("div");
        colorBlock.style.backgroundColor =  basicColors[i]['hex'];
        colorBlock.className = "color-demo";

        let sitNameEl = document.createElement("span");
        sitNameEl.className = "sitname";
        sitNameEl.innerHTML = sitNames[i]['name'];

        let sitCountEl = document.createElement("span");
        sitCountEl.className = "sitcount";
        sitCountEl.innerHTML  = sitArr[i];

        newLi.appendChild(colorBlock);
        newLi.appendChild(sitNameEl);
        newLi.appendChild(sitCountEl);

        contentUl.appendChild(newLi);
    }
    contentEl.appendChild(contentUl);

    let titleEl = document.getElementById('popup-title');
    titleEl.innerHTML = regionName;

    popupWindow.style = 'display: block; opacity:1';
}
