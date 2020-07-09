app.controller("orderController",function($cookieStore,$scope,orderService) {
    $scope.data = {
        order: {},   //页内通信对象
        orderList: [],  //类型列表
        orderDetailList:[],
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
    orderService.getMe().then(function(response) {
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
    //初始化order对象
    $scope.data.order = {"order":"id","page":$scope.data.control.page};
    //初始化类型列表
    orderService.listOrder($scope.data.order).then(function(response) {
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
            $scope.data.orderList = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    $scope.getDetail = function(id) {
        if ($scope.data.user.role.orderSelect != true) {
            alert("抱歉,您没有该权限");
        } else {
            var img = $(".tr" + id +" td img").attr("src");
            if (img == "lib/advanced-datatable/images/details_open.png") {
                orderService.getDetail({"order":id}).then(function(response){
                    var od = response.data.object;
                    var sum = 0;
                    for (i = 0; i < od.length; i ++) {
                        $(".tr" + id).after("<div style='width:100%' class='div" + id +"'>"
                        + "<h5 style='font-weight:900'>商品编号:</h5>" + od[i].product 
                        + "<h5 style='font-weight:900'>商品名称:</h5>" + od[i].productName
                        + "<h5 style='font-weight:900'>商品价格:</h5>" + od[i].productPrice 
                        + "<h5 style='font-weight:900'>购买数量:</h5>" + od[i].count
                        + "<h5 style='font-weight:900'>合计:</h5>" + od[i].productPrice * od[i].count
                        + "<h5>--------------------------------------------------------------------------------------------------------------------------------------------</h5>"
                        + "</div>");
                        sum = sum + od[i].productPrice * od[i].count;
                    }
                    $(".tr" + id).after("<h3 class='h3" + id + "' style='color:blue;margin:0 auto'>总计消费: " + sum + "</h3>")
                    $(".tr" + id +" td img").attr("src","lib/advanced-datatable/images/details_close.png");
                })
            } else {
                $(".div" + id).remove();
                $(".h3" + id).remove();
                $(".tr" + id +" td img").attr("src","lib/advanced-datatable/images/details_open.png");
            }
        }
    }
    //
    $scope.listOrder = function(x) {
        if ($scope.data.user.role.orderSelect != true) {
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
            $scope.data.order = {"order":"id","page":$scope.data.control.page};
            orderService.listOrder($scope.data.order).then(function(response) {
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
                     $scope.data.orderList = response.data.object;
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
    $scope.deleteOrder = function (o) {
        if ($scope.data.user.role.orderDelete != true) {
            alert("抱歉,您没有该权限");
        } else {
            orderService.deleteOrder(o.id).then(function(response){
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html"
                 } else if (response.data.status == 1){ //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token",response.data.token);
                    $scope.data.orderList = response.data.object;
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
    $scope.editOrder = function(o) {
        //alert(12);
        $scope.data.order = o;
        $scope.data.control.modalTitle = "修改订单信息";
        
    }

    $scope.submitOrder = function() {
        if ($scope.data.control.modalTitle == "修改订单信息") {
            if ($scope.data.user.role.orderUpdate != true) {
                alert("抱歉,您没有该权限");
            } else {
                var parrent1 = /^[0-9]{11}$/;
                var parrent2 = /^[0-9]{18}$/;
                var o = $scope.data.order;
                if(o.describe != null && o.describe != "") {
                    orderService.editOrder(o).then(function(response) { 
                        if (response.data.status == 4) {   //用户登录问题
                            $cookieStore.remove("token");
                            window.location = "./login.html"
                         } else if (response.data.status == 1){ //成功
                            $cookieStore.remove("token");
                            $cookieStore.put("token",response.data.token);
                            $scope.data.orderList = response.data.object;
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
                    alert("备注不合法");
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
                orderService.editMe2(form).then(function(response) {
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
                orderService.editMe1(form).then(function(response) {
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