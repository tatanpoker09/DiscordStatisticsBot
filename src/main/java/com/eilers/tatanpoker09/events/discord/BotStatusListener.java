package com.eilers.tatanpoker09.events.discord;

import com.eilers.tatanpoker09.BotStatus;
import com.eilers.tatanpoker09.DiscordBot;
import com.eilers.tatanpoker09.DiscordChannel;
import com.eilers.tatanpoker09.DiscordUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class BotStatusListener extends ListenerAdapter {
    private static final int MAX_RETRIEVE_SIZE = 1000;
    private Connection connection;
    private DiscordBot discordBot;
    private int duplicateKeyCount;
    private int messageCount;

    public BotStatusListener(Connection connection, DiscordBot discordBot) {
        this.connection = connection;
        this.discordBot = discordBot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        saveMessagesToDatabase(event);
    }

    private void saveMessagesToDatabase(ReadyEvent event) {
        discordBot.BOT_STATUS = BotStatus.SAVING_MESSAGES;
        System.out.println("Saving messages");
        duplicateKeyCount = 0;
        messageCount = 0;
        JDA bot = event.getJDA();
        List<TextChannel> textChannels = bot.getTextChannels();
        for (TextChannel textChannel : textChannels) {
            DiscordChannel discordChannel = new DiscordChannel(textChannel);
            discordChannel.saveToDatabase(connection);
            if(textChannel.getIdLong()==656019311206465565L){
                saveUsers(textChannel);
                continue;
            }
            long id = getLatestMessageSaved(textChannel);
            if(id!=-1) {
                saveMessagesUntil(textChannel, id);
            } else {
                System.out.println("Saving all messages for channel: " + textChannel);
                saveMessages(textChannel);
            }
        }
        discordBot.BOT_STATUS = BotStatus.READY;
        System.out.println("Duplicate key count: "+duplicateKeyCount);
        System.out.println(String.format("Finished saving %d messages", messageCount));
    }

    private void saveMessagesUntil(TextChannel textChannel, long id) {
        System.out.println("Saving messages for channel: "+textChannel.getName());
        List<Message> messages = getMessages(textChannel, id);
        for (Message message : messages) {
            String messageId = message.getId();
            String userId = message.getAuthor().getId();
            OffsetDateTime creationTime = message.getTimeCreated();
            Timestamp date = new Timestamp(creationTime.toInstant().toEpochMilli());
            String contentRaw = message.getContentRaw();
            if (contentRaw.equals("")) {
                contentRaw = "$PHOTO_CONTENT$";
            }
            String channelId = message.getChannel().getId();
            saveMessage(messageId, userId, date, contentRaw, channelId);
        }
        messageCount += messages.size();
        System.out.println(String.format("Saved %d messages for channel %s", messages.size(), textChannel.getName()));
    }

    private long getLatestMessageSaved(TextChannel textChannel){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT message_id FROM messages WHERE channel_id = ? ORDER BY DATE DESC limit 1");
            preparedStatement.setLong(1, textChannel.getIdLong());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("message_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveMessages(TextChannel textChannel) {
        System.out.println("Saving messages for channel: "+textChannel.getName());
        List<Message> messages = getMessages(textChannel);
        for (Message message : messages) {
            String messageId = message.getId();
            String userId = message.getAuthor().getId();
            OffsetDateTime creationTime = message.getTimeCreated();
            Timestamp date = new Timestamp(creationTime.toInstant().toEpochMilli());
            String contentRaw = message.getContentRaw();
            if (contentRaw.equals("")) {
                contentRaw = "$PHOTO_CONTENT$";
            }
            String channelId = message.getChannel().getId();
            saveMessage(messageId, userId, date, contentRaw, channelId);
        }
        messageCount += messages.size();
        System.out.println(String.format("Saved %d messages for channel %s", messages.size(), textChannel.getName()));
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
            if(e instanceof SQLIntegrityConstraintViolationException){
                duplicateKeyCount++;
            } else {
                e.printStackTrace();
            }
        }
    }

    private List<Message> getMessages(TextChannel channel){
        int amount = 100;
        List<Message> messages = new ArrayList<>();
        List<Message> receivingMessages = channel.getHistory().retrievePast(amount).complete();
        messages.addAll(receivingMessages);

        while(true){
            try {
                long idLong = receivingMessages.get(amount - 3).getIdLong();
                MessageHistory history = channel.getHistoryBefore(idLong, 2).complete();
                receivingMessages = history.retrievePast(amount).complete();
                messages.addAll(receivingMessages);
                if (receivingMessages.size() < amount) {
                    break;
                }
            } catch(IndexOutOfBoundsException e){
                break;
            }
        }
        System.out.println(messages.size());
        return messages;
    }
    private List<Message> getMessages(TextChannel channel, long messageId){
        int amount = 100;
        List<Message> messages = new ArrayList<>();
        List<Message> receivingMessages = channel.getHistory().retrievePast(amount).complete();

        for(Message message : receivingMessages){
            if(message.getIdLong() == messageId){
                return receivingMessages;
            }
        }
        messages.addAll(receivingMessages);

        while(true){
            try {
                long idLong = receivingMessages.get(amount - 3).getIdLong();
                MessageHistory history = channel.getHistoryBefore(idLong, 2).complete();
                receivingMessages = history.retrievePast(amount).complete();

                for(Message message : receivingMessages){
                    if(message.getIdLong() == messageId){
                        messages.addAll(receivingMessages);
                        break;
                    }
                }
                messages.addAll(receivingMessages);
                if (receivingMessages.size() < amount) {
                    break;
                }
            } catch(IndexOutOfBoundsException e){
                break;
            }
        }
        System.out.println(messages.size());
        return messages;
    }

    public void saveUsers(TextChannel general) {
        List<Member> members = general.getMembers();
        for (Member member : members) {
            OffsetDateTime joinDate = member.getTimeJoined();
            long millis = joinDate.toInstant().toEpochMilli();

            Date date = new Date(millis);
            String userName = member.getUser().getName();
            long userId = member.getUser().getIdLong();
            DiscordUser discordUser = new DiscordUser(userId, userName, date);
            discordUser.saveToDatabase(this.connection);
        }
    }


}
