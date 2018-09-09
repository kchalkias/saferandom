/**
 * Created by pavlos on 23/4/2016.
 */
var dbPool = require("../../db/DBPool"),
    Auth = require("../../utils/auth");

module.exports = function(req, res, next){
    dbPool.getConnection(function(error, connection){
        if (error) return next(error);

        if (!Auth.loggedIn) {
            connection.release();
            return next("Login");
        }
        //TODO:add checks
        var post = req.body,
            contest = {
                title: post.title,
                organizer_id: Auth.user.id,
                state: 0,
                type: post.type,
                endtime: new Date(parseInt(post.endTime))//End time is timestamp in millis
            };
        console.log("Insert contest", post, contest);
        connection.query("Insert Into `contest` Set ?", contest, function(err, result) {
            if (err) {
                connection.release();
                return next(err);
            }
            console.log('Insert result: ', result);
            if(result.affectedRows && result.insertId){
                res.send({error:false, contestId: result.insertId});
            }else{
                res.send({error:true, type:"Adding contest failed"});
            }
            connection.release();
        });
    });
};