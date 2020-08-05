package com.eilers.tatanpoker09.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase extends Database {
    private final String ip;
    private final String database;
    private final String username;
    private final String password;
    private final String port;

    public MySQLDatabase(String ip, String database, String username, String password, String port){
        this.ip = ip;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public Connection connect() {
        try {
            return DriverManager.getConnection(
                    String.format("jdbc:mysql://%s:%s/%s", ip, port, database),username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
