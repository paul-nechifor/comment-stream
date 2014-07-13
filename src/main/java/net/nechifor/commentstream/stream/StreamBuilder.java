package net.nechifor.commentstream.stream;

public class StreamBuilder {
    public String subreddit;
    public String userAgent;
    public long wait = 2100; // In milliseconds.
    public int maxLag = 60; // In seconds.
    public StreamListener[] listeners;

    public Stream build() {
        if (listeners == null) {
            throw new IllegalArgumentException("You forgot the listeners.");
        }

        if (userAgent == null) {
            throw new IllegalArgumentException("You forgot the userAgent.");
        }

        return new Stream(this);
    }
}
