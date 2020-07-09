var url = "http://localhost:8888/sms"
app.service("typeService",function($http){
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
    this.addType = function(type) {
        return $http({
            url: url + "/api/type/add",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: type
        })
    }
    this.deleteType = function(id) {
        return $http({
            url: url + "/api/type/delete",
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
    this.editType = function(type) {
        return $http({
            url: url + "/api/type/edit",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: type
        })
    }
    this.listType = function(type) {
        return $http({
            url: url + "/api/type/list",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: type
        })
    }
})