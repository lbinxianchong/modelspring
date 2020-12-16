selectList()
function selectList() {
    var selectlist = $(".selectlist");
    var select = selectlist.attr("mo-selected");
    var url = selectlist.data("url");
    $.get(url, function (result) {
        var options = '';
        var data = result.data;
        for (var key in data) {
            var title = data[key].name;
            if (title == undefined || title == null) {
                title = data[key].title;
            }
            var id = data[key].id;
            var selected = select == id ? "selected=''" : "";
            options += "<option value='" + id + "' " + selected + ">" + title + "</option>";
        }
        selectlist.html(options);
    });

}
