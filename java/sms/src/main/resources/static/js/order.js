let page = 0;
let order = "id";
let orders = [];

function getOrder() {
    if (canOrderSelect()) {
        $.ajax({
            url: contextPath + "/api/order/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order
            },
            success: function (response) {
                if (response.status == 1) {
                    orders = response.object;
                    $("#order-items").empty();
                    for (let i = 0; i < orders.length; i++) {
                        $("#order-items").append("<tr ng-repeat=\"o in data.orderList\" class=\"" + "order_" + orders[i].id + "\">\n" +
                            "                                <td><img src=\"" + contextPath + "/lib/advanced-datatable/images/details_open.png" + "\"\n" +
                            "                                         onclick=\"getDetail('" + orders[i].id + "')\"></td>\n" +
                            "                                <td>" + orders[i].id + "</td>\n" +
                            "                                <td>" + orders[i].describe + "</td>\n" +
                            "                                <td>" + orders[i].sum + "</td>\n" +
                            "                                <td>" + orders[i].createTime.substr(0,10) + "</td>\n" +
                            "                                <td>" + orders[i].creator.name + "</td>\n" +
                            "                                <td>" + orders[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-primary btn-xs\" onclick=\"editOrder('" + orders[i].id + "')\" data-toggle=\"modal\"\n" +
                            "                                            data-target=\"#edit-order-modal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                                    <button class=\"btn btn-danger btn-xs\" onclick=\"deleteOrder('" + orders[i].id + "')\"><i\n" +
                            "                                            class=\"fa fa-trash-o \"></i></button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
                    }
                    if (orders.length < 10) {
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
    if (canOrderSelect()) {
        var img = $(".order_" + id +" td img").attr("src");
        if (img == (contextPath + "/lib/advanced-datatable/images/details_open.png")) {
            $.ajax({
                url: contextPath + "/api/orderdetail/list",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "order": id
                },
                success: function(response) {
                    var od = response.object;
                    var sum = 0;
                    for (i = 0; i < od.length; i ++) {
                        $(".order_" + id).after("<div style='width:100%' class='div" + id +"'>"
                            + "<h5 style='font-weight:900'>商品编号:</h5>" + od[i].product
                            + "<h5 style='font-weight:900'>商品名称:</h5>" + od[i].productName
                            + "<h5 style='font-weight:900'>商品价格:</h5>" + od[i].productPrice
                            + "<h5 style='font-weight:900'>购买数量:</h5>" + od[i].count
                            + "<h5 style='font-weight:900'>合计:</h5>" + od[i].productPrice * od[i].count
                            + "<h5>--------------------------------------------------------------------------------------------------------------------------------------------</h5>"
                            + "</div>");
                        sum = sum + od[i].productPrice * od[i].count;
                    }
                    $(".order_" + id).after("<h3 class='h3" + id + "' style='color:blue;margin:0 auto'>总计消费: " + sum + "</h3>")
                    $(".order_" + id +" td img").attr("src",contextPath + "/lib/advanced-datatable/images/details_close.png");
                }
            })
        } else {
            $(".div" + id).remove();
            $(".h3" + id).remove();
            $(".order_" + id +" td img").attr("src",contextPath + "/lib/advanced-datatable/images/details_open.png");
        }
    } else {
        alert("抱歉，您无此权限")
    }
}

function editOrder(id) {
    let i;
    for (i = 0; i < orders.length; i ++) {
        if (id == orders[i].id) {
            break;
        }
    }
    let o = orders[i];
    $("#edit-id-input").val(o.id)
    $("#edit-describe-input").val(o.describe)
    $("#edit-order-btn").attr("disabled",true)
}

function enableSubmit() {
    $("#edit-order-btn").attr("disabled",false)
}

function submitOrder() {
    if (canOrderUpdate()) {
        let id = $("#edit-id-input").val();
        let describe = $("#edit-describe-input").val();
        if (id != null && id != '' && id != undefined && id > 0) {
            if(describe.length <= 255) {
                $.ajax({
                    url: contextPath + "/api/order/edit",
                    type: "post",
                    data: {
                        "_csrf": _csrf,
                        "id": id,
                        "describe": describe
                    },
                    success: function (response) {
                        if (response.status == 1) {
                            $("#edit-order-close-btn").click()
                            getOrder()
                        } else {
                            alert(response.describe)
                        }
                    }
                })
            } else {
                alert("备注不合法")
            }
        } else {
            alert("id不合法")
        }
    } else {
        alert("抱歉，您无此权限")
    }
}

function deleteOrder(id) {
    if (canOrderDelete()) {
        let i;
        for (i = 0; i < orders.length; i ++) {
            if (id == orders[i].id) {
                break;
            }
        }
        if (confirm("确定删除订单" + orders[i].id + "?")) {
            $.ajax({
                url: contextPath + "/api/order/delete",
                type: "post",
                data: {
                    "_csrf": _csrf,
                    "id": id
                },
                success: function (response) {
                    if (response.status == 1) {
                        getOrder();
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
    getOrder()
}

function beforePage() {
    page--;
    getOrder()
}

function canOrderSelect() {
    return user.role.orderSelect == true;
}

function canOrderInsert() {
    return user.role.orderInsert == true;
}

function canOrderDelete() {
    return user.role.orderDelete == true;
}

function canOrderUpdate() {
    return user.role.orderUpdate == true;
}