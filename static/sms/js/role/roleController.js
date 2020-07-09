app.controller("roleController",function($cookieStore,$scope,roleService){
    $scope.data = {
        role: {},   //页内通信对象
        roleList: [],
        user: {},  //用户
        control: {
            page: 0,   //当前页码
            modalTitle: "新增类型",  //模态框标题
            prevHide: true,  //上一页按钮隐藏
            nextHide: false, //下一页按钮隐藏
        }
    }
    //初始化user对象
    roleService.getMe().then(function(response) {
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
    $scope.data.role = {"order":"id","page":$scope.data.control.page};
    //初始化类型列表
    roleService.listRole($scope.data.role).then(function(response) {
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
            $scope.data.roleList = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    $scope.getDetail = function(r) {
        if ($scope.data.user.role.orderSelect != true) {
            alert("抱歉,您没有该权限");
        } else {
            var img = $(".tr" + r.id +" td img").attr("src");
            if (img == "lib/advanced-datatable/images/details_open.png") {
                $(".tr" + r.id).after("<table class='table table-striped table-advance table-hover table" + r.id +"' cellpadding='0' cellspacing='0' border='0' class='display table table-bordered' id='hidden-table-info'>" +
                "<thead>" +
                  "<tr>" +
                    "<th><i class=‘fa’></i>商品添加</th>" + 
                    "<th><i class=‘fa’></i>商品删除</th>" +
                    "<th><i class=‘fa’></i>商品修改</th>" +
                    "<th><i class=‘fa’></i>商品查询</th>" +
                    "<th><i class=‘fa’></i>类型添加</th>" + 
                    "<th><i class=‘fa’></i>类型删除</th>" +
                    "<th><i class=‘fa’></i>类型修改</th>" +
                    "<th><i class=‘fa’></i>类型查询</th>" +
                    "<th><i class=‘fa’></i>会员添加</th>" + 
                    "<th><i class=‘fa’></i>会员删除</th>" +
                    "<th><i class=‘fa’></i>会员修改</th>" +
                    "<th><i class=‘fa’></i>会员查询</th>" +
                    "<th><i class=‘fa’></i>订单添加</th>" + 
                    "<th><i class=‘fa’></i>订单删除</th>" +
                    "<th><i class=‘fa’></i>订单修改</th>" +
                    "<th><i class=‘fa’></i>订单查询</th>" +
                    "<th><i class=‘fa’></i>员工添加</th>" + 
                    "<th><i class=‘fa’></i>员工删除</th>" +
                    "<th><i class=‘fa’></i>员工修改</th>" +
                    "<th><i class=‘fa’></i>员工查询</th>" +
                    "<th><i class=‘fa’></i>角色添加</th>" + 
                    "<th><i class=‘fa’></i>角色删除</th>" +
                    "<th><i class=‘fa’></i>角色修改</th>" +
                    "<th><i class=‘fa’></i>角色查询</th>" +
                  "</tr>" +
                "</thead>"+
                "<tbody>" +
                  "<tr>" +
                    "<td>" + (r.productInsert ? "允许":"拒绝") +"</td>" +
                    "<td>" + (r.productDelete ? "允许":"拒绝") + "</td>" +
                    "<td>"+ (r.productUpdate ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.productSelect ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.typeInsert ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.typeDelete ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.typeUpdate ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.typeSelect ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.numberInsert ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.numberDelete ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.numberUpdate ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.numberSelect ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.orderInsert ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.orderDelete ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.orderUpdate ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.orderSelect ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.userInsert ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.userDelete ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.userUpdate ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.userSelect ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.roleInsert ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.roleDelete ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.roleUpdate ? "允许":"拒绝") +"</td>" +
                    "<td>"+ (r.roleSelect ? "允许":"拒绝") +"</td>" +
                  "</tr>" +
                "</tbody>" +
              "</table>");
                $(".tr" + r.id +" td img").attr("src","lib/advanced-datatable/images/details_close.png");
            } else {
                $(".table" + r.id).remove();
                $(".tr" + r.id +" td img").attr("src","lib/advanced-datatable/images/details_open.png");
            }
        }
    }
    //
    $scope.listRole = function(x) {
        if ($scope.data.user.role.roleSelect != true) {
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
            $scope.data.role = {"order":"id","page":$scope.data.control.page};
            roleService.listRole($scope.data.role).then(function(response) {
                //console.log(response)
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
                     $scope.data.roleList = response.data.object;
                  } else {
                    alert(response.data.describe);
                  }
             })
        }
    }
    // 新增
    $scope.addRole = function() {
        $scope.data.control.modalTitle = "新增角色";
        $scope.data.role = {}
        $("#advance-privilege").attr("hidden",true)
    }
    //删除
    $scope.deleteRole = function (r) {
        if ($scope.data.user.role.roleDelete != true) {
            alert("抱歉,您没有该权限");
        } else {
            roleService.deleteRole(r.id).then(function(response){
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html"
                 } else if (response.data.status == 1){ //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token",response.data.token);
                    $scope.data.roleList = response.data.object;
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
    $scope.editRole = function(r) {
        //alert(12);
        $scope.data.role = r;
        $scope.data.control.modalTitle = "修改角色信息";
        $("#advance-privilege").attr("hidden",true)
    }

    $scope.submitRole = function() {
        if ($scope.data.control.modalTitle == "新增角色") {
            if ($scope.data.user.role.roleInsert != true) {
                alert("抱歉,您没有该权限");
            } else {
                var r = $scope.data.role;
                if(r.name != null && r.name != "") {
                    r.productInsert = (r.productInsert == null || r.productInsert == undefined) ? false :r.productInsert
                    r.productDelete = (r.productDelete == null || r.productDelete == undefined) ? false :r.productDelete
                    r.productUpdate = (r.productUpdate == null || r.productUpdate == undefined) ? false :r.productUpdate
                    r.productSelect = (r.productSelect == null || r.productSelect == undefined) ? false :r.productSelect
                    r.typeInsert = (r.typeInsert == null || r.typeInsert == undefined) ? false :r.typeInsert
                    r.typeDelete = (r.typeDelete == null || r.typeDelete == undefined) ? false :r.typeDelete
                    r.typeUpdate = (r.typeUpdate == null || r.typeUpdate == undefined) ? false :r.typeUpdate
                    r.typeSelect = (r.typeSelect == null || r.typeSelect == undefined) ? false :r.typeSelect
                    r.numberInsert = (r.numberInsert == null || r.numberInsert == undefined) ? false :r.numberInsert
                    r.numberDelete = (r.numberDelete == null || r.numberDelete == undefined) ? false :r.numberDelete
                    r.numberUpdate = (r.numberUpdate == null || r.numberUpdate == undefined) ? false :r.numberUpdate
                    r.numberSelect = (r.numberSelect == null || r.numberSelect == undefined) ? false :r.numberSelect
                    r.orderInsert = (r.orderInsert == null || r.orderInsert == undefined) ? false :r.orderInsert
                    r.orderDelete = (r.orderDelete == null || r.orderDelete == undefined) ? false :r.orderDelete
                    r.orderUpdate = (r.orderUpdate == null || r.orderUpdate == undefined) ? false :r.orderUpdate
                    r.orderSelect = (r.orderSelect == null || r.orderSelect == undefined) ? false :r.orderSelect
                    r.userInsert = (r.userInsert == null || r.userInsert == undefined) ? false :r.userInsert
                    r.userDelete = (r.userDelete == null || r.userDelete == undefined) ? false :r.userDelete
                    r.userUpdate = (r.userUpdate == null || r.userUpdate == undefined) ? false :r.userUpdate
                    r.userSelect = (r.userSelect == null || r.userSelect == undefined) ? false :r.userSelect
                    r.roleInsert = (r.roleInsert == null || r.roleInsert == undefined) ? false :r.roleInsert
                    r.roleDelete = (r.roleDelete == null || r.roleDelete == undefined) ? false :r.roleDelete
                    r.roleUpdate = (r.roleUpdate == null || r.roleUpdate == undefined) ? false :r.roleUpdate
                    r.roleSelect = (r.roleSelect == null || r.roleSelect == undefined) ? false :r.roleSelect
                    roleService.addRole(r).then(function(response) { 
                        if (response.data.status == 4) {   //用户登录问题
                            $cookieStore.remove("token");
                            window.location = "./login.html"
                         } else if (response.data.status == 1){ //成功
                            $cookieStore.remove("token");
                            $cookieStore.put("token",response.data.token);
                            $scope.data.roleList = response.data.object;
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
                alert("名称不合法");
            }
               
            }
        } else {
            if ($scope.data.user.role.roleUpdate != true) {
                alert("抱歉,您没有该权限");
            } else {
                var r = $scope.data.role;
                if(r.name != null && r.name != "") {
                    r.productInsert = (r.productInsert == null || r.productInsert == undefined) ? false :r.productInsert
                    r.productDelete = (r.productDelete == null || r.productDelete == undefined) ? false :r.productDelete
                    r.productUpdate = (r.productUpdate == null || r.productUpdate == undefined) ? false :r.productUpdate
                    r.productSelect = (r.productSelect == null || r.productSelect == undefined) ? false :r.productSelect
                    r.typeInsert = (r.typeInsert == null || r.typeInsert == undefined) ? false :r.typeInsert
                    r.typeDelete = (r.typeDelete == null || r.typeDelete == undefined) ? false :r.typeDelete
                    r.typeUpdate = (r.typeUpdate == null || r.typeUpdate == undefined) ? false :r.typeUpdate
                    r.typeSelect = (r.typeSelect == null || r.typeSelect == undefined) ? false :r.typeSelect
                    r.numberInsert = (r.numberInsert == null || r.numberInsert == undefined) ? false :r.numberInsert
                    r.numberDelete = (r.numberDelete == null || r.numberDelete == undefined) ? false :r.numberDelete
                    r.numberUpdate = (r.numberUpdate == null || r.numberUpdate == undefined) ? false :r.numberUpdate
                    r.numberSelect = (r.numberSelect == null || r.numberSelect == undefined) ? false :r.numberSelect
                    r.orderInsert = (r.orderInsert == null || r.orderInsert == undefined) ? false :r.orderInsert
                    r.orderDelete = (r.orderDelete == null || r.orderDelete == undefined) ? false :r.orderDelete
                    r.orderUpdate = (r.orderUpdate == null || r.orderUpdate == undefined) ? false :r.orderUpdate
                    r.orderSelect = (r.orderSelect == null || r.orderSelect == undefined) ? false :r.orderSelect
                    r.userInsert = (r.userInsert == null || r.userInsert == undefined) ? false :r.userInsert
                    r.userDelete = (r.userDelete == null || r.userDelete == undefined) ? false :r.userDelete
                    r.userUpdate = (r.userUpdate == null || r.userUpdate == undefined) ? false :r.userUpdate
                    r.userSelect = (r.userSelect == null || r.userSelect == undefined) ? false :r.userSelect
                    r.roleInsert = (r.roleInsert == null || r.roleInsert == undefined) ? false :r.roleInsert
                    r.roleDelete = (r.roleDelete == null || r.roleDelete == undefined) ? false :r.roleDelete
                    r.roleUpdate = (r.roleUpdate == null || r.roleUpdate == undefined) ? false :r.roleUpdate
                    r.roleSelect = (r.roleSelect == null || r.roleSelect == undefined) ? false :r.roleSelect
                    roleService.editRole(r).then(function(response) { 
                        if (response.data.status == 4) {   //用户登录问题
                            $cookieStore.remove("token");
                            window.location = "./login.html"
                         } else if (response.data.status == 1){ //成功
                            $cookieStore.remove("token");
                            $cookieStore.put("token",response.data.token);
                            $scope.data.roleList = response.data.object;
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
                alert("名称不合法");
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
                roleService.editMe2(form).then(function(response) {
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
                roleService.editMe1(form).then(function(response) {
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