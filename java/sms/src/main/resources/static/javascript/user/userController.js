app.controller('userController',function($scope,userService){

    $scope.login = function(){
        alert("用户名是："+$scope.username);
        alert("密码是："+$scope.userpassword);
        userService.login($scope.username,$scope.userpassword).success(
            function(response){
                console.log(response);
            }
        );
    }

});