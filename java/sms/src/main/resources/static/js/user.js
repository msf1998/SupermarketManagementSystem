let page = 0;
let order = "create_time";
let offset = 10;
let users = [];
let role = [];

function getUser() {
    if (canUserSelect()) {
        $.ajax({
            url: contextPath + "/api/user/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order,
                "offset": offset
            },
            success: function (response) {
                if (response.status == 1) {
                    users = response.object;
                    $("#user-items").empty();
                    for (let i = 0; i < users.length; i++) {
                        $("#user-items").append("<tr >\n" +
                            "                                <td><img src=\"" + contextPath + "/head/" + users[i].head + "\"\n" +
                            "                                         style=\"height: 30px; width: 30px;\"></td>\n" +
                            "                                <td>" + users[i].username + "</td>\n" +
                            "                                <td>" + users[i].name + "</td>\n" +
                            "                                <td>" + users[i].role.name + "</td>\n" +
                            "                                <td>" + users[i].createTime.substr(0,10) + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-primary btn-xs\" onclick=\"editUser('" + users[i].id + "')\" data-toggle=\"modal\"\n" +
                            "                                            data-target=\"#edit-add-user-modal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                                    <button class=\"btn btn-danger btn-xs\" onclick=\"deleteUser('" + users[i].id + "')\"><i\n" +
                            "                                            class=\"fa fa-trash-o \"></i></button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }
                    if (users.length < 10) {
                        $("#next-page-btn").attr("hidden", true)
                    } else {
                        $("#next-page-btn").attr("hidden", false)
                    }
                    if (page <= 0) {
                        $("#before-page-btn").attr("hidden", true)
                    } else {
                        $("#before-page-btn").attr("hidden", false)
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

function getRole() {
    if (canRoleSelect()) {
        $.ajax({
            url: contextPath + "/api/role/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "order": order
            },
            success: function (response) {
                if (response.status == 1) {
                    role = response.object
                    for (let i = 0; i < role.length; i ++) {
                        $("#edit-add-role-select").append("<option id='edit-add-user-" + role[i].id + "' value=\"" + role[i].id + "\">" + role[i].name + "</option>")
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

function editUser(id) {
    let i;
    for (i = 0;i < users.length; i ++) {
        if (id == users[i].id) {
            break;
        }
    }
    $("#edit-add-user-modalLabel").text("修改员工信息")
    $("#edit-add-id-input").val(id)
    $("#edit-add-email-input").attr("readonly",true)
    $("#edit-add-email-input").val(users[i].username)
    $("#edit-add-password-input").attr("readonly",true)
    $("#edit-add-name-input").val(users[i].name)
    $("#edit-add-user-" + users[i].id).attr("selected",true)
    $("#edit-add-user-btn").attr("disabled",true)
}

function addUser() {
    $("#edit-add-user-modalLabel").text("添加员工")
    $("#edit-add-id-input").val("")
    $("#edit-add-email-input").attr("readonly",false)
    $("#edit-add-email-input").val("")
    $("#edit-add-password-input").attr("readonly",false)
    $("#edit-add-password-input").val("")
    $("#edit-add-name-input").val("")
    $("#edit-add-user-btn").attr("disabled",true)
}

function enableSubmit() {
    $("#edit-add-user-btn").attr("disabled", false)
}

function submitUser() {
    let title = $("#edit-add-user-modalLabel").text();
    let id = $("#edit-add-id-input").val();
    let email = $("#edit-add-email-input").val();
    let password = $("#edit-add-password-input").val();
    let name = $("#edit-add-name-input").val();
    let role = $("#edit-add-role-select").val();
    let formData = new FormData();
    formData.append("_csrf",_csrf)
    let url;
    if (name != null && name != "" && name != undefined && name.length <= 4) {
        formData.append("name",name);
        if (role != null && role != undefined && role != "" && role > 0) {
            formData.append("roleId",role)
            if (title == "添加员工") {
                if (canUserInsert()) {
                    if (email != null && /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/.test(email) && email.length <= 32) {
                        formData.append("username",email)
                        if (password == null || password == undefined || password == "") {
                            password = "123"
                        }
                        formData.append("password",md5(password));
                        url = contextPath + "/api/user/add"
                    } else {
                        alert("邮箱不合法")
                        return;
                    }
                } else {
                    alert("抱歉，您无此权限")
                    return;
                }
            } else if (title == "修改员工信息") {
                if (canUserUpdate()) {
                    if (id != null && id != undefined && id != "" && id > 0) {
                        formData.append("id",id)
                        url = contextPath + "/api/user/edit"
                    } else {
                        alert("id不合法")
                        return;
                    }
                } else {
                    alert("抱歉，您无此权限")
                    return;
                }
            } else {
                alert("未知操作")
            }
            $.ajax({
                url: url,
                type: "post",
                contentType: false,
                processData: false,
                data: formData,
                success: function (response) {
                    if (response.status == 1) {
                        $("#edit-add-user-close-btn").click()
                        getUser()
                    } else {
                        alert(response.describe)
                    }
                }
            })
        } else {
            alert("角色不合法")
        }
    } else {
        alert("姓名不合法")
    }
}

function deleteUser(id) {
    if (canUserDelete()) {
        let i;
        for (i = 0; i < users.length; i ++) {
            if (id == users[i].id) {
                break;
            }
        }
        if (confirm("确认删除" + users[i].name +"?")) {
            $.ajax({
                url: contextPath + "/api/user/delete",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "id": id
                },
                success: function (response) {
                    if (response.status == 1) {
                        getUser()
                    } else {
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
    page++;
    getUser();
}

function beforePage() {
    page--;
    getUser();
}

function canUserSelect() {
    return user.role.userSelect == true;
}

function canUserInsert() {
    return user.role.userInsert == true;
}

function canUserDelete() {
    return user.role.userDelete == true;
}

function canUserUpdate() {
    return user.role.userUpdate == true;
}

function canRoleSelect() {
    return user.role.roleSelect = true;
}