package com.eilers.tatanpoker09;

import com.eilers.tatanpoker09.database.Database;
import com.eilers.tatanpoker09.database.MySQLDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    private Database database;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        DiscordBot discordBot = new DiscordBot(token);
    }
}
