const express = require('express');
const router = express.Router();
const db = require('../database');

/* GET users listing. */
router.get('/:username', function(req, res, next) {
  let username = req.params.username;
  db.query(`SELECT user_id, name, join_date FROM discorduser WHERE name='${username}'`, function (err, rows, fields) {
    if (err) {
      throw err;
    }
    if(rows[0]) {
      let date_since_join = new Date().getTime() - rows[0].join_date.getTime();
      res.status(200);
      res.send({"user_id": rows[0].user_id, "username": rows[0].name, "date_since_join": date_since_join});
    } else {
      res.status(400);
      res.send({"error": "Username not found"});
    }
  });
});


router.get('/:username/messagecount', function(req, res, next) {
  let username = req.params.username;
  db.query(`SELECT name, COUNT(messages.user_id) AS count FROM messages INNER JOIN discorduser on messages.user_id=discorduser.user_id WHERE discorduser.name='${username}'`,function(err, rows, fields) {
    if (err) throw err;
    res.status(200);
    res.send({"user": rows[0].name, "count": rows[0].count});
  });
});

module.exports = router;
