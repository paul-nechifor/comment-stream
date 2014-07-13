package net.nechifor.commentstream.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentSet {
    public final Map<String, Object> data = new HashMap<>();
    public final List<Comment> comments;
    
    public CommentSet(List<Comment> comments) {
        this.comments = comments;
    }
}
