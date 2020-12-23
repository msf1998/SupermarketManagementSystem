var url = "http://localhost:8888/sms"
app.service("numberService",function($http){
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
    this.addNumber = function(number) {
        return $http({
            url: url + "/api/number/add",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: number
        })
    }
    this.deleteNumber = function(id) {
        return $http({
            url: url + "/api/number/delete",
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
    this.editNumber = function(number) {
        return $http({
            url: url + "/api/number/edit",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: number
        })
    }
    this.listNumber = function(number) {
        return $http({
            url: url + "/api/number/list",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: number
        })
    }
})