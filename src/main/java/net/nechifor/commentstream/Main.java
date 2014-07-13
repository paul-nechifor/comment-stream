package net.nechifor.commentstream;

import net.nechifor.commentstream.comment.Processor;
import net.nechifor.commentstream.comment.WordCollectorProcessor;
import net.nechifor.commentstream.comment.WordProcessor;
import net.nechifor.commentstream.stream.IntervalAggregator;
import net.nechifor.commentstream.stream.IntervalListener;
import net.nechifor.commentstream.stream.SimpleIntervalListener;
import net.nechifor.commentstream.stream.Stream;
import net.nechifor.commentstream.stream.StreamBuilder;
import net.nechifor.commentstream.stream.StreamListener;

public class Main {
    public static void main(String[] args) {
        Processor[] onArrivals = {
            new WordProcessor()
        };
        Processor[] onIntervals = {
            //new LoggingProcessor(new File("/home/p/log"))
            new WordCollectorProcessor()
        };
    
        IntervalListener[] ils = {
            new SimpleIntervalListener(onArrivals, onIntervals)
        };
        
        IntervalAggregator ia = new IntervalAggregator(ils, 60, 60, 24);
        
        StreamBuilder b = new StreamBuilder();
        b.wait = 5000;
        b.listeners = new StreamListener[]{ia};
        
        Stream s = b.build();
        s.start();
    }
}
