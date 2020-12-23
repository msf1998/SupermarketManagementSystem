function md5EncodePassword() {
    $(".password").val(md5($(".password").val()));
    return true;
}