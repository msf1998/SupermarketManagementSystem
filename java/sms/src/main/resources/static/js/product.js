let page = 0;
let order = "name";
let product = [];
let type = 1;
function getProduct() {
    if (canProductSelect()) {
        $.ajax({
            url: contextPath + "/api/product/list",
            type: "post",
            data: {
                "_csrf": _csrf,
                "page": page,
                "order": order,
            },
            success: function (response) {
                if (response.status == 1) {
                    product = response.object
                    for (let i = 0; i < product.length; i ++) {
                        $("#product-items").append("<tr>\n" +
                            "                                <td>" + product[i].id + "</td>\n" +
                            "                                <td><img src='" + contextPath + "/product/" + product[i].photo + "' style=\"width: 30px;height: 30px;\"\n" +
                            "                                         data-toggle=\"modal\"\n" +
                            "                                         data-target=\"#myModal3\"></td>\n" +
                            "                                <td>" + product[i].name + "</td>\n" +
                            "                                <td>" + product[i].type.name + "</td>\n" +
                            "                                <td>" + product[i].manufacturer + "</td>\n" +
                            "                                <td>" + product[i].productDate + "</td>\n" +
                            "                                <td>" + product[i].selfLife + "</td>\n" +
                            "                                <td>" + product[i].warnBefore + "</td>\n" +
                            "                                <td>" + product[i].count + "</td>\n" +
                            "                                <td>" + product[i].warnCount + "</td>\n" +
                            "                                <td>" + product[i].inTime + "</td>\n" +
                            "                                <td>" + product[i].inPrice + "</td>\n" +
                            "                                <td>" + product[i].outPrice + "</td>\n" +
                            "                                <td>" + product[i].parent.name + "</td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-default btn-xs\"><a href=\"" + contextPath + "/product/qcode/" + product[i].qcode + "\">打印</a>\n" +
                            "                                </button>\n" +
                            "                                </td>\n" +
                            "                                <td>\n" +
                            "                                    <button class=\"btn btn-primary btn-xs\" ng-click=\"editProduct(p)\" data-toggle=\"modal\"\n" +
                            "                                            data-target=\"#myModal\"><i class=\"fa fa-pencil\"></i></button>\n" +
                            "                                    <button class=\"btn btn-danger btn-xs\" ng-click=\"deleteProduct(p)\"><i\n" +
                            "                                            class=\"fa fa-trash-o \"></i></button>\n" +
                            "                                </td>\n" +
                            "                            </tr>")
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

}

function canProductSelect() {
    return user.role.productSelect == true;
}