package com.thecrowdedstudio.database;

public class DatabaseColumn {

    String name;
    String type;
    boolean notNull;
    boolean primaryKey;
    boolean autoIncrement;

    public DatabaseColumn(String name, String type, boolean notNull, boolean primaryKey, boolean autoIncrement) {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    @Override
    public String toString(){
        String notNullString = notNull ? " not null" : "";
        String primaryKeyString = primaryKey ? " primary key" : "";
        String autoIncrementString = autoIncrement ? " autoincrement" : "";
        return name + " " + type + primaryKeyString + autoIncrementString + notNullString;
    }
}
