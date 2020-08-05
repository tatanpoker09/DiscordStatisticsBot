package com.eilers.tatanpoker09.database;

import java.sql.Connection;

public abstract class Database {
    public abstract Connection connect();
}
