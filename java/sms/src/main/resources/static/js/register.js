function confirmPassword() {
    if ($("#password").val().equals($("#password1").val())) {
        return true;
    } else {
        alert("两次密码输入不一致");
        return false;
    }
}