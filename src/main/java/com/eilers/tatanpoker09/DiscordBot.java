package com.eilers.tatanpoker09;

import com.eilers.tatanpoker09.database.Database;
import com.eilers.tatanpoker09.database.MySQLDatabase;
import com.eilers.tatanpoker09.events.discord.BotStatusListener;
import com.eilers.tatanpoker09.events.discord.MessageListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.sql.Connection;

public class DiscordBot {
    private static final DatabaseType databaseType = DatabaseType.MYSQL;
    private static final String DATABASE_PREFIX = "DATABASE_";

    private Database database;
    private Connection connection;
    public BotStatus BOT_STATUS = BotStatus.GETTING_READY;

    public DiscordBot(String token){
        initializeDatabase();
        try {
            initializeDiscordBot(token);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void initializeDiscordBot(String token) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.addEventListener(new MessageListener(connection));
        builder.addEventListener(new BotStatusListener(connection, this));
        JDA bot = builder.buildAsync();

    }

    public void initializeDatabase(){
        Dotenv dotenv = Dotenv.load();
        String database = dotenv.get(DATABASE_PREFIX + "DATABASE");
        String ip = dotenv.get(DATABASE_PREFIX + "IP");
        String port = dotenv.get(DATABASE_PREFIX + "PORT");
        String username = dotenv.get(DATABASE_PREFIX + "USERNAME");
        String password = dotenv.get(DATABASE_PREFIX + "PASSWORD");

        switch(databaseType){
            case MYSQL:
                this.database = new MySQLDatabase(ip, database, username, password, port);
                connection = this.database.connect();

                break;
            case POSTGRES:
            case SQLITE:
            default:
                break;
        }
    }
}
