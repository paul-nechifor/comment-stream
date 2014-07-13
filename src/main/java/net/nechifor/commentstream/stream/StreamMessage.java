package net.nechifor.commentstream.stream;

public abstract class StreamMessage {
    public final String type;
    
    public StreamMessage(String type) {
        this.type = type;
    }
}
