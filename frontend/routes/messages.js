
var express = require('express');
var router = express.Router();
var db = require('../database');

/* GET home page. */
router.get('/date/all', async function(req, res, next) {
    let dates = [];
    db.query("SELECT DATE(date) AS date, COUNT(DATE(date)) AS count FROM messages GROUP BY DATE(date)",function(err, rows, fields) {
        if (err) throw err;
        rows.forEach((row)=>{
            row.date = formatDate(row.date);
            dates.push({"date":row.date, "count": row.count});
        });
        res.send(dates);
    });
});

router.get('/date/:username', async function(req, res, next) {
    let username = req.params.username;
    let dates = [];
    db.query(`SELECT DATE(date) AS date, COUNT(DATE(date)) AS count FROM messages INNER JOIN discorduser ON messages.user_id=discorduser.user_id WHERE discorduser.name='${username}' GROUP BY DATE(date)`,function(err, rows, fields) {
        if (err) throw err;
        rows.forEach((row)=>{
            row.date = formatDate(row.date);
            dates.push({"date":row.date, "count": row.count});
        });
        res.send(dates);
    });
});

router.get('/leaderboard', async function(req, res, next) {
    let entries = [];
    db.query(`SELECT COUNT(messages.user_id) AS count, name FROM messages INNER JOIN discorduser ON discorduser.user_id=messages.user_id GROUP BY messages.user_id ORDER BY COUNT(messages.user_id) DESC LIMIT 10`,function(err, rows, fields) {
        if (err) throw err;
        rows.forEach((row)=>{
            entries.push({"username":row.name, "message_count": row.count});
        });
        res.send(entries);
    });
});


function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

module.exports = router;
