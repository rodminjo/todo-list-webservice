const multipleImageDom = document.querySelector('.multipleImage');

const sliderImagesDom = document.querySelector('.sliderImages');

const sliderLeftBtnDom = document.querySelector('.left');

const sliderRightBtnDom = document.querySelector('.right');

let sliderLength = 0;

let current = 1;

let imageDom = [];

// 창 새로 열렸을때 이미지가 있다면 보여주기
window.onload = windowInit();
function windowInit() {
  if (storedImg.length > 0){
    const initImageList = [];

    storedImg.forEach((stored) => {
      const convert = '/display?file='+stored;
      initImageList.push(convert);
    })

    initImageList.forEach((convertImage,i) => {
      const img = document.createElement('img');
      img.src = convertImage;
      img.style.transform = `translateX(${400 * i}px)`;
      sliderImagesDom.appendChild(img);
      imageDom.push(img);
    })

    sliderLength = initImageList.length;
    if (sliderLength > 1) {
      sliderRightBtnDom.classList.remove('hidden');
    }
  }
}


function init() {
  sliderImagesDom.innerHTML = '<p>이미지를 업로드해주세요</p>';
  sliderLength = 0;
  imageDom = [];
  current = 1;
  sliderImagesDom.style.transform = `translateX(0)`;
  sliderRightBtnDom.classList.add('hidden');
  sliderLeftBtnDom.classList.add('hidden');
}

multipleImageDom.addEventListener('change', (e) => {
  init();

  const imageLists = [...e.target.files];

  if (imageLists.length > 3) {
    alert('3개까지만 업로드가능합니다');
    return;
  }

  const covertedImageList = [];

  imageLists.forEach((imageList) => {
    const convert = window.URL.createObjectURL(imageList);

    covertedImageList.push(convert);
  });

  covertedImageList.forEach((convertedImage, i) => {
    const img = document.createElement('img');
    img.src = convertedImage;

    img.style.transform = `translateX(${400 * i}px)`;
    // img.dataset.id = `${i}`; //임시

    sliderImagesDom.appendChild(img);

    imageDom.push(img);
  });
  sliderLength = covertedImageList.length;
  if (sliderLength > 1) {
    sliderRightBtnDom.classList.remove('hidden');
  }
});

sliderRightBtnDom.addEventListener('click', () => {
  sliderImagesDom.style.transform = `translateX(${-400 * current}px)`;

  if (current === sliderLength - 1) {
    sliderRightBtnDom.classList.add('hidden');
  }

  current++;
  if (current === 2) {
    sliderLeftBtnDom.classList.remove('hidden');
  }
});

sliderLeftBtnDom.addEventListener('click', () => {
  sliderImagesDom.style.transform = `translateX(${-400 * (current - 2)}px)`;
  if (current === 2) {
    sliderRightBtnDom.classList.remove('hidden');
  }
  current--;

  if (current === 1) {
    sliderLeftBtnDom.classList.add('hidden');
  }
});

// 정보 제출하기 위한 형식
var main = {
  init : function () {
    var _this = this;

    $('#btn-update').on('click', function () {
      _this.todoUpdate();
    });
    $('#btn-delete').on('click', function () {
      _this.todoDelete();
    });

  },

  todoUpdate : function () {
    var todoDate = $("#todoDate").val();

    var data = {
      todoDate: $('#todoDate').val(),
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
      url: '/api/v1/todo/'+id,
      processData: false,
      contentType: false,
      data: formData,

    })
        .done(function () {
          alert("글이 수정되었습니다.");
          window.location.href = "/todo/"+todoDate;
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  },


  todoDelete : function () {
    var todoId = $('#id').val();
    var todoDate = $("#todoDate").val();

    $.ajax({
      type: "DELETE",
      url: "/api/v1/todo/"+todoId,
      dataType: "json",
      contentType: "application/json; charset=utf-8",
    })
        .done(function () {
          alert("글이 삭제되었습니다.");
          window.location.href = "/todo/"+todoDate;
        })
        .fail(function (error) {
          alert(JSON.stringify(error));
        });
  }

}


main.init();

