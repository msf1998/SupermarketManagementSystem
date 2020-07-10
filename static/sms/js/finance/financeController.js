var data = []
app.controller("financeController",function($cookieStore,$scope,financeService) {
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
    financeService.getMe().then(function(response) {
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
    financeService.getFinanceChart().then(function(response) {
        if (response.data.status == 4) {   //用户登录问题
            $cookieStore.remove("token");
            window.location = "./login.html";
            alert(response.data.describe);
         } else if (response.data.status == 1){ //成功
            $cookieStore.remove("token");
            $cookieStore.put("token",response.data.token);
            var Script = function () {

                //morris chart
            
                $(function () {
                  var tax_data = response.data.object;
                  Morris.Line({
                    element: 'hero-graph',
                    data: tax_data,
                    xkey: 'period',
                    ykeys: ['licensed', 'sorned'],
                    labels: ['Licensed', 'Off the road'],
                    lineColors:['#4ECDC4','#ed5565']
                  });
            
                });
            
            }();         
         } else {
            alert(response.data.describe)
         }
    })
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
                financeService.editMe2(form).then(function(response) {
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
                financeService.editMe1(form).then(function(response) {
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
    $scope.getFinanceChart = function() {
        financeService.getFinanceChart().then(function(response) {
            if (response.data.status == 4) {   //用户登录问题
                $cookieStore.remove("token");
                window.location = "./login.html";
                alert(response.data.describe);
             } else if (response.data.status == 1){ //成功
                $cookieStore.remove("token");
                $cookieStore.put("token",response.data.token);
                data = response.data.object;
             } else {
                alert(response.data.describe)
             }
        })
    }
})
