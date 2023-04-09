var main = {
  init : function () {
    var _this = this;
    $('#btn-save').on('click', function () {
      _this.todoSave();
    });
    $('#btn-update').on('click', function () {
      _this.todoUpdate();
    });
    $('#btn-delete').on('click', function () {
      _this.todoDelete();
    });
    $('#checked').on('click', function () {
      _this.todoChecked();
    });
  },

  todoSave: function () {
    var data = {
      title: $("#title").val(),
      content: $("#content").val(),
      checked: $("#checked").is(':checked'),
      todoDate: $("#todoDate").val(),
    };
    var todoDate =$("#todoDate").val();
    $.ajax({
      type: "POST",
      url: "/api/v1/todo/",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data)
    })
        .done(function () {
          alert("글이 등록되었습니다.");
          window.location.href = "/todo/"+todoDate+"/";
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },
  todoUpdate: function () {
    var data = {
      title: $("#title").val(),
      content: $("#content").val(),
      checked: $("#checked").is(':checked'),
      todoDate: $("#todoDate").val(),
    };
    var todoId = $('#todoId').val();
    var todoDate = $("#todoDate").val();
    $.ajax({
      type: "POST",
      url: "/api/v1/todo/"+todoId+"/",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data)
    })
        .done(function () {
          alert("글이 수정되었습니다.");
          window.location.href = "/todo/"+todoDate+"/";
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },
  todoDelete: function () {

    var todoId = $('#todoId').val();
    var todoDate = $("#todoDate").val();

    $.ajax({
      type: "DELETE",
      url: "/api/v1/todo/"+todoId+"/",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
    })
        .done(function () {
          alert("글이 삭제되었습니다.");
          window.location.href = "/todo/"+todoDate+"/";
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },
  todoUpdate: function () {
    var data = {
      checked: $("#checked").is(':checked')
    };
    var todoId = $('#todoId').val();
    $.ajax({
      type: "PATCH",
      url: "/api/v1/todo/"+todoId+"/",
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data)
    })
        .done(function () {
          // window.location.href = "/todo/"+todoDate+"/";
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  }

};

main.init();
