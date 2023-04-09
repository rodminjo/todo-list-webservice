const multipleImageDom = document.querySelector('.multipleImage');

const sliderImagesDom = document.querySelector('.sliderImages');

const sliderLeftBtnDom = document.querySelector('.left');

const sliderRightBtnDom = document.querySelector('.right');

let sliderLength = 0;

let current = 1;

let imageDom = [];

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
