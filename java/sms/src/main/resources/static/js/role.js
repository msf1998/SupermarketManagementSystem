let page = 0;
let order = "create_time";
let role = [];
let offset = 10;

function getRole() {
    if (canRoleSelect()) {
        $.ajax({
            url: contextPath + "/api/role/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order,
                "offset": offset
            },
            success: function (response) {
                if (response.status == 1) {
                    role = response.object;
                    $("#role-items").empty();
                    for (let i = 0; i < role.length; i++) {
                        $("#role-items").append("<tr class=\"" + "tr" + role[i].id + "\">\n" +
                            "                          <td><img src=\"" + contextPath + "/lib/advanced-datatable/images/details_open.png" + "\" onclick=\"getDetail('" + role[i].id + "')\"></td>\n" +
                            "                          <td>" + role[i].id + "</td>\n" +
                            "                          <td>" + role[i].name + "</td>\n" +
                            "                          <td>" + role[i].createTime.substr(0,10) + "</td>\n" +
                            "                          <td>" + role[i].parent.name + "</td>\n" +
                            "                          <td>\n" +
                            "                            <button class=\"btn btn-primary btn-xs\" onclick=\"editRole('" + role[i].id + "')\" data-toggle=\"modal\" data-target=\"#edit-add-role-modal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                            <button class=\"btn btn-danger btn-xs\" onclick=\"deleteRole('" + role[i].id + "')\"><i class=\"fa fa-trash-o \"></i></button>\n" +
                            "                          </td>\n" +
                            "                        </tr>")
                    }
                    if (role.length < 10) {
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

function getDetail(id) {
    let i;
    for (i = 0; i < role.length; i ++) {
        if (id == role[i].id) {
            break;
        }
    }
    let r = role[i]
    var img = $(".tr" + r.id +" td img").attr("src");
    if (img == contextPath + "/lib/advanced-datatable/images/details_open.png") {
        $(".tr" + r.id).after("<table class='table table-striped table-advance table-hover table" + r.id +"' cellpadding='0' cellspacing='0' border='0' class='display table table-bordered' id='hidden-table-info'>" +
            "<thead>" +
            "<tr>" +
            "<th><i class=‘fa’></i>商品添加</th>" +
            "<th><i class=‘fa’></i>商品删除</th>" +
            "<th><i class=‘fa’></i>商品修改</th>" +
            "<th><i class=‘fa’></i>商品查询</th>" +
            "<th><i class=‘fa’></i>类型添加</th>" +
            "<th><i class=‘fa’></i>类型删除</th>" +
            "<th><i class=‘fa’></i>类型修改</th>" +
            "<th><i class=‘fa’></i>类型查询</th>" +
            "<th><i class=‘fa’></i>会员添加</th>" +
            "<th><i class=‘fa’></i>会员删除</th>" +
            "<th><i class=‘fa’></i>会员修改</th>" +
            "<th><i class=‘fa’></i>会员查询</th>" +
            "<th><i class=‘fa’></i>订单添加</th>" +
            "<th><i class=‘fa’></i>订单删除</th>" +
            "<th><i class=‘fa’></i>订单修改</th>" +
            "<th><i class=‘fa’></i>订单查询</th>" +
            "<th><i class=‘fa’></i>员工添加</th>" +
            "<th><i class=‘fa’></i>员工删除</th>" +
            "<th><i class=‘fa’></i>员工修改</th>" +
            "<th><i class=‘fa’></i>员工查询</th>" +
            "<th><i class=‘fa’></i>角色添加</th>" +
            "<th><i class=‘fa’></i>角色删除</th>" +
            "<th><i class=‘fa’></i>角色修改</th>" +
            "<th><i class=‘fa’></i>角色查询</th>" +
            "</tr>" +
            "</thead>"+
            "<tbody>" +
            "<tr>" +
            "<td>" + (r.productInsert ? "允许":"拒绝") +"</td>" +
            "<td>" + (r.productDelete ? "允许":"拒绝") + "</td>" +
            "<td>"+ (r.productUpdate ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.productSelect ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.typeInsert ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.typeDelete ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.typeUpdate ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.typeSelect ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.memberInsert ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.memberDelete ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.memberUpdate ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.memberSelect ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.orderInsert ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.orderDelete ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.orderUpdate ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.orderSelect ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.userInsert ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.userDelete ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.userUpdate ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.userSelect ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.roleInsert ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.roleDelete ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.roleUpdate ? "允许":"拒绝") +"</td>" +
            "<td>"+ (r.roleSelect ? "允许":"拒绝") +"</td>" +
            "</tr>" +
            "</tbody>" +
            "</table>");
        $(".tr" + r.id +" td img").attr("src",contextPath + "/lib/advanced-datatable/images/details_close.png");
    } else {
        $(".table" + r.id).remove();
        $(".tr" + r.id +" td img").attr("src",contextPath + "/lib/advanced-datatable/images/details_open.png");
    }
}

function editRole(id) {
    let i;
    for (i = 0; i < role.length; i ++) {
        if (id == role[i].id) {
            break;
        }
    }
    $("#edit-add-role-modalLabel").text("修改角色信息")
    $("#edit-add-id-input").val(id)
    $("#edit-add-name-input").val(role[i].name)
    $("#edit-add-productInsert-input").attr("checked",role[i].productInsert)
    $("#edit-add-productDelete-input").attr("checked",role[i].productDelete)
    $("#edit-add-productUpdate-input").attr("checked",role[i].productUpdate)
    $("#edit-add-productSelect-input").attr("checked",role[i].productSelect)
    $("#edit-add-typeInsert-input").attr("checked",role[i].typeInsert)
    $("#edit-add-typeDelete-input").attr("checked",role[i].typeDelete)
    $("#edit-add-typeUpdate-input").attr("checked",role[i].typeUpdate)
    $("#edit-add-typeSelect-input").attr("checked",role[i].typeSelect)
    $("#edit-add-memberInsert-input").attr("checked",role[i].memberInsert)
    $("#edit-add-memberDelete-input").attr("checked",role[i].memberDelete)
    $("#edit-add-memberUpdate-input").attr("checked",role[i].memberUpdate)
    $("#edit-add-memberSelect-input").attr("checked",role[i].memberSelect)
    $("#edit-add-orderInsert-input").attr("checked",role[i].orderInsert)
    $("#edit-add-orderDelete-input").attr("checked",role[i].orderDelete)
    $("#edit-add-orderUpdate-input").attr("checked",role[i].orderUpdate)
    $("#edit-add-orderSelect-input").attr("checked",role[i].orderSelect)
    $("#edit-add-userInsert-input").attr("checked",role[i].userInsert)
    $("#edit-add-userDelete-input").attr("checked",role[i].userDelete)
    $("#edit-add-userUpdate-input").attr("checked",role[i].userUpdate)
    $("#edit-add-userSelect-input").attr("checked",role[i].userSelect)
    $("#edit-add-roleInsert-input").attr("checked",role[i].roleInsert)
    $("#edit-add-roleDelete-input").attr("checked",role[i].roleDelete)
    $("#edit-add-roleUpdate-input").attr("checked",role[i].roleUpdate)
    $("#edit-add-roleSelect-input").attr("checked",role[i].roleSelect)
    $("#edit-add-role-btn").attr("disabled",true)
}

function addRole() {
    $("#edit-add-role-modalLabel").text("添加角色")
    $("#edit-add-name-input").val("")
    $("#edit-add-productInsert-input").attr("checked",false)
    $("#edit-add-productDelete-input").attr("checked",false)
    $("#edit-add-productUpdate-input").attr("checked",false)
    $("#edit-add-productSelect-input").attr("checked",false)
    $("#edit-add-typeInsert-input").attr("checked",false)
    $("#edit-add-typeDelete-input").attr("checked",false)
    $("#edit-add-typeUpdate-input").attr("checked",false)
    $("#edit-add-typeSelect-input").attr("checked",false)
    $("#edit-add-memberInsert-input").attr("checked",false)
    $("#edit-add-memberDelete-input").attr("checked",false)
    $("#edit-add-memberUpdate-input").attr("checked",false)
    $("#edit-add-memberSelect-input").attr("checked",false)
    $("#edit-add-orderInsert-input").attr("checked",false)
    $("#edit-add-orderDelete-input").attr("checked",false)
    $("#edit-add-orderUpdate-input").attr("checked",false)
    $("#edit-add-orderSelect-input").attr("checked",false)
    $("#edit-add-userInsert-input").attr("checked",false)
    $("#edit-add-userDelete-input").attr("checked",false)
    $("#edit-add-userUpdate-input").attr("checked",false)
    $("#edit-add-userSelect-input").attr("checked",false)
    $("#edit-add-roleInsert-input").attr("checked",false)
    $("#edit-add-roleDelete-input").attr("checked",false)
    $("#edit-add-roleUpdate-input").attr("checked",false)
    $("#edit-add-roleSelect-input").attr("checked",false)
    $("#edit-add-role-btn").attr("disabled",true)
}

function enableSubmit() {
    $("#edit-add-role-btn").attr("disabled", false)
}

function submitRole() {
    let title = $("#edit-add-role-modalLabel").text();
    let id = $("#edit-add-id-input").val();
    let name = $("#edit-add-name-input").val();
    let url;
    let formData = new FormData();
    formData.append("_csrf",_csrf)
    if (title == "修改角色信息") {
        if (!canRoleUpdate()) {
            alert("抱歉，您无此权限")
            return;
        }
        url = contextPath + "/api/role/edit"
        if (id == null || id == "" || id == undefined || id < 0) {
            alert("角色id不合法");
            return;
        }
        formData.append("id",id);
    } else if (title == "添加角色") {
        if (!canRoleInsert()) {
            alert("抱歉，您无此权限")
            return;
        }
        url = contextPath + "/api/role/add"
    } else {
        alert("未知操作")
        return;
    }
    if (name != null && name != "" && name != undefined && name.length < 32) {
        formData.append("name",name)
        formData.append("productInsert",$("#edit-add-productInsert-input")[0].checked)
        formData.append("productDelete",$("#edit-add-productDelete-input")[0].checked)
        formData.append("productUpdate",$("#edit-add-productUpdate-input")[0].checked)
        formData.append("productSelect",$("#edit-add-productSelect-input")[0].checked)
        formData.append("typeInsert",$("#edit-add-typeInsert-input")[0].checked)
        formData.append("typeDelete",$("#edit-add-typeDelete-input")[0].checked)
        formData.append("typeUpdate",$("#edit-add-typeUpdate-input")[0].checked)
        formData.append("typeSelect",$("#edit-add-typeSelect-input")[0].checked)
        formData.append("memberInsert",$("#edit-add-memberInsert-input")[0].checked)
        formData.append("memberDelete",$("#edit-add-memberDelete-input")[0].checked)
        formData.append("memberUpdate",$("#edit-add-memberUpdate-input")[0].checked)
        formData.append("memberSelect",$("#edit-add-memberSelect-input")[0].checked)
        formData.append("orderInsert",$("#edit-add-orderInsert-input")[0].checked)
        formData.append("orderDelete",$("#edit-add-orderDelete-input")[0].checked)
        formData.append("orderUpdate",$("#edit-add-orderUpdate-input")[0].checked)
        formData.append("orderSelect",$("#edit-add-orderSelect-input")[0].checked)
        formData.append("userInsert",$("#edit-add-userInsert-input")[0].checked)
        formData.append("userDelete",$("#edit-add-userDelete-input")[0].checked)
        formData.append("userUpdate",$("#edit-add-userUpdate-input")[0].checked)
        formData.append("userSelect",$("#edit-add-userSelect-input")[0].checked)
        formData.append("roleInsert",$("#edit-add-roleInsert-input")[0].checked)
        formData.append("roleDelete",$("#edit-add-roleDelete-input")[0].checked)
        formData.append("roleUpdate",$("#edit-add-roleUpdate-input")[0].checked)
        formData.append("roleSelect",$("#edit-add-roleSelect-input")[0].checked)
        $.ajax({
            url: url,
            type: "post",
            contentType: false,
            processData: false,
            data: formData,
            success: function (response) {
                if (response.status == 1) {
                    $("#edit-add-role-close-btn").click()
                    getRole()
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("角色名称不合法")
    }
}

function deleteRole(id) {
    if (canRoleDelete()) {
        let i;
        for (i = 0; i < role.length; i ++) {
            if (id == role[i].id) {
                break;
            }
        }
        if (confirm("确认删除" + role[i].name +"?")) {
            $.ajax({
                url: contextPath + "/api/role/delete",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "id": id
                },
                success: function (response) {
                    if (response.status == 1) {
                        getRole()
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
    getRole();
}

function beforePage() {
    page--;
    getRole();
}


function canRoleInsert() {
    return user.role.roleInsert == true;
}

function canRoleDelete() {
    return user.role.roleDelete == true;
}

function canRoleUpdate() {
    return user.role.roleUpdate == true;
}

function canRoleSelect() {
    return user.role.roleSelect = true;
}
