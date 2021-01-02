let page = 0;
let order = "id";
let member = [];

function getMember() {
    if (canMemberSelect()) {
        $.ajax({
            url: contextPath + "/api/member/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order
            },
            success: function (response) {
                if (response.status == 1) {
                    member = response.object;
                    $("#member-items").empty();
                    for (let i = 0; i < member.length; i++) {
                        $("#member-items").append("<tr>\n" +
                            "                                <td>" + member[i].id + "</td>\n" +
                            "                                <td>" + member[i].name + "</td>\n" +
                            "                                <td>" + member[i].phone + "</td>\n" +
                            "                                <td>" + member[i].idNumber + "</td>\n" +
                            "                                <td>" + member[i].score + "</td>\n" +
                            "                                <td>" + member[i].createTime.substr(0, 10) + "</td>\n" +
                            "                                <td>" + member[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-primary btn-xs\" onclick=\"editMember('" + member[i].id + "')\" data-toggle=\"modal\"\n" +
                            "                                            data-target=\"#edit-add-member-modal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                                    <button class=\"btn btn-danger btn-xs\" onclick=\"deleteMember('" + member[i].id + "')\"><i\n" +
                            "                                            class=\"fa fa-trash-o \"></i></button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }
                    if (member.length < 10) {
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

function editMember(id) {
    let i;
    for (i = 0; i < member.length; i++) {
        if (id == member[i].id) {
            break;
        }
    }
    let m = member[i];
    $("#edit-add-member-modalLabel").text("修改会员信息")
    $("#edit-add-id-input").val(m.id)
    $("#edit-add-name-input").val(m.name)
    $("#edit-add-phone-input").val(m.phone)
    $("#edit-add-idNumber-input").attr("type", "hidden")
    $("#edit-add-member-btn").attr("disabled",true)
}

function addMember() {
    $("#edit-add-member-modalLabel").text("添加会员")
    $("#edit-add-id-input").val("")
    $("#edit-add-name-input").val("")
    $("#edit-add-phone-input").val("")
    $("#edit-add-idNumber-input").attr("hidden", false)
    $("#edit-add-member-btn").attr("disabled",true)
}

function enableSubmit() {
    $("#edit-add-member-btn").attr("disabled", false)
}

function submitMember() {
    let title = $("#edit-add-member-modalLabel").text();
    let id = $("#edit-add-id-input").val();
    let name = $("#edit-add-name-input").val();
    let phone = $("#edit-add-phone-input").val();
    let idNumber = $("#edit-add-idNumber-input").val();
    let url;
    let formData = new FormData();
    formData.append("_csrf",_csrf)
    if (name != null && name.length <= 4) {
        if (/^[0-9]{11}$/.test(phone)) {
            formData.append("name", name)
            formData.append("phone", phone)
            if (title == "修改会员信息") {
                if(!canMemberUpdate()) {
                    alert("抱歉，您无此权限")
                    return;
                }
                url = contextPath + "/api/member/edit"
                formData.append("id",id)
            } else if (title == "添加会员") {
                if(!canMemberInsert()) {
                    alert("抱歉，您无此权限")
                    return;
                }
                url = contextPath + "/api/member/add"
                if (/^[0-9]{17}([0-9]|x)$/.test(idNumber)) {
                    formData.append("idNumber",idNumber)
                } else {
                    alert("身份证号不合法")
                    return;
                }
            } else {
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
                        $("#edit-add-member-close-btn").click()
                        getMember()
                    } else {
                        alert(response.describe)
                    }
                }
            })
        } else {
            alert("手机号不合法")
        }
    } else {
        alert("姓名不合法")
    }
}

function deleteMember(id) {
    if (canMemberDelete()) {
        let i;
        for (i = 0; i < member.length; i ++) {
            if (id == member[i].id) {
                break;
            }
        }
        if (confirm("确认删除" + member[i].name +"?")) {
            $.ajax({
                url: contextPath + "/api/member/delete",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "id": id
                },
                success: function (response) {
                    if (response.status == 1) {
                        getMember()
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
    getMember();
}

function beforePage() {
    page--;
    getMember();
}

function canMemberSelect() {
    return user.role.memberSelect == true;
}

function canMemberInsert() {
    return user.role.memberInsert == true;
}

function canMemberDelete() {
    return user.role.memberDelete == true;
}

function canMemberUpdate() {
    return user.role.memberUpdate == true;
}