function login(){
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    let userInfo = {};

    userInfo.username = email;
    userInfo.password = password;

    $.ajax({
        type: "post",
        url: "/login",
        data: userInfo,
        success: function(data) {
            alert('성공');
            console.log(data);
            //location.href="/"
        },
        error: function(e){
            alert('error!!');
        }
    });
}