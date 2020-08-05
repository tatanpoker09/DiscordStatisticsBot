package com.eilers.tatanpoker09;

import net.dv8tion.jda.core.entities.TextChannel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class DiscordChannel {
    private long channel_id;
    private String name;
    private final String topic;
    private final String category;
    private Date date;

    public DiscordChannel(TextChannel textChannel){
        this.channel_id = textChannel.getIdLong();
        this.name = textChannel.getName();
        this.topic = textChannel.getTopic();
        this.category = textChannel.getParent().getName();
        long millis = textChannel.getCreationTime().toInstant().toEpochMilli();
        this.date = new Date(millis);
    }


    public DiscordChannel(long user_id, String name, String topic, String category, Date date) {
        this.channel_id = user_id;
        this.name = name;
        this.topic = topic;
        this.category = category;
        this.date = date;
    }

    public boolean saveToDatabase(Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DiscordChannels(channel_id, name, topic, category, creation_time) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setLong(1, channel_id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, topic);
            preparedStatement.setString(4, category);
            preparedStatement.setDate(5, date);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}