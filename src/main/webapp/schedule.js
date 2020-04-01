function LoadTodaysMatchTeams(){
    var teamsNames;
    $.get('schedule/today/',{},function(responseText){
        console.log(responseText);
        teamsNames = JSON.parse(responseText);
         var data = teamsNames["Todays_Teams"];

         const app = document.createElement("div");
         app.setAttribute('id','root');
         document.body.appendChild(app);
        var id =1;
        for(i=0;i<data.length;i++){
            console.log(data[i]);
            var teamCard = $.parseHTML(create_template(data[i],id));
            app.appendChild(teamCard[0]);
            id=id+1;
        }
        var scorecard = $.parseHTML(`<div class="scorecard">
        <button class ="toss" onclick="toss();this.disabled=true;this.style.display='none';">Toss</button>
        </div>`);
        document.body.appendChild(scorecard[0]);
    });
  
};

function create_template(data,id){
    console.log(id);
    const template = `<div class="matchCard" id=${id}>
        <div class="info">Team Name:${data.TeamName}</div>
        <div class="info">Team Id:${data.TeamId}</div>
        <button class="infoButton" onclick="TeamDetails( ${data.TeamId},${id});this.disabled=true;this.style.display='none';">Get Team</button>
        <button class ="infoButton" onclick="createScoreCard(${data.TeamId},${id});this.disabled=true;this.style.display='none';">Update score</button>
      </div>`;

     return template;
};

function TeamDetails(teamId,id){
    var jsonData;
    $.get('TeamMembers',{
        teamId : teamId
    },function(responseText){
        console.log(responseText);

        jsonData=JSON.parse(responseText)

    const app1 = document.createElement("div");
      app1.setAttribute('id',teamId);
      
    var data  = jsonData["OwnerTeam_data"];
    console.log(data);
    for(i=0;i<data.length;i++){
      
      console.log(data);
      var x = $.parseHTML(create_template1(data[i]));
      app1.appendChild(x[0]);
  }
  document.getElementById(id).appendChild(app1);
    });
};

function create_template1(data){
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

function toss(){
    const tossed = $.parseHTML(`<form method="POST" action="innings">
    FirstInnings  : <input placeholder="First Innings" type="text" name="FirstInnings"  required ><br/>
    SecondInnings : <input placeholder="Second Innings" type="text" name="SecondInnings"  required ><br/>
    <button class="loginButton" id="buttonId">Submit</button>
</form>
    </div>`);
    console.log(tossed);
    document.getElementsByClassName("toss")[0].replaceWith(tossed[0]);
};

function createScoreCard(teamId,id){
    console.log(teamId);
    const div = $.parseHTML(`<form method ="POST" action = "Score">
    <div class="scorecard">
        <br><div class = "score">  Score : <input type = "text" name = "score" required ></input></div></br>
        <br><div class = "wickets">Wickets : <input type = "text" name = "wickets" required ></input></div></br>
        <input style="display:none" type = "text" name = "TeamId" value="${teamId}" required ></input>
        <button class="loginButton" id="buttonId1">Submit</button>
    </div>
    </form>`);

    document.getElementById(id).appendChild(div[0]);
};

function getSchedule(){
$.get('schedule/',{},function(responseText){
    console.log(responseText);
    
     if(responseText==="No Matches"){
            const app = document.createElement('div');
            app.setAttribute('id','no_matches');
            document.body.appendChild(app);
            document.getElementById("no_matches").innerHTML="NO Matches";
     }
    else{
        const app = document.createElement('div');
        app.setAttribute('id','root1');
        document.body.appendChild(app);
        var teams =  JSON.parse(responseText);
        for(i=0;i<teams.length;i++){
            var team = $.parseHTML(createMatchCard(teams[i]));
            app.appendChild(team[0]);
        }
    }
});
};

function createMatchCard(team){
    const template =`<div class = "scheduleCard">
        <div class = "details">Match_ID : ${team.MATCH_ID}</div>
        <div class = "details">${team.Team1_Name} VS ${team.Team2_Name}</div>
        <div class = "details">${team.Match_Date}</div>
    </div>`;
    return template;
};