app.controller("saleController", function ($cookieStore, $scope, saleService) {
    $scope.data = {
        sum: 0,   //合计
        numberId: "",  //会员id
        product: {},   //页内通信对象
        productList: [],  //商品列表
        user: {},  //用户
        control: {
            page: 0,   //当前页码
            modalTitle: "添加商品",  //模态框标题
            sale: true
        }
    }

    saleService.getMe().then(function (response) {
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

    //dyz
    $scope.getProduct = function (id, count) {
        saleService.getProduct({ "id": id, "count": count }).then(function (response) {
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html";
                alert(response.data.describe);
            } else if (response.data.status == 1) { //成功
                $cookieStore.remove("token");
                $cookieStore.put("token", response.data.token);
                if (response.data.object.length < 10) {
                    $scope.data.control.nextHide = true;
                }
                var p = response.data.object;
                if ((p.typeId == 1 && $scope.data.control.sale == false) || (p.typeId != 1 && $scope.data.control.sale == true)) {
                    var f = true;
                    for (var i = 0; i < $scope.data.productList.length; i++) {
                        if ($scope.data.productList[i].id == p.id) {
                            $scope.data.productList[i].count = $scope.data.productList[i].count + p.count;
                            f = false;
                            break;
                        }
                    }
                    if (f == true) {
                        $scope.data.productList.push(p)
                    }
                    $scope.data.sum  = $scope.data.sum + (p.outPrice * p.count);
                } else {
                    alert(p.name + "是" + (p.typeId == 1 ? "积分兑换":"正常销售") + "商品")
                }
            } else {
                alert(response.data.describe)
            }
        })
    }

    //dyz
    $scope.pay = function () {
        var pattern = /^[0-9]{11}$/
        if ($scope.data.control.sale == true && (pattern.test($scope.data.numberId) || ($scope.data.numberId == null || $scope.data.numberId == undefined ||
            $scope.data.numberId == ''))) {
            if ($scope.data.numberId == null || $scope.data.numberId == undefined ||
                $scope.data.numberId == '') {
                $scope.data.numberId = 1;
            }
            saleService.pay($scope.data.numberId, $scope.data.productList,$scope.data.control.sale).then(function (response) {
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html";
                    alert(response.data.describe);
                } else if (response.data.status == 1) { //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token", response.data.token);
                    window.location = "./index.html";
                } 
                alert(response.data.describe)
            })
        } else if($scope.data.control.sale == false && pattern.test($scope.data.numberId)){
            saleService.pay($scope.data.numberId, $scope.data.productList,$scope.data.control.sale).then(function (response) {
                if (response.data.status == 4) {   //用户登录问题
                    $cookieStore.remove("token");
                    window.location = "./login.html";
                    alert(response.data.describe);
                } else if (response.data.status == 1) { //成功
                    $cookieStore.remove("token");
                    $cookieStore.put("token", response.data.token);
                    window.location = "./index.html";
                } 
                alert(response.data.describe)
            })
        } else {
            alert("会员Id不合法");
        }
    }

    $scope.change = function() {
        $scope.data.control.sale = !$scope.data.control.sale
        $scope.data.productList = []
        $scope.data.sum = 0
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
                saleService.editMe2(form).then(function(response) {
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
                saleService.editMe1(form).then(function(response) {
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