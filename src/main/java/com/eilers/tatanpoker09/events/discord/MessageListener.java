package com.eilers.tatanpoker09.events.discord;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
            OffsetDateTime creationTime = message.getCreationTime();
            Timestamp date = new Timestamp(creationTime.toInstant().toEpochMilli());
            saveMessage(message.getId(), message.getAuthor().getId(), date, contentRaw, event.getChannel().getId());
    }

    private void saveMessage(String messageId, String userId, Timestamp date, String message, String channelId) {
        BotStatusListener.saveMessageToDatabase(messageId, userId, date, message, channelId, connection);
    }
}
