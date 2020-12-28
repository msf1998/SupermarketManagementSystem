function md5EncodePassword() {
    $(".password").val(md5($(".password").val()));
    return true;
}

function getMe(contextPath,_csrf) {
    $.ajax({
        url: contextPath + "/api/user/get",
        type: "post",
        async: true,
        dataType: "json",
        data: {"_csrf":_csrf},
        success: function (response) {
            $("#user-head").attr("src",contextPath + "/head/" + response.object.head)
            $("#user-name").text(response.object.name)
        }
    })
}

function formDataAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        async: true,
        contentType: false,
        processData:false,
        dataType: "json",
        data: data,
        success: function (response) {
            if (response.status == 1) {
                location.reload()
            } else {
                alert(response.describe)
            }
        }
    })
}

function defaultAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        data: data,
        success: function (response) {
            alert(response.describe)
        }
    })
}

function syncAjaxRequest(data,url) {
    return $.ajax({
        url: url,
        type: "post",
        data: data,
        async: false,
        success: function (response) {
            return response;
        }
    })
}

function jsonAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        contentType: "json",
        data: JSON.stringify(data),
        success: function (response) {
            alert(response.describe)
        }
    })
}