app.controller("productController", function ($cookieStore, $scope, productService, typeService) {
    $scope.data = {
        product: {},   //页内通信对象
        productList: [],  //商品列表
        user: {},  //用户
        typeList: [], //类别列表
        control: {
            page: 0,   //当前页码
            modalTitle: "添加商品",  //模态框标题
            prevHide: true,  //上一页按钮隐藏
            nextHide: false, //下一页按钮隐藏
            tyepId: null,
        }
    }
    typeService.listType({ "order": "id" }).then(function (response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
        } else if (response.data.status == 1) { //成功
            $cookieStore.remove("token");
            $cookieStore.put("token", response.data.token);
            $scope.data.typeList = response.data.object;
        } else {
            alert(response.data.describe)
        }
    })
    productService.getMe().then(function (response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
        } else if (response.data.status == 1) { //成功
            $cookieStore.remove("token");
            $cookieStore.put("token", response.data.token);
            $scope.data.user = response.data.object;
        } else {
            alert(response.data.describe)
        }
    })
    $scope.data.product = { "order": "name", "page": $scope.data.control.page, typeId: $scope.data.control.typeId };
    productService.listProduct($scope.data.product).then(function (response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
        } else if (response.data.status == 1) { //成功
            //console.log(response)
            $cookieStore.remove("token");
            $cookieStore.put("token", response.data.token);
            if (response.data.object.length < 10) {
                $scope.data.control.nextHide = true;
            }
            $scope.data.productList = response.data.object;
        } else {
            alert(response.data.describe)
        }

    })
    $scope.listProduct = function (x) {
        if ($scope.data.user.role.productSelect != true) {
            alert("抱歉,您没有此权限");
        } else {
            if (x == -1) {
                $scope.data.control.page = $scope.data.control.page - 1;
                $scope.data.control.nextHide = false;
            } else if (x == 1) {
                $scope.data.control.page = $scope.data.control.page + 1;
                $scope.data.control.prevHide = false;
            } else {
                $scope.data.control.page = 0;
            }
            if ($scope.data.control.page <= 0) {
                $scope.data.control.prevHide = true;
            }
            $scope.data.product = { "order": "name", "page": $scope.data.control.page, typeId: $scope.data.control.typeId };
            productService.listProduct($scope.data.product).then(function (response) {
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html";
                    alert(response.data.describe);
                } else if (response.data.status == 1) { //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token", response.data.token);
                    if (response.data.object.length < 10) {
                        $scope.data.control.nextHide = true;
                    } else {
                        $scope.data.control.nextHide = false;
                    }
                    $scope.data.productList = response.data.object;
                } else {
                    alert(response.data.describe);
                }
            })
        }
    }
    //lzc
    $scope.addProduct = function () {
        $scope.data.control.modalTitle = "添加商品";
        $scope.data.product = {}
    }
    //lzc
    $scope.submitProduct = function () {
        if ($scope.data.control.modalTitle == "添加商品") {
            if ($scope.data.user.role.productInsert != true) {
                alert("抱歉,您没有此权限");
            } else {
                var p = $scope.data.product;
                var parent1 = /^\d+$/;
                var parent2 = /^\d+\.\d+$/;
                var msg = "";
                if (parent1.test(p.id)) {
                    if (p.name != null && p.name != '') {
                        if (p.typeId != null && p.typeId != undefined && p.typeId >= 1 && p.typeId != '') {
                            if (p.manufacturer != null && p.manufacturer != '') {
                                if (p.productDate != null && p.productDate != '') {
                                    if (parent1.test(p.selfLife)) {
                                        if (parent1.test(p.warnBefore)) {
                                            if (parent1.test(p.count)) {
                                                if (parent1.test(p.warnCount)) {
                                                    if (parent2.test(p.inPrice) || parent1.test(p.inPrice)) {
                                                        if (parent2.test(p.outPrice) || parent1.test(p.outPrice)) {
                                                            productService.addProduct(p).then(function (response) {
                                                                if (response.data.status == 4) {   //用户登录问题
                                                                    $cookieStore.remove("token");
                                                                    window.location = "./login.html"
                                                                } else if (response.data.status == 1) { //成功
                                                                    $cookieStore.remove("token");
                                                                    $cookieStore.put("token", response.data.token);
                                                                    $scope.data.productList = response.data.object;
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
                                    alert("生产日期不合法");
                                }
                            } else {
                                alert("供货商不合法");
                            }
                        } else {
                            alert("商品类型不合法");
                        }
                    } else {
                        alert("商品名称不合法");
                    }
                } else {
                    alert("商品id不合法");
                }
            }
        } else {
            if ($scope.data.user.role.productUpdate != true) {
                alert("抱歉,您没有此权限");
            } else {
                var p = $scope.data.product;
                var parent1 = /^\d+$/;
                var parent2 = /^\d+\.\d+$/;
                var msg = "";
                if (parent1.test(p.id)) {
                    if (p.name != null && p.name != '') {
                        if (p.typeId != null && p.typeId != undefined && p.typeId >= 1 && p.typeId != '') {
                            if (p.manufacturer != null && p.manufacturer != '') {
                                if (p.productDate != null && p.productDate != '') {
                                    if (parent1.test(p.selfLife)) {
                                        if (parent1.test(p.warnBefore)) {
                                            if (parent1.test(p.count)) {
                                                if (parent1.test(p.warnCount)) {
                                                    if (parent2.test(p.inPrice) || parent1.test(p.inPrice)) {
                                                        if (parent2.test(p.outPrice) || parent1.test(p.outPrice)) {
                                                            productService.editProduct(p).then(function (response) {
                                                                if (response.data.status == 4) {   //用户登录问题
                                                                    $cookieStore.remove("token");
                                                                    window.location = "./login.html"
                                                                } else if (response.data.status == 1) { //成功
                                                                    $cookieStore.remove("token");
                                                                    $cookieStore.put("token", response.data.token);
                                                                    $scope.data.productList = response.data.object;
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
                                    alert("生产日期不合法");
                                }
                            } else {
                                alert("供货商不合法");
                            }
                        } else {
                            alert("商品类型不合法");
                        }
                    } else {
                        alert("商品名称不合法");
                    }
                } else {
                    alert("商品id不合法");
                }
            }
        }
    }
    //lzc
    $scope.deleteProduct = function (p) {
        if ($scope.data.user.role.productDelete != true) {
            alert("抱歉,您没有该权限");
        } else {
            productService.deleteProduct(p.id).then(function (response) {
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html"
                } else if (response.data.status == 1) { //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token", response.data.token);
                    $scope.data.productList = response.data.object;
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
    $scope.editProduct = function (p) {
        $scope.data.product = p;
        $scope.data.control.modalTitle = "修改商品信息";
    }
    //上传文件
    $scope.leadingInPurchase = function () {
        var form = new FormData();
        var file = $("#leading-in-purchase")[0].files[0];
        form.append("file", file);
        productService.leadingInPurchase(form).then(function (response) {
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html";
                alert(response.data.describe);
            } else if (response.data.status == 1) { //成功
                //console.log(response)
                $cookieStore.remove("token");
                $cookieStore.put("token", response.data.token);
                if (response.data.object.length < 10) {
                    $scope.data.control.nextHide = true;
                }
                $scope.data.productList = response.data.object;
                alert(response.data.describe)
            } else {
                alert(response.data.describe)
            }
        })
    }
    $scope.leadingInPhoto = function () {
        var form = new FormData();
        var file = $("#leading-in-photo")[0].files[0];
        form.append("file", file);
        form.append("id", $scope.data.product.id);
        productService.leadingInPhoto(form).then(function (response) {
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html";
                alert(response.data.describe);
            } else if (response.data.status == 1) { //成功
                //console.log(response)
                $cookieStore.remove("token");
                $cookieStore.put("token", response.data.token);
                if (response.data.object.length < 10) {
                    $scope.data.control.nextHide = true;
                }
                $scope.data.productList = response.data.object;
                alert(response.data.describe)
            } else {
                alert(response.data.describe)
            }
        })
    }
    $scope.leadingInPhotoModal = function (p) {
        $scope.data.product = p;
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
                productService.editMe2(form).then(function(response) {
                    if (response.data.status == 4) {   //用户登录问题
                        $cookieStore.remove("token");
                        window.location = "./login.html";
                        alert(response.data.describe);
                    } else if (response.data.status == 1) { //成功
                        $cookieStore.remove("token");
                        $cookieStore.put("token", response.data.token);
                        $scope.data.user = response.data.object;
                        alert(response.data.describe)
                    } else {
                        alert(response.data.describe)
                    }
                })
            } else {
                productService.editMe1(form).then(function(response) {
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