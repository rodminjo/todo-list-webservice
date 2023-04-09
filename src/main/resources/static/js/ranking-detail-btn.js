function replyRankingSave() {

    var data = {
        content: $('#replyContent').val(),
    };

    $.ajax({
        type: 'POST',
        url: '/api/v1/ranking/'+id+'/reply',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data)
    })
        .done(function () {
            alert("댓글이 등록되었습니다.");
            window.location.href = "/ranking/detail/"+id;
        })
        .fail(function (error) {
            alert(JSON.stringify(error));
        });
}
function replyRankingUpdate(e) {

    var index = e;
    var contentText = '#update' + index;
    var replyContent = $(contentText).val();

    var data = {
        content: replyContent
    };

    var replyId = reply[index].id;

    $.ajax({
        type: 'POST',
        url: '/api/v1/ranking/'+id+'/reply/'+replyId,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data)
    })
        .done(function () {
            alert("댓글이 수정되었습니다.");
            window.location.href = "/ranking/detail/"+id;
        })
        .fail(function (error) {
            alert(JSON.stringify(error));
        });
}

function replyRankingDelete (e) {
    var index = e;

    var replyId = reply[index].id;

    $.ajax({
        type: "DELETE",
        url: "/api/v1/ranking/"+id+"/reply/"+replyId,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
    })
        .done(function () {
            alert("댓글이 삭제되었습니다.");
            window.location.href = "/ranking/detail/"+id;
        })
        .fail(function (error) {
            alert(JSON.stringify(error));
        });
}
