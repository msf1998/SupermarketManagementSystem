function checkFile(file) {
    var pattern = /^.*\.png$|^.*\.jpg$/
    if (pattern.test(file) != true) {
        $("#leading-in-head-btn").disabled
        alert("仅支持.jpg和.png格式的图片");
    } else {
        $("#leading-in-head-btn").attr("disabled",false)
    }
}

function editHead() {
    var head = $("#leading-in-head-input")[0].files[0];
    var formData = new FormData();
    formData.append("head",head);
    formData.append("_csrf",_csrf);
    defaultAjaxRequest(formData,contextPath + "/api/user/edit/head")
}