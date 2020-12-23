app.controller("monitorController",function($cookieStore,$scope,monitorService) {
    $scope.data = {
        product: {},   //页内通信对象
        product1List: [],  //商品列表
        product2List: [],  //商品列表
        user: {},  //用户
        control: {
            page: 0,   //当前页码
            modalTitle: "添加商品",  //模态框标题
            prevHide: true,  //上一页按钮隐藏
            nextHide: false, //下一页按钮隐藏
            tyepId:null,
        }
    }
    monitorService.getMe().then(function(response) {
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
    monitorService.listProduct1().then(function(response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
         } else if (response.data.status == 1){ //成功
            //console.log(response)
            $cookieStore.remove("token");
            $cookieStore.put("token",response.data.token);
            if (response.data.object.length < 10) {
                $scope.data.control.nextHide = true;
            }
            $scope.data.product1List = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    monitorService.listProduct2().then(function(response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
         } else if (response.data.status == 1){ //成功
            //console.log(response)
            $cookieStore.remove("token");
            $cookieStore.put("token",response.data.token);
            if (response.data.object.length < 10) {
                $scope.data.control.nextHide = true;
            }
            $scope.data.product2List = response.data.object;
         } else {
            alert(response.data.describe)
         }
         
    })
    //lzc
    $scope.submitProduct = function() {
        if ($scope.data.user.role.productUpdate != true) {
            alert("抱歉,您没有此权限");
        } else {
            var p = $scope.data.product;
            p.typeId = 2;
            var parent1 = /^\d+$/;
            var parent2 = /^\d+\.\d+$/;
            var msg = "";
            if (parent1.test(p.id)) {
                if (p.name != null && p.name != '') {
                    if (p.manufacturer != null && p.manufacturer != '') {
                            if(parent1.test(p.selfLife)) {
                                if (parent1.test(p.warnBefore)) {
                                    if (parent1.test(p.count)) {
                                        if (parent1.test(p.warnCount)) {
                                            if (parent2.test(p.inPrice) || parent1.test(p.inPrice)) {
                                                if (parent2.test(p.outPrice) || parent1.test(p.outPrice)) {
                                                     monitorService.editProduct(p).then(function(response) { 
                                                        if (response.data.status == 4) {   //用户登录问题
                                                            $cookieStore.remove("token");
                                                            window.location = "./login.html"
                                                         } else if (response.data.status == 1){ //成功
                                                            $cookieStore.remove("token");
                                                            $cookieStore.put("token",response.data.token);
                                                            $scope.data.product2List = response.data.object;
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
                                                    alert("售价不合法");
                                                }
                                            } else {
                                                alert("进价不合法");
                                            }
                                        } else {
                                            alert("提醒库存不合法");
                                        }
                                    } else {
                                        alert("库存数量不合法");
                                    }
                                } else {
                                    alert("提醒日期不合法"); 
                                }
                            } else {
                                alert("保质期不合法"); 
                            }
                    } else {
                        alert("供货商不合法"); 
                    }
                } else {
                    alert("商品名称不合法"); 
                }
            } else {
                alert("商品id不合法");
            }
        }
    }

    $scope.editProduct = function(p) {
        $scope.data.product = p;
        $scope.data.control.modalTitle = "修改商品信息";
    }
    $scope.deleteProduct2 = function(p) {
        p.count = 0;
        monitorService.editProduct(p).then(function(response) { 
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html"
             } else if (response.data.status == 1){ //成功
                $cookieStore.remove("token");
                $cookieStore.put("token",response.data.token);
                $scope.data.product2List = response.data.object;
                $scope.data.control.prevHide = true;
                $scope.data.control.page = 0;
                if (response.data.object.length < 10) {
                    $scope.data.control.nextHide = true;
                } else {
                    $scope.data.control.nextHide = false;
                }
                monitorService.listProduct1().then(function(response) {
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
                        $scope.data.product1List = response.data.object;
                     } 
                })
             } 
             alert(response.data.describe)
         })
    }
    //删除商品
    //lzc
    $scope.deleteProduct1 = function(p) {
        monitorService.deleteProduct(p.id).then(function(response) {
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html"
             } else if (response.data.status == 1){ //成功
                $cookieStore.remove("token");
                $cookieStore.put("token",response.data.token);
                $scope.data.product1List = response.data.object;
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
    //导出进货单
    //lzc
    $scope.createExcel = function() {
        monitorService.createExcel().then(function(response) {
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html"
             } else if (response.data.status == 1){ //成功
                $cookieStore.remove("token");
                $cookieStore.put("token",response.data.token);
                monitorService.listProduct1().then(function(response) {
                    if (response.data.status == 4) {   //用户登录问题
                        $cookieStore.remove("token");
                        window.location = "./login.html";
                        alert(response.data.describe);
                     } else if (response.data.status == 1){ //成功
                        //console.log(response)
                        $cookieStore.remove("token");
                        $cookieStore.put("token",response.data.token);
                        if (response.data.object.length < 10) {
                            $scope.data.control.nextHide = true;
                        }
                        $scope.data.product1List = response.data.object;
                     } else {
                        alert(response.data.describe)
                     }
                     
                })
                window.open("http://localhost:8888/sms/product/excel/leading-out/" + response.data.object,"_blank")
            } else {
                alert(response.data.describe)
            }
        })
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
                monitorService.editMe2(form).then(function(response) {
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
                monitorService.editMe1(form).then(function(response) {
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