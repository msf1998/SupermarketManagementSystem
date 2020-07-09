app.controller("numberController",function($cookieStore,$scope,numberService) {
    $scope.data = {
        number: {},   //页内通信对象
        numberList: [],  //类型列表
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
    numberService.getMe().then(function(response) {
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
    //初始化number对象
    $scope.data.number = {"order":"name","page":$scope.data.control.page};
    //初始化类型列表
    numberService.listNumber($scope.data.number).then(function(response) {
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
            $scope.data.numberList = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    //
    $scope.listNumber = function(x) {
        if ($scope.data.user.role.numberSelect != true) {
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
            $scope.data.number = {"order":"name","page":$scope.data.control.page};
             numberService.listNumber($scope.data.number).then(function(response) {
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
                     $scope.data.numberList = response.data.object;
                  } else {
                    alert(response.data.describe);
                  }
             })
        }
    }
    // 新增
    $scope.addNumber = function() {
        $scope.data.control.modalTitle = "新增会员";
        $scope.data.number = {}
    }
    //删除
    $scope.deleteNumber = function (n) {
        if ($scope.data.user.role.numberDelete != true) {
            alert("抱歉,您没有该权限");
        } else {
            numberService.deleteNumber(n.id).then(function(response){
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html"
                 } else if (response.data.status == 1){ //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token",response.data.token);
                    $scope.data.numberList = response.data.object;
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
    $scope.editNumber = function(n) {
        //alert(12);
        $scope.data.number = n;
        $scope.data.control.modalTitle = "修改会员信息";
        
    }

    $scope.submitNumber = function() {
        if ($scope.data.control.modalTitle == "新增会员") {
            if ($scope.data.user.role.numberInsert != true) {
                alert("抱歉,您没有该权限");
            } else {
                var parrent1 = /^[0-9]{11}$/;
                var parrent2 = /^[0-9]{18}$/;
                var n = $scope.data.number;
                if(n.name != null && n.name != "") {
                    if (parrent1.test(n.phone)) {
                        if (parrent2.test(n.idNumber)) {
                            numberService.addNumber(n).then(function(response) { 
                                if (response.data.status == 4) {   //用户登录问题
                                    $cookieStore.remove("token");
                                    window.location = "./login.html"
                                 } else if (response.data.status == 1){ //成功
                                    $cookieStore.remove("token");
                                    $cookieStore.put("token",response.data.token);
                                    $scope.data.numberList = response.data.object;
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
                            alert("身份证号不合法");
                        }
                    } else {
                        alert("手机号不合法");
                    }
                } else {
                    alert("姓名不合法");
                }
            }
        } else {
            if ($scope.data.user.role.numberUpdate != true) {
                alert("抱歉,您没有该权限");
            } else {
                var parrent1 = /^[0-9]{11}$/;
                var parrent2 = /^[0-9]{18}$/;
                var n = $scope.data.number;
                if(n.name != null && n.name != "") {
                    if (parrent1.test(n.phone)) {
                        if (parrent2.test(n.idNumber)) {
                            numberService.editNumber(n).then(function(response) { 
                                if (response.data.status == 4) {   //用户登录问题
                                    $cookieStore.remove("token");
                                    window.location = "./login.html"
                                 } else if (response.data.status == 1){ //成功
                                    $cookieStore.remove("token");
                                    $cookieStore.put("token",response.data.token);
                                    $scope.data.numberList = response.data.object;
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
                            alert("身份证号不合法");
                        }
                    } else {
                        alert("手机号不合法");
                    }
                } else {
                    alert("姓名不合法");
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
                numberService.editMe2(form).then(function(response) {
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
                numberService.editMe1(form).then(function(response) {
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