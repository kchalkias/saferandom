/**
 * Created by pavlos on 23/4/2016.
 */
var db = require("../db/DBPool"),
    Promise = require("promise"),
    Auth = {
        check: function(apiKey){
            return new Promise(function(res, rej){
                db.getConnection(function(error, con){
                    if(error) {
                        rej(error);
                        return;
                    }
                    con.query("Select * From `organizer` Where ?", {api_key: apiKey}, function(dbError, results, fields){
                        if(dbError){
                            con.release();
                            rej(dbError);
                            return;
                        }
                        console.log("Auth", results, Auth);
                        if(results.length){
                            Auth.loggedIn = true;
                            Auth.user = results[0];
                            res();
                        }else{
                            rej("Login")
                        }
                        con.release();
                    })
                });
            });
        },
        loggedIn: false,
        user: null
    };

module.exports = Auth;