app.controller("userController",function($cookieStore,$scope,userService){
    $scope.data = {
        tuser: {},   //页内通信对象
        roleList: [],
        userList: [],  //类型列表
        user: {},  //用户
        control: {
            page: 0,   //当前页码
            modalTitle: "新增类型",  //模态框标题
            prevHide: true,  //上一页按钮隐藏
            nextHide: false, //下一页按钮隐藏
        }
    }
    //初始化
    userService.listRole({"order":"id"}).then(function (response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
         } else if (response.data.status == 1){ //成功
            $cookieStore.remove("token");
            $cookieStore.put("token",response.data.token);
            $scope.data.roleList = response.data.object;
         } else {
            alert(response.data.describe)
         }
    })
    //初始化user对象
    userService.getMe().then(function(response) {
        //console.log(response)
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
         } else if (response.data.status == 1){ //成功
            $cookieStore.remove("token");
            $cookieStore.put("token",response.data.token);
            $scope.data.user = response.data.object;
         } else {
            alert(response.data.describe)
         }
    })
    //初始化type对象
    $scope.data.tuser = {"order":"id","page":$scope.data.control.page};
    //初始化类型列表
    userService.listUser($scope.data.tuser).then(function(response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
         } else if (response.data.status == 1){ //成功
            $cookieStore.remove("token");
            $cookieStore.put("token",response.data.token);
            if (response.data.object.length < 10) {
                $scope.data.control.nextHide = true;
            }
            $scope.data.userList = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    //
    $scope.listUser = function(x) {
        if ($scope.data.user.role.userSelect != true) {
            alert("抱歉,您没有该权限");
        } else {
            if (x == -1) {
                $scope.data.control.page = $scope.data.control.page - 1;   
                $scope.data.control.nextHide = false;
            } else {
                $scope.data.control.page = $scope.data.control.page + 1;
                $scope.data.control.prevHide = false;
            }
            if ($scope.data.control.page <= 0) {
                $scope.data.control.prevHide = true;
            }
            $scope.data.tuser = {"order":"id","page":$scope.data.control.page};
            userService.listUser($scope.data.tuser).then(function(response) {
                console.log(response)
                 if (response.data.status == 4) {   //用户登录问题
                     $cookieStore.remove("token");
                     window.location = "./login.html";
                     alert(response.data.describe);
                  } else if (response.data.status == 1){ //成功
                     $cookieStore.remove("token");
                     $cookieStore.put("token",response.data.token);
                     if (response.data.object.length < 10) {
                         $scope.data.control.nextHide = true;
                     }
                     $scope.data.userList = response.data.object;
                  } else {
                    alert(response.data.describe);
                  }
             })
        }
    }
    // 新增
    $scope.addUser = function() {
        $scope.data.control.modalTitle = "新增员工";
        $scope.data.tuser = {}
    }
    //删除
    $scope.deleteUser = function (u) {
        if ($scope.data.user.role.userDelete != true) {
            alert("抱歉,您没有该权限");
        } else {
            userService.deleteUser(u.id).then(function(response){
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html"
                 } else if (response.data.status == 1){ //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token",response.data.token);
                    $scope.data.userList = response.data.object;
                    $scope.data.control.prevHide = true;
                    $scope.data.control.page = 0;
                    if (response.data.object.length < 10) {
                        $scope.data.control.nextHide = true;
                    } else {
                        $scope.data.control.nextHide = false;
                    }
                 } 
                 alert(response.data.describe)
            })
        }
    }
    //修改
    $scope.editUser = function(u) {
        //alert(12);
        $scope.data.tuser = u;
        $scope.data.control.modalTitle = "修改员工信息";
        
    }

    $scope.submitUser = function() {
        if ($scope.data.control.modalTitle == "新增员工") {
            if ($scope.data.user.role.userInsert != true) {
                alert("抱歉,您没有该权限");
            } else {
                var parrent = /^[0-9]{11}$/
                var u = $scope.data.tuser;
                if (parrent.test(u.id)) {
                    if(u.name != null && u.name != "") {
                        if(u.roleId != null && u.roleId != "") {
                            if (u.password != null && u.password != undefined && u.password != '') {
                                u.password = md5(u.password)
                            }
                            userService.addUser(u).then(function(response) { 
                                if (response.data.status == 4) {   //用户登录问题
                                    $cookieStore.remove("token");
                                    window.location = "./login.html"
                                 } else if (response.data.status == 1){ //成功
                                    $cookieStore.remove("token");
                                    $cookieStore.put("token",response.data.token);
                                    $scope.data.userList = response.data.object;
                                    $scope.data.control.prevHide = true;
                                    $scope.data.control.page = 0;
                                    if (response.data.object.length < 10) {
                                        $scope.data.control.nextHide = true;
                                    } else {
                                        $scope.data.control.nextHide = false;
                                    }
                                 } 
                                 alert(response.data.describe)
                             })
                        } else {
                            alert("角色不合法");
                        }
                    } else {
                        alert("名称不合法");
                    }
                } else {
                    alert("用户账号必须为手机号");
                }
            }
        } else {
            if ($scope.data.user.role.userUpdate != true) {
                alert("抱歉,您没有该权限");
            } else {
                var parrent = /^[0-9]{11}$/
                var u = $scope.data.tuser;
                if (parrent.test(u.id)) {
                    if(u.name != null && u.name != "") {
                        if(u.roleId != null && u.roleId != "") {
                            console.log(u)
                            if (u.password != null && u.password != "" && u.password != undefined) {
                                u.password = md5(u.password);
                            }
                            console.log(u)
                            userService.editUser(u).then(function(response) { 
                                if (response.data.status == 4) {   //用户登录问题
                                    $cookieStore.remove("token");
                                    window.location = "./login.html"
                                 } else if (response.data.status == 1){ //成功
                                    $cookieStore.remove("token");
                                    $cookieStore.put("token",response.data.token);
                                    $scope.data.userList = response.data.object;
                                    $scope.data.control.prevHide = true;
                                    $scope.data.control.page = 0;
                                    if (response.data.object.length < 10) {
                                        $scope.data.control.nextHide = true;
                                    } else {
                                        $scope.data.control.nextHide = false;
                                    }
                                 } 
                                 alert(response.data.describe)
                             })
                        } else {
                            alert("角色不合法");
                        }
                    } else {
                        alert("名称不合法");
                    }
                } else {
                    alert("用户账号必须为手机号");
                }
            }
        }
    }
    $scope.editMe = function() {
        var form = new FormData();
        var head = $("#leading-in-head")[0].files[0];
        var password = $("#password").val();
        if (head != null && head != undefined && head != "") {
            form.append("head",head);
        }
        if (password != null && password != undefined && password != "") {
            form.append("password",md5(password));
        } else {
            form.append("password",null)
        }
        if (head != null || password != "") {
            if (head == null) {
                userService.editMe2(form).then(function(response) {
                    if (response.data.status == 4) {   //用户登录问题
                        $cookieStore.remove("token");
                        window.location = "./login.html";
                        alert(response.data.describe);
                    } else if (response.data.status == 1) { //成功
                        $cookieStore.remove("token");
                        $cookieStore.put("token", response.data.token);
                        $scope.data.user = response.data.object;
                    }
                    alert(response.data.describe)
                })
            } else {
                userService.editMe1(form).then(function(response) {
                    if (response.data.status == 4) {   //用户登录问题
                        $cookieStore.remove("token");
                        window.location = "./login.html";
                        alert(response.data.describe);
                    } else if (response.data.status == 1) { //成功
                        $cookieStore.remove("token");
                        $cookieStore.put("token", response.data.token);
                        $scope.data.user = response.data.object;
                    }
                    alert(response.data.describe)
                })
            }
            
        }
    }

    $scope.logout = function() {
        $cookieStore.remove("token");
        window.location = "./login.html";
    }
})