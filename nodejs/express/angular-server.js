/**
 * Created by pavlos on 23/4/2016.
 */
var express = require("express");
module.exports = function(app){
    var path = require("path");
    var dir = path.join(__dirname, '/../../front_end/');
    console.log("Angular directory is: " + dir);
    app.use(express.static(dir));
    app.all('*', function (req, res) {
        res.sendFile(path.join(dir, 'index.html'));
    });
};