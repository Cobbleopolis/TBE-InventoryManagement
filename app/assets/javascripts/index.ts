import {Book} from './models/book';

let book: Book;
export let info: JQuery;
let loadingHTML = '<i class="fa fa-circle-o-notch fa-spin" style="font-size:48px"></i>';

$(() => {
    info = $('#info');
    book = new Book('someISBN', 'This is a Book');
    if (window.location.hash) {
        var hash = window.location.hash;
        $(hash).modal('toggle');
    }
    $('#isbnSubmit').on('click', () => {
        isbnLookup($('#isbnInput').val())
    });
});

export function isbnLookup(isbn: string): void {
    info.html(loadingHTML);
    $.ajax({
        type: 'GET',
        url: `/isbnLookup/${isbn}`,
        dataType: 'html',
        error(error: JQueryXHR, status: string, errorThrown: string) {
            console.error(status + ' | ' + errorThrown, error);
            info.html(`<p>${error.responseText}</p>`);
        },
        success(data: any) {
            info.html(data);
        }
    })
}