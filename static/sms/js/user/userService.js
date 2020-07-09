var url = "http://127.0.0.1:8888/sms"
app.service("userService",function($http) {
    this.checkExist = function(user) {
        return $http.post(url + "/api/user/checkExist",user);
    }
    this.register = function (user) {
        return $http.post(url + "/api/user/register",user);
    }
    this.login = function (user) {
        return $http({
            method:"post",
            url:url + "/api/user/login",
            xhrFields: {
                withCredentials: true
            },
            headers:{
                "Content-Type":"application/json",
            },
            data:user
        });
    }
})