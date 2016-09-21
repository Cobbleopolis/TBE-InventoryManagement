import {Book} from "./models/book";

let book: Book;
export let info: JQuery;

$(function () {
    info = $("#info");
    book = new Book("someISBN", "This is a Book");
    if (window.location.hash) {
        var hash = window.location.hash;
        $(hash).modal('toggle');
    }
    $("#isbnSubmit").on("click", () => {
        $.ajax({
            type: 'GET',
            url: `/isbnLookup/${$('#isbnInput').val()}`,
            crossDomain: true,
            dataType: 'html',
            error(error: JQueryXHR, status: string, errorThrown: string) {
                info.html("<p>No books found.</p>")
            },
            success(data: any) {
                $("#info").html(data);
            }
        })
    });
});