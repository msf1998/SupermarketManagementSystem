let bad = []
let less = []
let product = {}
function getWillGoBad() {
    if (canSelectProduct()) {
        $("#bad-table").empty()
        $.ajax({
            url: contextPath + "/api/product/list/bad",
            type: "post",
            data: {"_csrf": _csrf},
            success: function (response) {
                if (response.status == 1) {
                    let o = response.object;
                    bad = o;
                    for (let i = 0; i < o.length; i ++) {
                        $("#bad-table").append("<tr>\n" +
                            "                                <td>" + o[i].id + "</td>\n" +
                            "                                <td><img src='" + contextPath + "/product/" + o[i].photo + "' style='width: 30px;height: 30px;'></td>\n" +
                            "                                <td>" + o[i].name + "</td>\n" +
                            "                                <td>" + o[i].type.name + "</td>\n" +
                            "                                <td>" + o[i].manufacturer + "</td>\n" +
                            "                                <td>" + o[i].productDate.substr(0,10) + "</td>\n" +
                            "                                <td>" + o[i].selfLife + "</td>\n" +
                            "                                <td>" + o[i].warnBefore + "</td>\n" +
                            "                                <td>" + o[i].count + "</td>\n" +
                            "                                <td>" + o[i].warnCount + "</td>\n" +
                            "                                <td>" + o[i].inTime.substr(0,10) + "</td>\n" +
                            "                                <td>" + o[i].inPrice + "</td>\n" +
                            "                                <td>" + o[i].outPrice + "</td>\n" +
                            "                                <td>" + o[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class='btn btn-danger btn-xs' data-toggle='modal' data-target='#myModal' onclick=\"editProduct('" + o[i].id + "')\">\n" +
                            "                                        <i class='fa'></i>促销\n" +
                            "                                    </button>\n" +
                            "                                    <button class='btn btn-danger btn-xs' onclick=\"deleteProduct('" + o[i].id + "')\" >\n" +
                            "                                        <i class='fa'></i>下架\n" +
                            "                                    </button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("抱歉，您没有该权限")
    }
}

function getLessProduct() {
    if (canSelectProduct()) {
        $("#less-table").empty()
        $.ajax({
            url: contextPath + "/api/product/list/less",
            type: "post",
            data: {"_csrf":_csrf},
            success: function (response) {
                if (response.status == 1) {
                    let o = response.object;
                    less = o;
                    for (let i = 0; i< o.length; i ++) {
                        $("#less-table").append("<tr>\n" +
                            "                                <td>" + o[i].id + "</td>\n" +
                            "                                <td><img src='" + contextPath + "/product/" + o[i].photo + "' style='width: 30px;height: 30px;'></td>\n" +
                            "                                <td>" + o[i].name + "</td>\n" +
                            "                                <td>" + o[i].type.name + "</td>\n" +
                            "                                <td>" + o[i].manufacturer + "</td>\n" +
                            "                                <td>" + o[i].productDate.substr(0,10) + "</td>\n" +
                            "                                <td>" + o[i].selfLife + "</td>\n" +
                            "                                <td>" + o[i].warnBefore + "</td>\n" +
                            "                                <td>" + o[i].count + "</td>\n" +
                            "                                <td>" + o[i].warnCount + "</td>\n" +
                            "                                <td>" + o[i].inTime.substr(0,10) + "</td>\n" +
                            "                                <td>" + o[i].inPrice + "</td>\n" +
                            "                                <td>" + o[i].outPrice + "</td>\n" +
                            "                                <td>" + o[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class='btn btn-danger btn-xs' >\n" +
                            "                                        <i class='fa fa-trash-o' onclick=\"deletedLessProduct('" + o[i].id + "')\"></i>\n" +
                            "                                    </button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("抱歉，您没有该权限")
    }
}

function deletedLessProduct(id) {
    if (canUpdateProduct()) {
        $.ajax({
            url: contextPath + "/api/product/edit",
            type: "post",
            data: {
                "_csrf": _csrf,
                "id": id.toString(),
                "typeId": 3
            },
            success: function (response) {
                if (response.status == 1) {
                    getLessProduct()
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("抱歉，您没有该权限")
    }
}

function editProduct(id) {
    console.log(id)
    for (let i = 0; i < bad.length; i ++) {
        if (bad[i].id == id) {
            product = bad[i]
            break;
        }
    }
    console.log(product)
    $("#name-input").val(product.name)
    $("#selfLife-input").val(product.selfLife)
    $("#warnBefore-input").val(product.warnBefore)
    $("#inPrice-input").val(product.inPrice)
    $("#outPrice-input").val(product.outPrice)
}

function submitEdit() {
    let name = $("#name-input").val();
    let selfLife = $("#selfLife-input").val();
    let warnBefore = $("#warnBefore-input").val();
    let inPrice = $("#inPrice-input").val();
    let outPrice = $("#outPrice-input").val();
    $.ajax({
        url: contextPath + "/api/product/edit/bad",
        type: "post",
        data: {
            "_csrf": _csrf,
            "id": product.id,
            "name": name,
            "selfLife": selfLife,
            "warnBefore": warnBefore,
            "inPrice": inPrice,
            "outPrice": outPrice
        },
        success: function (response) {
            if (response.status == 1) {
                getWillGoBad()
            } else {
                alert(response.describe)
            }
        }
    })
}

function deleteProduct(id) {
    $.ajax({
        url: contextPath + "/api/product/delete",
        type: "post",
        data: {
            "_csrf": _csrf,
            "id": id
        },
        success: function (response) {
            if (response.status == 1) {
                getWillGoBad();
            } else {
                alert(response.describe)
            }
        }
    })
}

function canUpdateProduct() {
    return user.role.productUpdate == true;
}

function canSelectProduct() {
    return user.role.productSelect == true;
}

function canDeleteProduct() {
    return user.role.productDelete == true;
}

