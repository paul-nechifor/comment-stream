package net.nechifor.commentstream.stream;

public class Stream {   
    private final StreamThread thread;
    private final StreamDemuxThread demuxThread;
    
    Stream(StreamBuilder builder) {
        this.demuxThread = new StreamDemuxThread(builder.listeners);
        this.thread = new StreamThread(builder, this.demuxThread);
    }
    
    public void start() {
        thread.start();
        demuxThread.start();
    }
    
    public void stop() {
        thread.stopRunning();
        demuxThread.stopRunning();
    }
}
