function loadMyTeam(){
var jsonData;
$.get('PlayerServlet',{}
,function(responseText){
    console.log(responseText);
    jsonData = JSON.parse(responseText);

    const app1 = document.createElement('div');
    app1.setAttribute('id','teamName');
    document.body.appendChild(app1);

    const app = document.createElement('div');
    app.setAttribute('id','root');
    document.body.appendChild(app);

    var data = jsonData["Players_Team"];
    console.log(data);
    var teamName = $.parseHTML(createHeader(data[0].Team));
    app1.appendChild(teamName[0]);

    for(i=0;i<data.length;i++){
        console.log(data);
        var x = $.parseHTML(createTemplate(data[i]));

        app.appendChild(x[0]);
    }
    
});
}

function createHeader(teamName){
 const template = `<div class = "head">
        <h1>TEAM:${teamName}</h1>
 </div>`;
 return template;
};
function createTemplate(data){
    const template = `<div class="card">
        <div class="details">Name:${data.Name}</div>
        <div class="details">Id:${data.UserId}</div>
        <div class="details">Ph No:${data.MobileNumber}</div>
        <div class="details">Email:${data.EmailId}</div>
        <div class="details">Skills:${data.Skills}</div>
        <div class="details">Rating:${data.Rating}</div>
      </div>`;

     return template;
};