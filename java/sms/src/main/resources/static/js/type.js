let page = 0;
let order = "id";
let type = [];

function getType() {
    if (canTypeSelect()) {
        $.ajax({
            url: contextPath + "/api/type/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order
            },
            success: function (response) {
                if (response.status == 1) {
                    $("#type-items").empty()
                    type = response.object;
                    for (let i = 0; i < type.length; i ++) {
                        $("#type-items").append("<tr>\n" +
                            "                                <td>" + type[i].id + "</td>\n" +
                            "                                <td>" + type[i].name + "</td>\n" +
                            "                                <td>" + type[i].createTime.substr(0,10) + "</td>\n" +
                            "                                <td>" + type[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-primary btn-xs\" onclick=\"editType('" + type[i].id + "')\" data-toggle=\"modal\"\n" +
                            "                                            data-target=\"#edit-add-type-Modal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                                    <button class=\"btn btn-danger btn-xs\" onclick=\"deleteType('" + type[i].id + "')\"><i\n" +
                            "                                            class=\"fa fa-trash-o \"></i></button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }

                    if (type.length < 10) {
                        $("#next-page-btn").attr("hidden",true)
                    } else {
                        $("#next-page-btn").attr("hidden",false)
                    }
                    if (page <= 0) {
                        $("#before-page-btn").attr("hidden",true)
                    } else {
                        $("#before-page-btn").attr("hidden",false)
                    }
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("抱歉，您无此权限")
    }

}

function editType(id) {
    let i;
    for (i = 0; i < type.length; i ++) {
        if (type[i].id == id) {
            break;
        }
    }
    let t = type[i];
    $("#edit-add-type-ModalLabel").text("修改类型")
    $("#edit-add-id-input").val(t.id)
    $("#edit-add-name-input").val(t.name)

}

function enableSubmit() {
    $("#edit-add-type-btn").attr("disabled",false);
}

function submitType() {
    let id = $("#edit-add-id-input").val();
    let name = $("#edit-add-name-input").val();
    let formData = new FormData();
    let url = contextPath + "/api/type/add"
    if (id != null && id != "" && id != undefined) {
        formData.append("id",id);
        url = contextPath + "/api/type/edit"
    }
    formData.append("name",name);
    formData.append("_csrf",_csrf)
    if (url.endsWith("add") && !canTypeInsert()) {
        alert("抱歉，您无此权限")
        return;
    }
    if(url.endsWith("edit") && !canTypeUpdate()) {
        alert("抱歉，您无此权限")
        return;
    }
    $.ajax({
        url: url,
        type: "post",
        contentType: false,
        processData: false,
        data: formData,
        success: function (response) {
            if (response.status == 1) {
                $("#edit-add-type-close-btn").click()
                getType()
            } else {
                alert(response.describe)
            }
        }
    })
}

function addType() {
    $("#edit-add-type-ModalLabel").text("创建类型")
    $("#edit-add-id-input").val("")
    $("#edit-add-name-input").val("")
}

function deleteType(id) {
    if (canTypeDelete()) {
        let i;
        for (i = 0 ;i < type.length; i ++) {
            if (id == type[i].id) {
                break;
            }
        }
        if (confirm("确认删除" + type[i].name + "类型？")) {
            $.ajax({
                url: contextPath + "/api/type/delete",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "id": id
                },
                success: function (response) {
                    if (response.status == 1) {
                        getType()
                    } else{
                        alert(response.describe)
                    }
                }
            })
        }
    } else {
        alert("抱歉，您无此权限")
    }
}

function nextPage() {
    page ++;
    getType();
}

function beforePage() {
    page --;
    getType();
}



function canTypeSelect() {
    return user.role.typeSelect == true;
}

function canTypeInsert() {
    return user.role.typeInsert == true;
}

function canTypeDelete() {
    return user.role.typeDelete == true;
}

function canTypeUpdate() {
    return user.role.typeUpdate == true;
}