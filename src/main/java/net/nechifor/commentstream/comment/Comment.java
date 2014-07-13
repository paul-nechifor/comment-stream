package net.nechifor.commentstream.comment;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class Comment implements Comparable<Comment> {
    public final JsonObject raw;
    public final long id;
    public final int created;
    public final Map<String, Object> data = new HashMap<>();
    
    public Comment(JsonObject raw) {
        this.raw = raw;
        this.id = Long.parseLong(raw.get("id").getAsString(), 36);
        this.created = raw.get("created_utc").getAsInt();
    }

    @Override
    public int compareTo(Comment o) {
        return (created == o.created)
                ? Long.compare(id, o.id)
                : Integer.compare(created, o.created);
    }
}
