package net.nechifor.commentstream.stream;

import net.nechifor.commentstream.comment.Comment;
import net.nechifor.commentstream.comment.CommentSet;
import net.nechifor.commentstream.comment.Processor;

import java.util.List;

public class SimpleIntervalListener implements IntervalListener {
    private final Processor[] onArrivals;
    private final Processor[] onIntervals;
    
    public SimpleIntervalListener(Processor[] onArrivals,
            Processor[] onIntervals) {
        this.onArrivals = onArrivals;
        this.onIntervals = onIntervals;
    }

    @Override
    public void onInterval(CommentSet set, int intervalCount) {
        for (Processor p : onIntervals) {
            try {
                p.transform(set);
            } catch (Exception ex) {
                this.onRedditProblem(ex);
            }
        }
    }

    @Override
    public void onMinor() {
    }

    @Override
    public void onMajor() {
    }

    @Override
    public void onComments(List<Comment> comments) {
        for (Processor p : onArrivals) {
            for (Comment c : comments) {
                try {
                    p.transform(c);
                } catch (Exception ex) {
                    this.onRedditProblem(ex);
                }
            }
        }
    }

    @Override
    public void onRedditProblem(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onMessage(StreamMessage message) {
    }
}