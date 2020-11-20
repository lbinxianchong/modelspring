layui.use(['laypage', 'layer'], function () {
    var laypage = layui.laypage
        , layer = layui.layer;

    //完整功能
    laypage.render({
        elem: 'pagelay'
        , curr: $(".curr").val()
        , count: $(".count").val()
        , limit: $(".limit").val()
        , limits: [10, 50, 100, 500]
        , layout: ['skip', 'count', 'limit', 'prev', 'page', 'next']
        , jump: function (obj, first) {
            console.log(obj)
            console.log(window.location.href)
            console.log(window.location.pathname)
            console.log(window.location.search)
            if (!first) {
                var getPage = "";
                var getSearch = window.location.search;
                var get = "page=" + $(".curr").val() + "&size=" + $(".limit").val();
                console.log(get)
                getSearch=getSearch.replace("&" + get, "")
                getSearch=getSearch.replace("?" + get, "")
                console.log(getSearch)
                if (getSearch !== undefined && getSearch !== "") {
                    getPage = getSearch + "&page=" + obj.curr + "&size=" + obj.limit
                } else {
                    getPage = "?page=" + obj.curr + "&size=" + obj.limit
                }
                window.location.href = window.location.pathname + getPage;
                layer.msg('第 ' + obj.curr + ' 页');
            }
        }
    });

});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    ;
    return null;
}

function replaceParamVal(oUrl, name, replaceWith) {
    var re = eval('/(' + name + '=)([^&]*)/gi');
    var nUrl = oUrl.replace(re, name + '=' + replaceWith);
    this.location = nUrl;
    window.location.href = nUrl
}