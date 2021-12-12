const mysql = require('mysql');
require('dotenv').config();
const connection = mysql.createConnection({
    host     : process.env.DB_HOST,
    user     : process.env.DB_USER || "host",
    password : process.env.DB_PASSWORD,
    database: process.env.DB_NAME
});
connection.connect(function(err) {
    if (err) throw err;
});
module.exports = connection;
