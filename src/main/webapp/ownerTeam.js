function loadData(){
var jsonData;
$.get('TeamsId', {
}, function(responseText){
    console.log(responseText);

    jsonData=JSON.parse(responseText)

    const app = document.createElement("div");
      app.setAttribute('id','root');
      document.body.appendChild(app);

  var data  = jsonData["OwnerTeam_data"];
   console.log(data);
  for(i=0;i<data.length;i++){
      console.log(data);
      var x = $.parseHTML(create_template(data[i]));
      app.appendChild(x[0]);
  }
});
}

function create_template(data){
    const template = `<div class="card">
        <div class="details">Name:${data.Name}</div>
        <div class="details">Id:${data.UserId}</div>
        <div class="details">Ph no:${data.MobileNumber}</div>
        <div class="details">Email:${data.EmailId}</div>
        <div class="details">Skills:${data.Skills}</div>
        <div class="details">Rating:${data.Rating}</div>
      </div>`;

     return template;
};