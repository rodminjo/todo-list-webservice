var main = {
  init : function () {
    var _this = this;
    $('#btn-save').on('click', function () {
      _this.postsSave();
    });
    $('#btn-update').on('click', function () {
      _this.postsUpdate();
    });
    $('#btn-delete').on('click', function () {
      _this.postsDelete();
    });

  },
  postsSave : function () {
    var data = {
      title: $('#title').val(),
      content: $('#content').val()
    };

    var form = $('#form')[0];
    var formData = new FormData(form);

    var totalfiles = document.getElementById('files').files.length;
    for (var index = 0; index < totalfiles; index++) {
      formData.append("file", document.getElementById('files').files[index]);
    }
    formData.append('key', new Blob([JSON.stringify(data)], {type: "application/json"}));

    $.ajax({
      type: 'POST',
      url: '/api/v1/posts',
      processData: false,
      contentType: false,
      data: formData,

    })
        .done(function () {
          alert("글이 등록되었습니다.");
          window.location.href = "/posts";
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },

  postsUpdate : function () {
    var data = {
      title: $('#title').val(),
      content: $('#content').val()
    };
    var id = $('#id').val();
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
          window.location.href = "/posts/"+id;
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },


  postsDelete : function () {
    var id = $('#id').val();

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
