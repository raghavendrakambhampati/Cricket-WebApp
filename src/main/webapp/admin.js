function loadData(){
var jsonData;
$.get('teams/', {
}, function(responseText){
    console.log(responseText);

    jsonData=JSON.parse(responseText)

    const app = document.createElement("div");
      app.setAttribute('id','root');
      document.body.appendChild(app);

  var data  = jsonData["Teams_data"];
   console.log(data);
  for(i=0;i<data.length;i++){
      console.log(data);
      var x = $.parseHTML(create_template(data[i]));
      app.appendChild(x[0]);
  }
});
};

function create_template(data){
    const template = `<div class="card">
        <div class="details">Team Name:${data.TeamName}</div>
        <div class="details">Id:${data.UserId}</div>
        <div class="details">City:${data.city}</div>
        <button class="teamDetailsButton" onclick="TeamDetails(${data.UserId})">Get Details</button>
      </div>`;

     return template;
};
function TeamDetails(UserId){
        var user=UserId;
        $.post('PlayerServlet', {
            userId : user,
        },
  );
window.location.replace("ownerTeam.html");
}


