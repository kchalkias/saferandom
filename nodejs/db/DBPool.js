/**
 * Created by pavlos on 23/4/2016.
 */
var mysql = require('mysql');
var pool  = mysql.createPool({
    host     : '144.76.234.83',
    user     : 'nodejs-user',
    password : '45RZEZVrd8xcgQgPKrMY',
    database : 'safe_random'
});
module.exports = pool;