app.controller("userController",function($cookieStore,$cookies,$scope,userService){
    
    $scope.user = {}
    $scope.disabled = true;
    $scope.login = function () {
        $scope.user.password = md5($scope.user.password);
        userService.login($scope.user).then(function(response) {
            console.log(response);
            if (response.data.status == 1) {
                $cookies.remove("token");
                $cookieStore.put("token",response.data.token);
                window.location = "./index.html";
            } 
            alert(response.data.describe);
        })
    }
    $scope.checkExist = function() {
        userService.checkExist($scope.user).then(function(response) {
            if (response.data.status != 1) {
                alert(response.data.describe);
                $scope.disabled = true;
               } else {
                   $scope.disabled = false;
               }
        })
    }
    $scope.register = function () {
        var pattern = /^[0-9]{11}$/
        if ($scope.user.id == "" || $scope.user.id == null || !pattern.test($scope.user.id)) {
            alert("用户名不合法");
        } else {
            if ($scope.user.password == "" || $scope.user.password == null) {
                alert("密码不能为空");
            } else {
                if ($scope.user.password != $scope.password) {
                    alert("确认密码与密码不一致");
                } else {
                    if ($scope.user.name == "" || $scope.user.name == null) {
                        alert("昵称不能为空");
                    } else {
                        if ($scope.user.confirmCode == "" || $scope.user.confirmCode == null) {
                            alert("证书不能为空");
                        } else {
                            $scope.user.password = md5($scope.user.password);
                            userService.register($scope.user).then(function(response) {
                                alert(response.data.describe);
                            })
                        }
                    }
                }
            }
        }
    }
})