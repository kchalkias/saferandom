/**
 * Created by pavlos on 23/4/2016.
 */
var dbPool = require("../../db/DBPool");

module.exports = function(req, res, next){
    dbPool.getConnection(function(error, connection){
        if (error) return next(error);
        var post = req.body,
            contestID = post && post.contest_id ? parseInt(post.contest_id) : null;
        console.log("Get contest", post, contestID);
        if(contestID == null){
            connection.release();
            next("Missing contest id");
            return;
        }
        connection.query("Select * From `contest` Where `id` = ?", [contestID], function(err, rows) {
            if (err) {
                connection.release();
                return next(err);
            }
            console.log('The contest: ', rows);
            if(!rows.length){
                connection.release();
                next("Contest not found");
                return;
            }
            var contest = rows[0];
            contest.endtime = contest.endtime.getTime();
            connection.query("Select * From `participation` Where `contest_id` = ?", [contestID], function(err, rows){
                if (err) {
                    connection.release();
                    return next(err);
                }
                console.log('The participants: ', rows);
                contest.participations = rows;
                connection.query("Select `id`, `name` From `organizer` Where `id` = ?", [contest.organizer_id], function(err, rows){
                    if (err) {
                        connection.release();
                        return next(err);
                    }
                    console.log('The organizer: ', rows);
                    contest.organizer = rows[0];
                    res.send(contest);
                    connection.release();
                });
            });
        });
    });
};