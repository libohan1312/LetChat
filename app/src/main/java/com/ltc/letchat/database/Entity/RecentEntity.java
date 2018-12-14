package com.ltc.letchat.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class RecentEntity {
    @Id public long id;
    public String recentName;
    public String content;
    public long time;
}
