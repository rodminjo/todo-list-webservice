function dateChange () {
    var toDate = $('.dateInput').val();
    location.href = "/todo/" + toDate;
};

function addNewTodo () {
    var toDate = $('.dateInput').val();
    location.href = "/todo/save/" + toDate;
};

function importanceChange(id){
    var str = "#importance" + id;
    var data = {
        importance: $(str).val()
    };

    $.ajax({
        type: 'POST',
        url: '/api/v1/todo/importance/'+id,
        dataType: 'json',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
};

function checkedChange(id){
    var str = "#checked" + id;
    var data = {
        checked: $(str).is(':checked')
    };

    $.ajax({
        type: 'POST',
        url: '/api/v1/todo/checked/'+id,
        dataType: 'json',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });

};