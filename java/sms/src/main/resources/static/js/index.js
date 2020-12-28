let sale = true;
let productIds = []
let counts = []

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
    formDataAjaxRequest(formData,contextPath + "/api/user/edit/head")
}

function changePassword() {
    var password = md5($("#change-password-input").val());
    defaultAjaxRequest({
        "_csrf": _csrf,
        "password": password
    },contextPath + "/api/user/edit/password")

}

function changeState() {
    sale = !sale;
    $("#unit").text(sale ? "价格（元）" : "价格（积分）")
    $("#change-state-btn").val(sale ? "商品销售" : "积分兑换")
}

function getProductById(id,count) {
    let response = syncAjaxRequest({
        "_csrf": _csrf,
        "id": id,
        "count": count
    },contextPath + "/api/product/get");
    response = response.responseJSON;
    if (response.status != 1) {
        alert(response.describe)
    } else {
        let object = response.object;
        if ((object.typeId == 1 && sale == false) || (object.typeId != 1 && sale == true)) {
            let items = document.getElementsByName("name-" + id);
            if (items.length == 1) {
                items[0].innerText = parseInt(items[0].innerText) + object.count
            } else {
                $("#product-items").append("<tr><td hidden>" + object.id
                    + "</td> <td>" + object.name + "</td> <td name='name-" + id + "'>" +
                    object.count + "</td> <td>" + object.outPrice + "</td> </tr>")
            }
            let money = $("#money");
            money.val(parseInt(money.val()) + (object.count * object.outPrice))
            let i;
            for (i = 0; i < productIds.length; i ++) {
                if (id == productIds[i]) {
                    counts[i]  = parseInt(counts[i]) + object.count
                    break;
                }
            }
            if (i >= productIds.length) {
                productIds.push(id)
                counts.push(object.count)
            }
        } else {
            alert(object.name + "是" + (object.typeId == 1 ? "赠品，请切换至积分兑换功能" : "正常销售产品，请切换至商品销售功能"))
        }
    }
}

function pay() {
    let memberId = $("#member-id-input").val();
    let data = {}
    if (memberId == null || memberId == undefined || memberId == "") {
        data = {
            "_csrf": _csrf,
            "productId": productIds[0],
            "count": counts[0],
            "sale": sale
        }
    } else if(memberId.length == 11) {
        data = {
            "_csrf": _csrf,
            "productId": productIds[0],
            "count": counts[0],
            "sale": sale,
            "memberId": memberId
        }
    } else {
        alert("会员Id不合法");
    }
    console.log(data)
    console.log(data)
    defaultAjaxRequest(data,contextPath + "/api/order/pay")
    productIds = []
    counts = []
    $("#product-items").empty()
    $("#money").val(0)
}