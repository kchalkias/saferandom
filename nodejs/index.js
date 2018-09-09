var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var Auth = require("./utils/auth");


var cors = require('cors');
app.use(cors());

app.use(bodyParser.urlencoded({
    extended: true
}));

// parse application/json
app.use(bodyParser.json());

//auth checker
app.post("/api/*", function(req, res, next){
    console.log("Api call", req.body);
    if(req.body && req.body.api_key){
        Auth.check(req.body.api_key)
            .then(function(){
                next();
            })
            .catch(function(){
                next();
            });
    }else{
        next();
    }
});

//register api calls.
app.post("/api/getContests", require("./express/api/getContests"));
app.post("/api/getContestDetails", require("./express/api/getContestDetails"));
app.post("/api/addContest", require("./express/api/addContest"));
app.post("/api/editContest", require("./express/api/editContest"));
app.post("/api/addParticipations", require("./express/api/addParticipations"));


//register angular server
var angularServer = require("./express/angular-server");
angularServer(app);

//catch errors
app.use(function(err, req, res, next){
    console.log("There was an error", err);
    res.status(500).send({error: true, details: err});
});
app.listen(8081);
