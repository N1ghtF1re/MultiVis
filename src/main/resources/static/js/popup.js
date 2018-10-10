"use strict";

const CLOSE_STYLE = "top: -50%; opacity: 0";
const SHOW_STYLE = "";

window.onload = function() {
    let popup = document.createElement('div');
    popup.id = 'popup-window';
    popup.style = CLOSE_STYLE;

    let title = document.createElement('h1');
    title.id="popup-title";

    popup.appendChild(title);

    let content = document.createElement('p');
    content.id="popup-content";

    popup.appendChild(content);

    let btnClose = document.createElement("button");
    btnClose.innerText = "Закрыть";

    btnClose.onclick = function () {
        popup.style = CLOSE_STYLE;
    };

    popup.appendChild(btnClose);

    document.body.appendChild(popup);
};

function showPopup(content, title) {
    let popupWindow = document.getElementById("popup-window");
    popupWindow.style = SHOW_STYLE;

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
    for(let i = 0; i < sitArr.length; i++) {
        let newLi = document.createElement("li");

        let colorBlock = document.createElement("div");
        colorBlock.style.backgroundColor =  basicColors[i]['hex'];
        colorBlock.className = "color-demo";

        let sitNameEl = document.createElement("span");
        sitNameEl.className = "sitname";
        sitNameEl.innerHTML = sitNames[i]['name'] + " - ";

        let sitCountEl = document.createElement("span");
        sitCountEl.className = "sitcount";
        sitCountEl.innerHTML  = sitArr[i];

        let strTimes = document.createElement("span");
        strTimes.innerText = " раз";

        newLi.appendChild(colorBlock);
        newLi.appendChild(sitNameEl);
        newLi.appendChild(sitCountEl);
        newLi.appendChild(strTimes);

        contentUl.appendChild(newLi);
    }
    contentEl.appendChild(contentUl);

    let titleEl = document.getElementById('popup-title');
    titleEl.innerHTML = regionName;

    popupWindow.style = SHOW_STYLE;
}
