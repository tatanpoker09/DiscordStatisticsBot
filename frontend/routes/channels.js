var express = require('express');
var router = express.Router();
var db = require('../database');

/* GET home page. */
router.get('/all', async function(req, res, next) {
    let channels = [];
    db.query("SELECT name from discordchannels",function(err, rows, fields) {
        if (err) throw err;
        rows.forEach((row)=>{
           channels.push({"name":row.name});
        });
        res.send(channels);
    });
});

module.exports = router;
