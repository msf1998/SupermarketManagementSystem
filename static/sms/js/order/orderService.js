var url = "http://localhost:8888/sms"
app.service("orderService",function($http){
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
    // this.addNumber = function(number) {
    //     return $http({
    //         url: url + "/api/number/add",
    //         method: "post",
    //         headers: {
    //             "Content-Type": "application/json"
    //         },
    //         withCredentials: true,
    //         data: number
    //     })
    // }
    this.deleteOrder = function(id) {
        return $http({
            url: url + "/api/order/delete",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: {
                "id":id
            }
        })
    }
    this.editOrder = function(order) {
        return $http({
            url: url + "/api/order/edit",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: order
        })
    }
    this.listOrder = function(order) {
        return $http({
            url: url + "/api/order/list",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: order
        })
    }
    this.getDetail = function (orderDetail) {
        return $http({
            url: url + "/api/orderdetail/list",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: orderDetail
        })
    }
})