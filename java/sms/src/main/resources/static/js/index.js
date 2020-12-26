function checkFile(file) {
    var pattern = /^.*\.png$|^.*\.jpg$/
    console.log(file)
    if (pattern.test(file) != true) {
        $("#leading-in-head-btn").disabled
        alert("仅支持.jpg和.png格式的图片");
    } else {
        $("#leading-in-head-btn").attr("disabled",false)
    }
}