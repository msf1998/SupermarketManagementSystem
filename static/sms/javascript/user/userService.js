app.service('userService',function($http){
    this.login=function(username,userpassword){
        alert("service:"+username+userpassword);
        return $http.get("http://localhost:8080/login?"+"username="+username+"&userpassword"+userpassword);
    }
});