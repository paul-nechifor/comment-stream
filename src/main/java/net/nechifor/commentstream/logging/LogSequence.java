package net.nechifor.commentstream.logging;

import java.io.File;
import java.security.InvalidParameterException;

public class LogSequence {
    private final File parent;
    private final int maxPerDir;
    private final String ext;
    private File child;
    private int major = 0;
    private int minor = 0;
    
    public LogSequence(File parent, int maxPerDir, String ext) {
        this.parent = parent;
        this.maxPerDir = maxPerDir;
        this.ext = ext;
    }
    
    public void setup() {
        if (!parent.exists()) {
            parent.mkdirs();
            child = new File(parent, Integer.toString(major));
            child.mkdir();
            return;
        }
        if (!parent.isDirectory()) {
            throw new InvalidParameterException("It's not a dir.");
        }
        
        major = getNewAvailable(parent);
        child = new File(parent, Integer.toString(major));
        child.mkdir();
        minor = getNewAvailable(child);
    }
    
    public File getFile() {
        File ret = new File(child, String.format("%d.%s", minor, ext));
        minor++;
        if (minor >= maxPerDir) {
            major++;
            minor = 0;
            child = new File(parent, Integer.toString(major));
        }
        return ret;
    }
    
    private int getNewAvailable(File dir) {
        int max = -1;
        
        for (String f : dir.list()) {
            int n = Integer.parseInt(f.split("\\.")[0]);
            if (n > max) {
                max = n;
            }
        }
        
        return max + 1;
    }
}
