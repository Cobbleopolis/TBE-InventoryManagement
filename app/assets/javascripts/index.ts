$(function() {
    if(window.location.hash) {
        var hash = window.location.hash;
        $(hash).modal('toggle');
    }
});