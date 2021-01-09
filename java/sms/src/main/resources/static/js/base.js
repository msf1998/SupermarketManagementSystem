let user = {}
function md5EncodePassword() {
    $(".password").val(md5($(".password").val()));
    return true;
}

function checkFile(file) {
    var pattern = /^.*\.png$|^.*\.jpg$/
    if (pattern.test(file) != true) {
        $("#leading-in-head-btn").attr("disabled",true)
        alert("仅支持.jpg和.png格式的图片");
    } else {
        $("#leading-in-head-btn").attr("disabled",false)
    }
}

function checkPassword(password) {
    if (password == "" || password == null || password == undefined) {
        $("#change-password-btn").attr("disabled",true)
    } else {
        $("#change-password-btn").attr("disabled",false)
    }
}

function editHead() {
    var head = $("#leading-in-head-input")[0].files[0];
    var formData = new FormData();
    formData.append("head",head);
    formData.append("_csrf",_csrf);
    $.ajax({
        url: contextPath + "/api/user/edit/head",
        type: "post",
        async: true,
        contentType: false,
        processData:false,
        dataType: "json",
        data: formData,
        success: function (response) {
            if (response.status == 1) {
                $("#leading-in-head-close-btn").click()
                getMe(contextPath,_csrf)
            } else {
                alert(response.describe)
            }
        }
    })
}

function changePassword() {
    var password = md5($("#change-password-input").val());
    defaultAjaxRequest({
        "_csrf": _csrf,
        "password": password
    },contextPath + "/api/user/edit/password")

}

function getMe(contextPath,_csrf) {
    $.ajax({
        url: contextPath + "/api/user/get",
        type: "post",
        async: false,
        dataType: "json",
        data: {"_csrf":_csrf},
        success: function (response) {
            if (response.status == 1) {
                $("#user-head").attr("src",contextPath + "/head/" + response.object.head)
                $("#user-name").text(response.object.name)
                user = response.object;
            }
        }
    })
}

function goToMonitor() {
    return user.role.productSelect == true;
}

function goToProduct() {
    return user.role.productSelect == true;
}

function goToType() {
    return user.role.typeSelect == true;
}

function goToMember() {
    return user.role.memberSelect == true;
}

function goToOrder() {
    return user.role.orderSelect == true;
}

function goToUser() {
    return user.role.userSelect == true;
}

function goToRole() {
    return user.role.roleSelect == true;
}

function dateFormat(fmt, date) {
    let ret;
    const opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        };
    };
    return fmt;
}

function defaultAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        data: data,
        success: function (response) {
            $("#change-password-close-btn").click()
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

function defaultFormDataAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        async: true,
        contentType: false,
        processData:false,
        dataType: "json",
        data: data,
        success: function (response) {
            $("#pay-close").click()
            alert(response.describe)
        }
    })
}

/**
 * springSecurity不支持使用json
 * */
function jsonAjaxRequest(data,url) {
    $.ajax({
        url: url,
        type: "post",
        data: JSON.stringify(data),
        success: function (response) {
            alert(response.describe)
        }
    })
}