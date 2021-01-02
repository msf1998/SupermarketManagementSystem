let sale = true;
let productIds = []
let counts = []

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
    if (productIds.length <= 0) {
        alert("没有商品");
    }
    let memberId = $("#member-id-input").val();
    let formData = new FormData();
    formData.append("_csrf",_csrf)
    formData.append("sale",sale)
    for (let i = 0; i < productIds.length; i ++) {
        formData.append("productId[" + i + "]",productIds[i])
    }
    for (let i = 0; i < counts.length; i ++) {
        formData.append("count[" + i + "]",counts[i])
    }
    if (memberId == null || memberId == undefined || memberId == "") {

    } else if(memberId.length == 11) {
        formData.append("memberId",memberId)
    } else {
        alert("会员Id不合法");
    }
    defaultFormDataAjaxRequest(formData,contextPath + "/api/order/pay")
    productIds = []
    counts = []
    $("#product-items").empty()
    $("#money").val(0)
}