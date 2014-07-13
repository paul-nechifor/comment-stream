package net.nechifor.commentstream.stream;

import java.util.LinkedList;
import java.util.List;
import net.nechifor.commentstream.comment.Comment;
import net.nechifor.commentstream.comment.CommentSet;

public class IntervalAggregator implements StreamListener {
    private final IntervalListener[] listeners;
    private final int interval;
    private final int minor;
    private final int major;
    
    private int lastIntervalCount = -1;
    private int lastMinorCount = -1;
    private int lastMajorCount = -1;
    
    private final List<Comment> intervalComments = new LinkedList<>();
    
    public IntervalAggregator(IntervalListener listeners[], int seconds,
            int minorTimes, int majorTimes) {
        this.listeners = listeners;
        this.interval = seconds;
        this.minor = seconds * minorTimes;
        this.major = seconds * minorTimes * majorTimes;
    }

    @Override
    public void onComments(List<Comment> comments) {
        for (Comment c: comments) {
            addComment(c);
        }
        for (IntervalListener l : listeners) {
            l.onComments(comments);
        }
    }

    @Override
    public void onRedditProblem(Exception ex) {
        for (IntervalListener l : listeners) {
            l.onRedditProblem(ex);
        }
    }

    @Override
    public void onMessage(StreamMessage message) {
        for (IntervalListener l : listeners) {
            l.onMessage(message);
        }
    }
    
    private void addComment(Comment c) {
        int intervalCount = c.created / interval;
        
        if (lastIntervalCount == -1) {
            lastIntervalCount = intervalCount;
            lastMinorCount = c.created / minor;
            lastMajorCount = c.created / major;
        }
        
        if (intervalCount == lastIntervalCount) {
            intervalComments.add(c);
            return;
        }
        
        flushComments(c.created, intervalCount);
        
        intervalComments.add(c);
    }
    
    private void flushComments(int created, int intervalCount) {
        for (IntervalListener l : listeners) {
            l.onInterval(new CommentSet(intervalComments), intervalCount);
        }
        intervalComments.clear();
        lastIntervalCount = intervalCount;
        
        int minorCount = created / minor;
        if (minorCount != lastMinorCount) {
            for (IntervalListener l : listeners) {
                l.onMinor();
            }
        }
        lastMinorCount = minorCount;
        
        int majorCount = created / major;
        if (majorCount != lastMajorCount) {
            for (IntervalListener l : listeners) {
                l.onMajor();
            }
        }
        lastMajorCount = majorCount;
    }
}
