function exit(){
    window.location.href = "/timer";
};

function timerUpdate(){
    var id = $('#timerId').val();
    if (confirm("수정하시겠습니까?")){
        var data = {
            title: $("#title").val(),
            content: $("#content").val(),
            category: $("#select_category").val()
        };

        $.ajax({
            type: "POST",
            url: "/api/v1/timer/"+id,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
        })
            .done(function () {
                alert("수정되었습니다.")
                window.location.href = "/timer/update/"+id;
            })
            .fail(function (error) {
                alert(JSON.stringify(error));
            });
    }
};

function timerDelete(){
    var id = $('#timerId').val();

    if (confirm("삭제하시겠습니까?")) {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/timer/" + id,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
        })
            .done(function () {
                alert("기록이 삭제되었습니다.");
                window.location.href = "/timer";
            })
            .fail(function (error) {
                alert(JSON.stringify(error));
            });

    }
};

function convertStartTime(){

    var convert = start.substring(0,10) +"  " + start.substring(11);

    document.getElementById("date").innerHTML = convert;
};

function convertDoingTime(){
    var hours = parseInt(doing / 3600);
    var minutes = parseInt((doing % 3600) / 60);
    var seconds = (doing % 3600) % 60;

    var convert = `${hours < 10 ? `0${hours}` : hours}:${
        minutes < 10 ? `0${minutes}` : minutes
    }:${seconds < 10 ? `0${seconds}` : seconds}`;

    document.getElementById("time").innerHTML = convert;
};

window.onload = function(){
    convertStartTime();
    convertDoingTime();
};