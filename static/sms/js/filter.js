app.filter("dateFilter",function() {
    return function(date) {
        var ch = date.split("");
        var nch = new Array(10);
        for (var i = 0; i < 10; i ++) {
            nch[i] = ch[i];
        }
        return nch.join("");
    }
})
app.filter("photoFilter",function(){
    return function(path) {
        return "http://localhost:8888/sms/product/" + path;
    }
})
app.filter("intFilter",function() {
    return function(text) {
        return parseInt(text);
    }
})
app.filter("productIdHideFilter",function(){
    return function(text) {
        if (text == "添加商品") {
            return false;
        } else {
            return true;
        }
    }
})
app.filter("boolFilter",function(){
    return function(b) {
        if (b == true || b == "true") {
            return "是";
        } else {
            return "否";
        }
    }
})
app.filter("typeIdHideFilter",function(){
    return function(text) {
        if (text == "新增类型") {
            return false;
        } else {
            return true;
        }
    }
})
app.filter("userIdHideFilter",function(){
    return function(text) {
        if (text == "新增员工") {
            return false;
        } else {
            return true;
        }
    }
})
app.filter("qCodeAddressFilter",function(){
    return function(text) {
        return "http://127.0.0.1:8888/sms/static/download/qcode?fileName="+text;
    }
})
app.filter("typeFilter",function(){
    return function(text) {
        if (text == null || text == "" || text == undefined) {
            return 商品类别;
        } else {
            return text;
        }
    }
})
app.filter("privilageFilter",function(){
    return function(text) {
        if (text) {
            return "允许"
        } else {
            return "拒绝"
        }
    }
})
app.filter("saleMoneyFilter",function() {
    return function(text) {
        if (text == true) {
            return "(元)"
        } else {
            return "(积分)"
        }
    }
})
app.filter("saleFilter",function() {
    return function(text) {
        if (text == true) {
            return "积分兑换"
        } else {
            return "商品销售"
        }
    }
})