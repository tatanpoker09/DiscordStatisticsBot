package com.eilers.tatanpoker09;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiscordUser {
    private long user_id;
    private String name;
    private Date date;

    public DiscordUser(long user_id, String name, Date date) {
        this.user_id = user_id;
        this.name = name;
        this.date = date;
    }

    public boolean saveToDatabase(Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DiscordUser(user_id, name, join_date) VALUES (?, ?, ?)");
            preparedStatement.setLong(1, user_id);
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, date);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
