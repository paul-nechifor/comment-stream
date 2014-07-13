package net.nechifor.commentstream.stream;

class LagSkip extends StreamMessage {
    LagSkip(long serverTime, long localTime) {
        super("lag-skip");
        this.serverTime = serverTime;
        this.localTime = localTime;
    }

    long serverTime;
    long localTime;
}
