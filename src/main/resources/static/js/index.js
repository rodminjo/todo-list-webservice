function setClock(){
    var dateInfo = new Date();
    var hour = modifyNumber(dateInfo.getHours());
    var min = modifyNumber(dateInfo.getMinutes());
    var sec = modifyNumber(dateInfo.getSeconds());
    document.getElementById("time").innerHTML = hour + ":" + min  + ":" + sec;
};
function modifyNumber(time){
    if(parseInt(time)<10){
        return "0"+ time;
    }
    else
        return time;
};

window.onload = function(){
    setClock();
    setInterval(setClock,1000); //1초마다 setClock 함수 실행
};