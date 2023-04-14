// 글 업데이트
var main = {

  init : function () {
    var _this = this;
    $('#btn-update').on('click', function () {
      _this.postsUpdate();
    });
    $('#btn-delete').on('click', function () {
      _this.postsDelete();
    });
  },

  postsUpdate : function () {

    var data = {
      title: $('#boardTitle').val(),
      content: $('#boardContent').val()
    };
    var id = posts.id;
    var form = $('#form')[0];
    var formData = new FormData(form);

    var totalfiles = document.getElementById('files').files.length;
    for (var index = 0; index < totalfiles; index++) {
      formData.append("file", document.getElementById('files').files[index]);
    }
    formData.append('key', new Blob([JSON.stringify(data)], {type: "application/json"}));

    $.ajax({
      type: 'POST',
      url: '/api/v1/posts/'+id,
      processData: false,
      contentType: false,
      data: formData,

    })
        .done(function () {
          alert("글이 수정되었습니다.");
          window.location.href = "/posts/update/"+id;
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },


  postsDelete : function () {
    var id = posts.id;

    $.ajax({
      type: "DELETE",
      url: "/api/v1/posts/"+id,
      dataType: "json",
      contentType: "application/json; charset=utf-8",
    })
        .done(function () {
          alert("글이 삭제되었습니다.");
          window.location.href = "/posts";
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  }

}

main.init();

// 댓글 기능
// 답글이 선택되었는지 확인
var checked= false;
function replyPostsSave() {
  var parentIndex = parseInt($('#replyParentIndex').val());

  if (checked){
    var data = {
      content: $('#replyContent').val(),
      replyLevel: parseInt(reply[parentIndex].replyLevel)+1,
      replyParent: reply[parentIndex].nickName,
      replyGroup: reply[parentIndex].replyGroup
    };

    var id = posts.id;
    $.ajax({
      type: 'POST',
      url: '/api/v1/posts/'+id+'/reply',
      async : false,
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data)
    })
        .done(function () {
          alert("댓글이 등록되었습니다.");
          window.location.href = "/posts/update/"+id;
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  }else{

    var data = {
      content: $('#replyContent').val(),
      replyLevel: 0,
      replyParent: posts.nickName
    };

    var id = posts.id;

    $.ajax({
      type: 'POST',
      url: '/api/v1/posts/'+id+'/reply',
      async: false,
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data)
    })
        .done(function () {
          alert("댓글이 등록되었습니다.");
          window.location.href = "/posts/update/"+id;
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  }
}
function replyPostsUpdate(e) {

  var index = e;
  var contentText = '#update' + index;
  var replyContent = $(contentText).val();

  var data = {
    content: replyContent
  };

  var id = posts.id;
  var replyId = reply[index].id;

  $.ajax({
    type: 'POST',
    url: '/api/v1/posts/'+id+'/reply/'+replyId,
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(data)
  })
      .done(function () {
        alert("댓글이 수정되었습니다.");
        window.location.href = "/posts/update/"+id;
      })
      .fail(function (error) {
        alert(JSON.stringify(error));
      });
}

function replyPostsDelete (e) {
  var index = e;

  var id = posts.id;
  var replyId = reply[index].id;

  $.ajax({
    type: "DELETE",
    url: "/api/v1/posts/"+id+"/reply/"+replyId,
    dataType: "json",
    contentType: "application/json; charset=utf-8",
  })
      .done(function () {
        alert("댓글이 삭제되었습니다.");
        window.location.href = "/posts/update/"+id;
      })
      .fail(function (error) {
        alert(JSON.stringify(error));
      });
}

function replyToReply(e) {
  var index = e;

  var userNameText = posts.nickName;
  var replyContent = reply[index].content;

  $('#replyParentIndex').val(index);
  $('#replyShowName').text(userNameText);
  $('#replyShowContent').text(replyContent);

  $('.replyComment').show();
  checked = true;
}

function replyToReplyCancel(){
  $('.replyComment').hide();
  checked = false;
}