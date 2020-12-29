function getWillGoBad() {
    $.ajax({
        url: contextPath + "/api/product/list/bad",
        type: "post",
        data: {"_csrf": _csrf},
        success: function (response) {
            if (response.status == 1) {
                let o = response.object;
                console.log(o)
            } else {
                alert(response.describe)
            }
        }
    })
}

function getLessProduct() {
    $.ajax({
        url: contextPath + "/api/product/list/less",
        type: "post",
        data: {"_csrf":_csrf}
    })
}

