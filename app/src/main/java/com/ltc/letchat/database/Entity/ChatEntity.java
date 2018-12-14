package com.ltc.letchat.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ChatEntity {
    @Id public long id;

    public String fromWho;
    public String content;
    public long time;
    public String toWho = "me";
}
