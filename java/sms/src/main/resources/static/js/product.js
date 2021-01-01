let page = 0;
let order = "name";
let product = [];
let type = [];
function getProduct() {
    if (canProductSelect()) {
        let type = $("#type-select").val();
        $.ajax({
            url: contextPath + "/api/product/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order,
                "typeId": type
            },
            success: function (response) {
                if (response.status == 1) {
                    $("#product-items").empty()
                    product = response.object
                    for (let i = 0; i < product.length; i ++) {
                        $("#product-items").append("<tr>\n" +
                            "                                <td>" + product[i].id + "</td>\n" +
                            "                                <td><img src='" + contextPath + "/product/" + product[i].photo + "' style=\"width: 30px;height: 30px;\"\n" +
                            "                                         data-toggle=\"modal\"\n" +
                            "                                         data-target=\"#edit-productPhoto-modal\" onclick=\"editProductPhoto('" + product[i].id +"')\"></td>\n" +
                            "                                <td>" + product[i].name + "</td>\n" +
                            "                                <td>" + product[i].type.name + "</td>\n" +
                            "                                <td>" + product[i].manufacturer + "</td>\n" +
                            "                                <td>" + product[i].productDate.substr(0,10) + "</td>\n" +
                            "                                <td>" + product[i].selfLife + "</td>\n" +
                            "                                <td>" + product[i].warnBefore + "</td>\n" +
                            "                                <td>" + product[i].count + "</td>\n" +
                            "                                <td>" + product[i].warnCount + "</td>\n" +
                            "                                <td>" + product[i].inTime.substr(0,10) + "</td>\n" +
                            "                                <td>" + product[i].inPrice + "</td>\n" +
                            "                                <td>" + product[i].outPrice + "</td>\n" +
                            "                                <td>" + product[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-default btn-xs\">\n" +
                            "                                       <form method='post' action='" +contextPath + "/api/product/qcode/download" + "'>\n" +
                            "                                           <input type='hidden' name='_csrf' value='" + _csrf + "'>\n" +
                            "                                           <input type='hidden' name='name' value='" + product[i].qcode + "'/>\n" +
                            "                                            <input type='submit' value='打印'>" +
                            "                                       </form>" +
                            "                                    </button>\n" +
                            "                                </td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-primary btn-xs\" onclick=\"return editProduct('" + product[i].id + "');\" data-toggle=\"modal\"\n" +
                            "                                            data-target=\"#edit-product-modal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                                    <button class=\"btn btn-danger btn-xs\" onclick=\"return deleteProduct('" + product[i].id + "');\"><i\n" +
                            "                                            class=\"fa fa-trash-o \"></i></button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }
                    if (product.length < 10) {
                        $("#nextPage-btn").attr("hidden",true)
                    } else {
                        $("#nextPage-btn").attr("hidden",false)
                    }
                    if (page <= 0) {
                        $("#beforePage-btn").attr("hidden",true)
                    } else {
                        $("#beforePage-btn").attr("hidden",false)
                    }
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("抱歉，您没有此权限")
    }
}

function getType() {
    if (canTypeSelect()) {
        $.ajax({
            url: contextPath + "/api/type/list",
            type: "post",
            data: {
                "_csrf": _csrf
            },
            success: function (response) {
                if (response.status == 1) {
                    type = response.object;
                    for (let i = 0; i < type.length; i ++) {
                        $("#type-select").append("<option value=\"" + type[i].id + "\">" + type[i].name + "</option>")
                        $("#edit-type-select").append("<option id='edit-type-" + type[i].id + "' value=\"" + type[i].id + "\">" + type[i].name + "</option>")
                        $("#add-type-select").append("<option id='edit-type-" + type[i].id + "' value=\"" + type[i].id + "\">" + type[i].name + "</option>")
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

function editProduct(id) {
    if (canProductUpdate()) {
        let i;
        for ( i = 0 ;i < product.length; i ++) {
            if (product[i].id == id) {
                break;
            }
        }
        let p = product[i];
        $("#edit-name-input").val(p.name)
        $("#edit-id-input").val(p.id)
        $("#edit-type-" + p.typeId).attr("selected",true)
        $("#edit-manufacturer-input").val(p.manufacturer)
        $("#edit-productDate-input").val(p.productDate.substr(0,10))
        $("#edit-selfLife-input").val(p.selfLife)
        $("#edit-warnBefore-input").val(p.warnBefore)
        $("#edit-count-input").val(p.count)
        $("#edit-warnCount-input").val(p.warnCount)
        $("#edit-inPrice-input").val(p.inPrice)
        $("#edit-outPrice-input").val(p.outPrice)
        return true;
    } else {
        alert("抱歉，您无此权限")
        return false;
    }
}

function enableSubmit() {
    $("#edit-product-btn").attr("disabled",false)
}

function submitEdit() {
    if (canProductUpdate()) {
        let id = $("#edit-id-input").val();
        let name = $("#edit-name-input").val();
        let typeId = $("#edit-type-select").val();
        let manufacturer = $("#edit-manufacturer-input").val();
        let productDate = $("#edit-productDate-input").val();
        let selfLife = $("#edit-selfLife-input").val();
        let warnBefore = $("#edit-warnBefore-input").val();
        let count = $("#edit-count-input").val();
        let warnCount = $("#edit-warnCount-input").val();
        let inPrice = $("#edit-inPrice-input").val();
        let outPrice = $("#edit-outPrice-input").val();
        if (id != null && id != '' && id != undefined) {
            if (name != null && name != '' && name != undefined ) {
                if (/^[0-9]+$/.test(typeId)) {
                    if(manufacturer != null && manufacturer != '' && manufacturer != undefined) {
                        if (productDate != null && productDate != '' && productDate != undefined) {
                            if (/^[0-9]+$/.test(selfLife) || selfLife == -1) {
                                if (/^[0-9]+$/.test(warnBefore)) {
                                    if (/^[0-9]+$/.test(count)) {
                                        if (/^[0-9]+$/.test(warnCount)) {
                                            if (/^[0-9]+(\.[0-9]{1,2})?$/.test(inPrice)) {
                                                if (/^[0-9]+(\.[0-9]{1,2})?$/.test(outPrice)) {
                                                    $.ajax({
                                                        url: contextPath + "/api/product/edit",
                                                        type: "post",
                                                        data: {
                                                            "_csrf": _csrf,
                                                            "id": id,
                                                            "name": name,
                                                            "typeId": typeId,
                                                            "manufacturer": manufacturer,
                                                            "productDate": productDate,
                                                            "selfLife": selfLife,
                                                            "warnBefore": warnBefore,
                                                            "count": count,
                                                            "warnCount": warnCount,
                                                            "inPrice": inPrice,
                                                            "outPrice": outPrice
                                                        },
                                                        success: function (response) {
                                                            if (response.status == 1) {
                                                                $("#edit-product-modal-btn").click()
                                                                getProduct()
                                                            } else {
                                                                alert(response.describe)
                                                            }
                                                        }
                                                    })
                                                } else {
                                                    alert("售价不合法")
                                                }
                                            } else {
                                                alert("进价不合法")
                                            }
                                        } else {
                                            alert("最小库存不合法")
                                        }
                                    } else {
                                        alert("库存数量不合法")
                                    }
                                } else {
                                    alert("提醒值不合法")
                                }
                            } else {
                                alert("保质期不合法")
                            }
                        } else {
                            alert("生产日期不合法")
                        }
                    } else {
                        alert("供货商不合法")
                    }
                } else {
                    alert("类型不合法")
                }
            } else {
                alert("名称不合法")
            }
        } else {
            alert("id不合法")
        }
    } else {
        alert("抱歉,您无此权限")
    }
}

function deleteProduct(id) {
    if(canProductDelete()) {
        let i;
        for (i = 0; i < product.length; i ++) {
            if (id == product[i].id) {
                break;
            }
        }
        let p = product[i];
        let b = confirm("确定删除" + p.name);
        if (b) {
            $.ajax({
                url: contextPath + "/api/product/delete",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "id": id
                },
                success: function (response) {
                    if (response.status == 1) {
                        getProduct();
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

function editProductPhoto(id) {
    $("#edit-productPhoto-id-input").val(id)
}

function submitEditPhoto() {
    let file = $("#edit-productPhoto-input")[0].files[0];
    let id = $("#edit-productPhoto-id-input").val()
    let formData = new FormData();
    formData.append("file",file);
    formData.append("_csrf",_csrf);
    formData.append("id",id)
    $.ajax({
        url: contextPath + "/api/product/edit/photo",
        contentType: false,
        processData: false,
        data: formData,
        type: "post",
        success: function (response) {
            if (response.status == 1) {
                $("#edit-productPhoto-close-btn").click()
                getProduct()
            } else {
                alert(response.describe)
            }
        }
    })
}

function checkFilePhoto(file) {
    var pattern = /^.*\.png$|^.*\.jpg$/
    if (pattern.test(file) != true) {
        $("#edit-productPhoto-btn").attr("disabled",true)
        alert("仅支持.jpg和.png格式的图片");
    } else {
        $("#edit-productPhoto-btn").attr("disabled",false)
    }
}

function getProductByType() {
    if (canProductSelect()) {
        page = 0;
        getProduct()
    } else {
        alert("抱歉，您没有此权限")
    }
}

function nextPage() {
        page ++;
        getProduct();
}

function beforePage() {
        page --;
        getProduct();
}

function submitAdd() {
    if (canProductInsert()) {
        let id = $("#add-id-input").val();
        let name = $("#add-name-input").val();
        let typeId = $("#add-type-select").val();
        let manufacturer = $("#add-manufacturer-input").val();
        let productDate = $("#add-productDate-input").val();
        let selfLife = $("#add-selfLife-input").val();
        let warnBefore = $("#add-warnBefore-input").val();
        let count = $("#add-count-input").val();
        let warnCount = $("#add-warnCount-input").val();
        let inPrice = $("#add-inPrice-input").val();
        let outPrice = $("#add-outPrice-input").val();
        if (id != null && id != '' && id != undefined) {
            if (name != null && name != '' && name != undefined ) {
                if (/^[0-9]+$/.test(typeId)) {
                    if(manufacturer != null && manufacturer != '' && manufacturer != undefined) {
                        if (productDate != null && productDate != '' && productDate != undefined) {
                            if (/^[0-9]+$/.test(selfLife) || selfLife == -1) {
                                if (/^[0-9]+$/.test(warnBefore)) {
                                    if (/^[0-9]+$/.test(count)) {
                                        if (/^[0-9]+$/.test(warnCount)) {
                                            if (/^[0-9]+(\.[0-9]{1,2})?$/.test(inPrice)) {
                                                if (/^[0-9]+(\.[0-9]{1,2})?$/.test(outPrice)) {
                                                    $.ajax({
                                                        url: contextPath + "/api/product/add",
                                                        type: "post",
                                                        data: {
                                                            "_csrf": _csrf,
                                                            "id": id,
                                                            "name": name,
                                                            "typeId": typeId,
                                                            "manufacturer": manufacturer,
                                                            "productDate": productDate,
                                                            "selfLife": selfLife,
                                                            "warnBefore": warnBefore,
                                                            "count": count,
                                                            "warnCount": warnCount,
                                                            "inPrice": inPrice,
                                                            "outPrice": outPrice
                                                        },
                                                        success: function (response) {
                                                            alert(response.describe)
                                                            page = 0;
                                                            getProduct()
                                                        }
                                                    })
                                                } else {
                                                    alert("售价不合法")
                                                }
                                            } else {
                                                alert("进价不合法")
                                            }
                                        } else {
                                            alert("最小库存不合法")
                                        }
                                    } else {
                                        alert("库存数量不合法")
                                    }
                                } else {
                                    alert("提醒值不合法")
                                }
                            } else {
                                alert("保质期不合法")
                            }
                        } else {
                            alert("生产日期不合法")
                        }
                    } else {
                        alert("供货商不合法")
                    }
                } else {
                    alert("类型不合法")
                }
            } else {
                alert("名称不合法")
            }
        } else {
            alert("id不合法")
        }
    } else {
        alert("抱歉，您无此权限")
    }
}

function checkExcelFile(file) {
    var pattern = /^.*\.xls$/
    if (pattern.test(file) != true) {
        $("#lead-in-excel-btn").attr("disabled", true)
        alert("仅支持xls格式文档");
    } else {
        $("#lead-in-excel-btn").attr("disabled", false)
    }
}

function leadInExcel() {
    if (canProductInsert()) {
        let file = $("#lead-in-excel-input")[0].files[0];
        let formData = new FormData();
        formData.append("file",file);
        formData.append("_csrf",_csrf);
        $.ajax({
            url: contextPath + "/api/product/add/excel",
            contentType: false,
            processData: false,
            data: formData,
            type: "post",
            success: function (response) {
                if (response.status == 1) {
                    $("#lead-in-excel-close-btn").click()
                    page = 0
                    getProduct()
                } else {
                    alert(response.describe)
                }
            }
        })
    } else {
        alert("抱歉，您无此权限")
    }
}


function canProductInsert() {
    return user.role.productInsert == true;
}

function canProductDelete() {
    return user.role.productDelete == true;
}

function canProductUpdate() {
    return user.role.productUpdate == true;
}

function canProductSelect() {
    return user.role.productSelect == true;
}

function canTypeSelect() {
    return user.role.typeSelect == true;
}
