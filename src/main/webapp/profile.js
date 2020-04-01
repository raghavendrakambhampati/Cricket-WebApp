function loadData(){
var jsonData;
$.get('Profile', {
}, function(responseText){
    console.log(responseText);

    jsonData=JSON.parse(responseText)
   console.log(jsonData);
      var userProfile = $.parseHTML(create_template(jsonData));
      console.log(userProfile);
      document.getElementById('profile').append(userProfile[0]);
      document.getElementById('profile').append(userProfile[2]);
      document.getElementById('profile').append(userProfile[4]);
      document.getElementById('profile').append(userProfile[6]);
      document.getElementById('profile').append(userProfile[8]);
});
}

function create_template(data){
console.log(data);
    const template = `<img class="profileImg" src="profileImg.jpg">
        <div class="profileDetails">Name:${data.Name}</div>
        <div class="profileDetails">Id:${data.UserId}</div>
        <div class="profileDetails">Email:${data.Email}</div>
        <div class="profileDetails">Skills:${data.Skills}</div>`;

     return template;
};