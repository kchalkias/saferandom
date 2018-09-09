/**
 * Created by pavlos on 23/4/2016.
 */
/**
 * Created by pavlos on 23/4/2016.
 */
var dbPool = require("../../db/DBPool"),
    Auth = require("../../utils/auth");

module.exports = function (req, res, next) {
    dbPool.getConnection(function (error, connection) {
        if (error) return next(error);

        if (!Auth.loggedIn) {
            connection.release();
            return next("Login");
        }
        //TODO:add checks
        var post = req.body,
            contest = {
                id: post.id,
                title: post.title,
                state: post.state,
                type: post.type,
                endtime: new Date(parseInt(post.endTime))//End time is timestamp in millis
            };
        console.log("Update contest", post, contest);
        connection.query("Update `contest` Set " +
            " `title` = ?," +
            " `state` = ?," +
            " `type` = ?," +
            " `endtime` = ?" +
            " Where `id` = ? And `organizer_id` = ?",
            [
                contest.title,
                contest.state,
                contest.type,
                contest.endtime,
                contest.id,
                Auth.user.id
            ], function (err, result) {
                if (err) {
                    connection.release();
                    return next(err);
                }
                console.log('Update result: ', result);
                res.send({error: false, rowsAffected: result.affectedRows});
                connection.release();
            });
    });
};