package net.nechifor.commentstream.stream;

import net.nechifor.commentstream.comment.CommentSet;

public interface IntervalListener extends StreamListener {
    public void onInterval(CommentSet set, int intervalCount);
    public void onMinor();
    public void onMajor();
}
