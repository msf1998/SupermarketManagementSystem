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

function defaultAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        async: true,
        contentType: false,
        processData:false,
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