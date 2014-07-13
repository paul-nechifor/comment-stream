package net.nechifor.commentstream.comment;

import com.google.gson.JsonArray;
import java.io.File;
import java.io.IOException;
import net.nechifor.commentstream.logging.LogSequence;
import net.nechifor.commentstream.util.Json;

public class LoggingProcessor extends Processor {
    private final LogSequence logSequence;
    
    public LoggingProcessor(File parent) {
        this.logSequence = new LogSequence(parent, 1000, "json");
        this.logSequence.setup();
    }

    @Override
    public void transform(CommentSet set) {
        JsonArray array = new JsonArray();
        for (Comment c : set.comments) {
            array.add(c.raw);
        }
        
        File out = logSequence.getFile();
        try {
            Json.writeToFile(array, out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void transform(Comment comment) {
        // It's not used. Only bulk comments are written.
    }
}
