/**
 * Created by pavlos on 24/4/2016.
 */
/**
 * Created by pavlos on 23/4/2016.
 */
var dbPool = require("../../db/DBPool"),
    Auth = require("../../utils/auth");
const crypto = require('crypto');

module.exports = function(req, res, next){
    dbPool.getConnection(function(error, connection){
        if (error) {
            return next(error);
        }
        if (!Auth.loggedIn) {
            connection.release();
            return next("Login");
        }
        //TODO:add checks
        var post = req.body,
            partipations = post.participations,
            forContestId = post.cid;
        connection.beginTransaction(function(err) {
            if (err) {
                connection.release();
                return next(err);
            }
            insertParticipations(forContestId, partipations, connection, function(success){
                if(success){
                    connection.commit(function(err) {
                        if (err) {
                            return connection.rollback(function() {
                                connection.release();
                                next(err);
                            });
                        }
                        connection.release();
                        console.log('success!');
                        res.send({error: false});
                    });
                }else{
                    connection.release();
                    return next("Failed inserting all participations");
                }
            });
        });
    });
};

/**
 * Get lower case sha256 hash from msg
 * @param msg
 */
function getSha256HEX(msg){
    msg = msg.toString();
    var hash = crypto.createHash('sha256')
        .update(msg)
        .digest('hex');
    hash = hash.toLowerCase();
    console.log("Sha256", msg, hash);
    return hash;
}

function insertParticipations(forContestId, partisipations, connection, completedFN){
    var pData = partisipations.pop();
    if(pData){
        var params = [forContestId, pData.id, pData.title, getSha256HEX(pData.id), forContestId, Auth.user.id];
        console.log("Pdata:", pData, params);
        connection.query("Insert Ignore `participation`(`contest_id`, `identifier`, `title`, `seed`)" +
            " Select ?, ?, ?, ? From `contest` Where `id` = ? And `organizer_id` = ? And `endtime` > Now() And `state` = 0", params, function(error, result){
            if(error){
                console.log(error);
                completedFN(false);
            }else{
                console.log(result);
                insertParticipations(forContestId, partisipations, connection, completedFN);
            }
        });
    }else{
        completedFN(true);
    }
}