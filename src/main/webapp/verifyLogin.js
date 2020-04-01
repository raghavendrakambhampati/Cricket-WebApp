$(document).ready(function(){
    $("#buttonId").on("click", function(){
        console.log("hi");
        var user        = $('#userId').val();
        var password    = $('#passwordId').val();
        $.post('LoginServlet', {
            userId : user,
            passwordId : password
        }, function(responseText) {
        console.log(responseText, userId, passwordId);
            if(responseText==="admin"){
            window.location.replace("adminLogin.html");
            }
            else if(responseText=="Owner"){
            window.location.replace("ownerLogin.html");
            }
            else if(responseText=="Player"){
            window.location.replace("playerLogin.html");
            }
            else {
                  alert(responseText);
                  window.location.replace("index.html");
                 }
        });
    });
});
