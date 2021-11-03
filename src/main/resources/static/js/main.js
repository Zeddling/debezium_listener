$('document').ready(() => {
    var eSource = new EventSource("http://localhost:8055/subscribe");
    var data_rows = document.getElementById("data-rows");

    eSource.addEventListener('message', function(e) {
        var data = JSON.parse(e.data);
        console.log(data);
        if (data["operation"] === "u") {
            delete data.operation;
            var row = document.createElement("tr");

            for (let key in data) {
                var cell = document.createElement("td");
                cell.innerHTML = data[key];
                row.appendChild(cell);
            }

            data_rows.appendChild(row);
        }
    }, false);
});