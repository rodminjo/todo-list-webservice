function sort() {
    var sort = $('#sort').val();
    const URLSearch = new URLSearchParams(location.search);
    URLSearch.set('sort', sort);
    const newParam = URLSearch.toString();

    window.open(location.pathname + '?' + newParam, '_self');
}