var url = "http://localhost:8888/sms"
app.service("saleService",function($http) {
    //获取个人信息
    this.getMe = function(){
        return $http({
            url:url + "/api/user/get",
            method:"get",
            withCredentials: true,
        });
    }

    this.editMe1 = function(form) {
        return $http({
            url: url + "/api/user/edit/me1",
            method: "post",
            withCredentials: true,
            transformRequest: angular.identity,
            headers:{
                'Content-Type': undefined
            },
            data: form
        })
    }
    this.editMe2 = function(form) {
        return $http({
            url: url + "/api/user/edit/me2",
            method: "post",
            withCredentials: true,
            transformRequest: angular.identity,
            headers:{
                'Content-Type': undefined
            },
            data: form
        })
    }

    //获取商品
    this.getProduct = function(product) {
        return $http({
            url:url + "/api/product/get",
            method:"post",
            withCredentials: true,
            headers:{
                "Content-Type":"application/json"
            },
            data: product
        });
    }
    this.pay = function(numberId,productList,sale) {
        var data = {
            numberId: "",
            sale: true,
            productId: [],
            count: []
        };
        data.numberId = numberId;
        data.sale = sale;
        for (var i = 0;i < productList.length; i ++) {
            data.productId.push(productList[i].id)
            data.count.push(productList[i].count)
        }
        return $http({
            url:url + "/api/order/pay",
            method:"post",
            withCredentials: true,
            headers:{
                "Content-Type":"application/json"
            },
            data: data
        });
    }
})