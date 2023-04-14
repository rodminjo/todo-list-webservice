const commentWrapperDom = document.querySelector('.commentWrapper');

const photoWrapperDom = document.querySelector('.photoWrapper');

const photoDom = document.querySelector('.photo');

const multipleImageDom = document.querySelector('.multipleImage');

const photoToggleBtnDom = document.querySelector('.photoToggleBtn');

const commnetToggleBtn = document.querySelector('.commnetToggleBtn');

photoToggleBtnDom.addEventListener('click', () => {
  photoDom.classList.add('active');
  commentWrapperDom.classList.remove('active');
});

commnetToggleBtn.addEventListener('click', () => {
  commentWrapperDom.classList.add('active');
  photoDom.classList.remove('active');
});

function init() {
  photoWrapperDom.innerHTML = '';
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
    photoWrapperDom.appendChild(img);
  });
});
