package net.nechifor.commentstream.stream;

import java.util.List;
import net.nechifor.commentstream.comment.Comment;

public interface StreamListener {
    public void onComments(List<Comment> comments);
    public void onRedditProblem(Exception ex);
    public void onMessage(StreamMessage message);
}
