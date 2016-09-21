
import {Book} from "./models/book";
let book: Book;

$(function() {
    book = new Book("someISBN", "This is a Book");
    if(window.location.hash) {
        var hash = window.location.hash;
        $(hash).modal('toggle');
    }
});