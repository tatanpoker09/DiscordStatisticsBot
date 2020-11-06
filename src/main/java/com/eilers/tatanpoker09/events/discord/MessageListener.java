package com.eilers.tatanpoker09.events.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class MessageListener extends ListenerAdapter {
    private final Connection connection;

    public MessageListener(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
            Message message = event.getMessage();
            String contentRaw = message.getContentRaw();
            if (contentRaw.equals("")) {
                contentRaw = "$PHOTO_CONTENT$";
            }
            OffsetDateTime creationTime = message.getTimeCreated();
            Timestamp date = new Timestamp(creationTime.toInstant().toEpochMilli());
            saveMessage(message.getId(), message.getAuthor().getId(), date, contentRaw, event.getChannel().getId());
    }

    private void saveMessage(String messageId, String userId, Timestamp date, String message, String channelId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages(message_id, user_id, date, message, channel_id) VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, messageId);
            preparedStatement.setString(2, userId);
            preparedStatement.setTimestamp(3, date);
            preparedStatement.setString(4, message);
            preparedStatement.setString(5, channelId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}