var url = "http://localhost:8888/sms"
app.service("userService",function($http){
    this.listRole = function (role) {
        return $http({
            url:url + "/api/role/list",
            method:"post",
            withCredentials: true,
            data: role
        })
    }
    
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
    this.addUser = function(user) {
        return $http({
            url: url + "/api/user/add",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: user
        })
    }
    this.deleteUser = function(id) {
        return $http({
            url: url + "/api/user/delete",
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
    this.editUser = function(user) {
        return $http({
            url: url + "/api/user/edit",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: user
        })
    }
    this.listUser = function(user) {
        return $http({
            url: url + "/api/user/list",
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true,
            data: user
        })
    }
})