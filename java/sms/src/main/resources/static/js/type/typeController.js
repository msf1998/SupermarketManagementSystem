app.controller("typeController",function($cookieStore,$scope,typeService) {
    $scope.data = {
        type: {},   //页内通信对象
        typeList: [],  //类型列表
        user: {},  //用户
        control: {
            page: 0,   //当前页码
            modalTitle: "新增类型",  //模态框标题
            prevHide: true,  //上一页按钮隐藏
            nextHide: false, //下一页按钮隐藏
        }
    }
    //初始化
    //初始化user对象
    typeService.getMe().then(function(response) {
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
    $scope.data.type = {"order":"id","page":$scope.data.control.page};
    //初始化类型列表
    typeService.listType($scope.data.type).then(function(response) {
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
            $scope.data.typeList = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    //
    $scope.listType = function(x) {
        if ($scope.data.user.role.typeSelect != true) {
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
            $scope.data.type = {"order":"id","page":$scope.data.control.page};
             typeService.listType($scope.data.type).then(function(response) {
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
                     $scope.data.typeList = response.data.object;
                  } else {
                    alert(response.data.describe);
                  }
             })
        }
    }
    // 新增
    $scope.addType = function() {
        $scope.data.control.modalTitle = "新增类型";
        $scope.data.type = {}
    }
    //删除
    $scope.deleteType = function (t) {
        if ($scope.data.user.role.typeDelete != true) {
            alert("抱歉,您没有该权限");
        } else {
            typeService.deleteType(t.id).then(function(response){
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html"
                 } else if (response.data.status == 1){ //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token",response.data.token);
                    $scope.data.typeList = response.data.object;
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
    $scope.editType = function(t) {
        //alert(12);
        $scope.data.type = t;
        $scope.data.control.modalTitle = "修改类型信息";
        
    }

    $scope.submitType = function() {
        if ($scope.data.control.modalTitle == "新增类型") {
            if ($scope.data.user.role.typeInsert != true) {
                alert("抱歉,您没有该权限");
            } else {
                var t = $scope.data.type;
                if(t.name != null && t.name != "") {
                    typeService.addType(t).then(function(response) { 
                        if (response.data.status == 4) {   //用户登录问题
                            $cookieStore.remove("token");
                            window.location = "./login.html"
                         } else if (response.data.status == 1){ //成功
                            $cookieStore.remove("token");
                            $cookieStore.put("token",response.data.token);
                            $scope.data.typeList = response.data.object;
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
                    alert("类型名称不合法");
                }
            }
        } else {
            if ($scope.data.user.role.typeUpdate != true) {
                alert("抱歉,您没有该权限");
            } else {
                var t = $scope.data.type;t
                if(t.name != null && t.name != "") {
                    typeService.editType(t).then(function(response) { 
                        if (response.data.status == 4) {   //用户登录问题
                            $cookieStore.remove("token");
                            window.location = "./login.html"
                         } else if (response.data.status == 1){ //成功
                            $cookieStore.remove("token");
                            $cookieStore.put("token",response.data.token);
                            $scope.data.typeList = response.data.object;
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
                    alert("类型名称不合法");
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
                typeService.editMe2(form).then(function(response) {
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
                typeService.editMe1(form).then(function(response) {
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