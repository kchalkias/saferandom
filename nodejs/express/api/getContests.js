var dbPool = require("../../db/DBPool"),
    Auth = require("../../utils/auth");

module.exports = function(req, res, next){
    dbPool.getConnection(function(error, connection){
        if (error) return next(error);
        var post = req.body,
            onlyMine = post && post.mine == "true",
            searchTitle = post && post.keyword && post.keyword.trim().length ? post.keyword.trim() : null;
        console.log("Get contests", post, onlyMine, searchTitle, Auth.loggedIn);
        if (onlyMine && !Auth.loggedIn) {
            connection.release();
            return next("Login");
        }
        var query = 'SELECT c.*, o.`name` organizer_name FROM `contest` c, `organizer` o Where c.`organizer_id` = o.`id`\n',
            params = [];
        if(onlyMine){
            query+= "And `organizer_id` = ?";
            params.push(Auth.user.id);
        }
        if(searchTitle != null){
            query += " And title Like ?";
            params.push(searchTitle + "%");
        }
        console.log(query, params);
        connection.query(query, params, function(err, rows) {
            if (err) {
                connection.release();
                return next(err);
            }
            console.log('The rows: ', rows);
            rows.forEach(function(contest){
                contest.endtime = contest.endtime.getTime();
            });
            res.send(rows);
            connection.release();
        });
    });
};