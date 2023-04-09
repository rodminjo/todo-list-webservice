const timer = document.querySelector(".js-timer"),
  stopBtn = document.querySelector(".js-timer_stopBtn"),
  startBtn = document.querySelector(".js-timer_startBtn"),
  saveBtn = document.querySelector(".js-timer_saveBtn"),
  refreshBtn = document.querySelector(".js-timer_refreshBtn");

let TIME = 0;
let cron;
var startTime;

function startButton() {
  updateTimer();
  stopButton();
  cron = setInterval(updateTimer, 1000);
  startTime = new Date().toISOString().substring(0, 19);
  timer.classList.add("start");
  timer.classList.add("refresh");
}

function stopButton() {
  clearInterval(cron);
  timer.classList.remove("start");
  timer.classList.remove("refresh");
}

function saveButton() {
  if (TIME!=0){
    if (confirm("타이머를 정지하면 다시 시작할 수 없습니다. 정지하고 저장하시겠습니까?")) {
      timerSave();
    }
  }else {
    window.location.href = "/timer";
  }
}

function refreshButton() {
  if (confirm("기존 시간은 저장되지 않습니다. 새로고침 하시겠습니까?")) {
    timer.innerText = "00:00:00";
    stopButton();
    return (TIME = 0);
  }
}

function updateTimer() {
  const hours = Math.floor(TIME / 3600);
  const checkMinutes = Math.floor(TIME / 60);
  const seconds = TIME % 60;
  const minutes = checkMinutes % 60;

  timer.innerText = `${hours < 10 ? `0${hours}` : hours}:${
    minutes < 10 ? `0${minutes}` : minutes
  }:${seconds < 10 ? `0${seconds}` : seconds}`;
  TIME++;
}

function timerSave() {
  var data = {
    title: $("#title").val(),
    content: $("#content").val(),
    category: $("#select_category").val(),
    doingTime: TIME,
    startTime: startTime,
  };

  $.ajax({
    type: "POST",
    url: "/api/v1/timer",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(data),
  })
      .done(function () {
        alert("저장되었습니다.")
        window.location.href = "/timer";
      })
      .fail(function (error) {
        alert(JSON.stringify(error));
      });
}
function exit(){
  if (TIME != 0){
    if (confirm("기존 시간은 저장되지 않습니다. 종료하시겠습니까?")){
      window.location.href = "/timer";
    }
  }else{
    window.location.href = "/timer";
  }
}

function init() {
  startBtn.addEventListener("click", startButton);
  stopBtn.addEventListener("click", stopButton);
  saveBtn.addEventListener("click", saveButton);
  refreshBtn.addEventListener("click", refreshButton);
}

init();
