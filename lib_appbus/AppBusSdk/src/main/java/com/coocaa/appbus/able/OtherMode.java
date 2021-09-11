package com.coocaa.appbus.able;

import java.io.Serializable;

public class OtherMode implements Serializable {
    String id = "com.open.xbus";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OtherMode{" +
                "id='" + id + '\'' +
                '}';
    }
}
