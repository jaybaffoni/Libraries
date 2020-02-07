package com.thecrowdedstudio.database;

public abstract class DatabaseObject {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
