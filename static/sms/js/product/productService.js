var url = "http://localhost:8888/sms"
app.service("productService",function($http) {
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
    this.listProduct = function(product) {
        return $http({
            url:url + "/api/product/list",
            method:"post",
            withCredentials: true,
            headers:{
                "Content-Type":"application/json"
            },
            data:product
        });
    }
    //添加商品
    this.addProduct = function(product) {
        return $http({
            url:url + "/api/product/add",
            method:"post",
            withCredentials: true,
            headers:{
                "Content-Type":"application/json"
            },
            data:product
        });
    }
    this.deleteProduct = function(id) {
        return $http({
            url: url + "/api/product/delete",
            method: "post",
            withCredentials: true,
            headers: {
                "Content-Type": "application/json"
            },
            data: {
                id: id
            }
        });
    }
    //修改商品信息
    this.editProduct = function(product) {
        return $http({
            url: url + "/api/product/edit",
            method: "post",
            withCredentials: true,
            headers: {
                "Content-Type": "application/json"
            },
            data: product
        });
    }
    this.leadingInPurchase = function(form) {
        return $http({
            url: url + "/api/product/leading-in/purchase-order",
            method: "post",
            withCredentials: true,
            transformRequest: angular.identity,
            headers:{
                'Content-Type': undefined
            },
            data: form
        })
    }
    this.leadingInPhoto = function(form) {
        return $http({
            url: url + "/api/product/leading-in/photo",
            method: "post",
            withCredentials: true,
            transformRequest: angular.identity,
            headers:{
                'Content-Type': undefined
            },
            data: form
        })
    }
})