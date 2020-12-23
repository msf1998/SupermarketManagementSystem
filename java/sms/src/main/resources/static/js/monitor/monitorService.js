var url = "http://localhost:8888/sms"
app.service("monitorService",function($http) {
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
    //获取商品列表
    this.listProduct1 = function() {
        return $http({
            url:url + "/api/product/list/less-than/warn-count",
            method:"post",
            withCredentials: true,
            headers:{
                "Content-Type":"application/json"
            }
        });
    }
    //获取商品列表
    this.listProduct2 = function() {
        return $http({
            url:url + "/api/product/list/will/go-bad",
            method:"post",
            withCredentials: true,
            headers:{
                "Content-Type":"application/json"
            }
        });
    }
    //修改商品信息
    this.editProduct = function(product) {
        return $http({
            url: url + "/api/product/edit/will/go-bad",
            method: "post",
            withCredentials: true,
            headers: {
                "Content-Type": "application/json"
            },
            data: product
        });
    }
    //修改商品信息
    this.deleteProduct = function(id) {
        return $http({
            url: url + "/api/product/delete/less-than/warn-count",
            method: "post",
            withCredentials: true,
            headers: {
                "Content-Type": "application/json"
            },
            data: {
                "id": id
            }
        });
    }
    this.createExcel = function() {
        return $http({
            url: url + "/api/product/leading-out/purchase-order",
            method: "post",
            withCredentials: true,
            headers: {
                "Content-Type": "application/json"
            },
        });
    }
})