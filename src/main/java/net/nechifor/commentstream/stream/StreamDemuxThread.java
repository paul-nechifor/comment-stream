package net.nechifor.commentstream.stream;

import net.nechifor.commentstream.comment.Comment;

import java.util.LinkedList;
import java.util.List;

class StreamDemuxThread extends Thread implements StreamListener {
    private final StreamListener[] listeners;
    private final List<Comment> onComments = new LinkedList<>();
    private final List<Exception> onRedditProblems = new LinkedList<>();
    private final List<StreamMessage> onMessages = new LinkedList<>();
    
    private volatile boolean keepRunning = false;

    public StreamDemuxThread(StreamListener[] listeners) {
        this.listeners = listeners;
    }

    @Override
    public void run() {
        keepRunning = true;

        while (keepRunning) {
            try {
                sleep(50);
                spread();
            } catch (InterruptedException ex) {
                // Do not report.
            } catch (Exception ex) {
                ex.printStackTrace();
                for (StreamListener l : listeners) {
                    l.onRedditProblem(ex);
                }
            }
        }
    }

    void stopRunning() {
        keepRunning = false;
        interrupt();
    }

    @Override
    public void onComments(List<Comment> comments) {
        synchronized (onComments) {
            for (Comment c: comments) {
                onComments.add(c);
            }
        }
    }

    @Override
    public void onRedditProblem(Exception ex) {
        synchronized (onRedditProblems) {
            onRedditProblems.add(ex);
        }
    }

    @Override
    public void onMessage(StreamMessage message) {
        synchronized (onMessages) {
            onMessages.add(message);
        }
    }
    
    private void spread() {
        synchronized (onRedditProblems) {
            if (!onRedditProblems.isEmpty()) {
                for (Exception ex: onRedditProblems) {
                    for (StreamListener l : listeners) {
                        l.onRedditProblem(ex);
                    }
                }
                onRedditProblems.clear();
            }
        }
        synchronized (onMessages) {
            if (!onMessages.isEmpty()) {
                for (StreamMessage m : onMessages) {
                    for (StreamListener l : listeners) {
                        l.onMessage(m);
                    }
                }
                onMessages.clear();
            }
        }
        synchronized (onComments) {
            if (!onComments.isEmpty()) {
                for (StreamListener l : listeners) {
                    l.onComments(onComments);
                }
                onComments.clear();
            }
        }
    }
}
