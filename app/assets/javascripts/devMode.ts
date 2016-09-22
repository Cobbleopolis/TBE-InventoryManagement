let socket: WebSocket;

$(() => {
    $("#wsSubmit").on("click", () => {
        socket = new WebSocket($("#wsInput").val());
        socket.binaryType = "arraybuffer";
        socket.onmessage = (message: MessageEvent) => {
            let data: Uint8Array = new Uint8Array(message.data);
            let isbn: string = "";
            data.forEach((charCode: number) => isbn += String.fromCharCode(charCode));
            $("#isbnInput").val(decodeURIComponent(String.fromCharCode.apply(null, data)));
        };
    });
});