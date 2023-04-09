function convertDoingTime(){
    var hours = parseInt(total / 3600);
    var minutes = parseInt((total % 3600) / 60);
    var seconds = (total % 3600) % 60;

    var convert = `${hours < 10 ? `0${hours}` : hours}:${
        minutes < 10 ? `0${minutes}` : minutes
    }:${seconds < 10 ? `0${seconds}` : seconds}`;

    document.getElementById("time").innerHTML = convert;
};

window.onload = function(){
    convertDoingTime();
};