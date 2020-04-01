$(document).ready(function(){
$("#logoutButton").on("click", function(){
console.log("Bye");
$.get('LogoutServlet', {
}, function(responseText) {
    if(responseText.includes("<html>") || responseText != null){
        window.location.replace("index.html");
    }
},);
});
});