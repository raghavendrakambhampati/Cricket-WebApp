function loadPlayers(){
var jsonData;
$.get('users/', {}, function(responseText){
console.log(responseText);

jsonData=JSON.parse(responseText)
var data = jsonData["Players_Details"];
console.log(data);

const app = document.createElement("div");
app.setAttribute('id','root');
document.body.appendChild(app);


for(i=0;i<data.length;i++){
var x = $.parseHTML(create_template(data[i]));
app.appendChild(x[0]);
}
});

};

function create_template(data){
const template = `<div class="card">
<img class="profileImg" src="profileImg.jpg">
<div class="details" style="float:right;text-align:center;margin-top:-8px;margin-right:150px;">
<div style="font-size:18px;margin-bottom:10px">Id: ${data.UserId}</div>
<div style="font-size:18px;margin-bottom:10px">Name: ${data.Name}</div>
<div style="font-size:18px;margin-bottom:10px">Skills: ${data.Skills}</div>
<input class="rating" id="${data.UserId}" type="number" placeholder="Provide Rating Here" name="rating" min="1" max="5">
<input type="button" onclick="cli(${data.UserId})" class="ratingButton" value="Submit">
</div>`;

return template;
};

function cli(userId){
var rating=document.getElementById(userId).value;
$.post('Rating', {
userId : userId,
rating : rating
});
};
