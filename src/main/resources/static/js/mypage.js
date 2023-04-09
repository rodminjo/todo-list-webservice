function idChecked(){
    var data = {
        nickName: $('#userNickName').val()
    };

    $.ajax({
        type: 'POST',
        url: '/api/v1/mypage/nickname',
        dataType: 'text',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(result){
        if(result === 'available'){

            $('#idCheck').text("이 닉네임은 사용할 수 있습니다.");
            $('#userNickName').css('background-color', 'blue');
            $('#idCheck').css('margin-bottom', '10px');

        }else{
            $('#idCheck').text("이 닉네임은 사용할 수 없습니다.");
            $('#userNickName').css('background-color', 'red');
            $('#idCheck').css('margin-bottom', '10px');
        }
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
};
function idChange(){
    var data = {
        nickName: $('#userNickName').val()
    };

    $.ajax({
        type: 'POST',
        url: '/api/v1/mypage/change-nickname',
        dataType: 'text',
        contentType:'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(result){
        if(result=='done'){
            alert("수정되었습니다.");
            location.href = '/mypage';
        }else {
            alert("실패하였습니다.");
        }

    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
};