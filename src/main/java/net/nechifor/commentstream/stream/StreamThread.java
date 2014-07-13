package net.nechifor.commentstream.stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.nechifor.commentstream.comment.Comment;

class StreamThread extends Thread {
    private final String path;
    private final String userAgent;
    private final long wait;
    private final int maxLag;
    private final StreamListener listener;
    private final JsonParser parser = new JsonParser();
    private final BoundedSet<String> seen = new BoundedSet<>(128);
        
    private volatile boolean keepRunning = false;
    
    private String before = null;
    
    StreamThread(StreamBuilder builder, StreamListener listener) {
        this.path = getSubredditPath(builder.subreddit);
        this.userAgent = builder.userAgent;
        this.wait = builder.wait;
        this.maxLag = builder.maxLag;
        this.listener = listener;
    }

    @Override
    public void run() {
        keepRunning = true;

        while (keepRunning) {
            try {
                sleep(wait);
                checkComments();
            } catch (InterruptedException ex) {
                // Do not report.
            } catch (Exception ex) {
                ex.printStackTrace();
                listener.onRedditProblem(ex);
                before = null;
            }
        }
    }

    void stopRunning() {
        keepRunning = false;
        interrupt();
    }
    
    private void checkComments() throws Exception {
        URL url = getUrl(before);
        JsonObject update = getUpdate(url);
        
        JsonArray rawChildren = update.get("data").getAsJsonObject()
                .get("children").getAsJsonArray();
        
        List<Comment> comments = getProcessedComments(rawChildren);
        
        if (!comments.isEmpty()) {
            checkLag(comments);
            listener.onComments(comments);
        }
    }
    
    private void checkLag(List<Comment> comments) {
        Comment latest = comments.get(0);
        long now = new Date().getTime() / 1000;
        
        if (latest.created + maxLag < now) {
            before = null;
            listener.onMessage(new LagSkip(latest.created, now));
        }
    }
    
    private List<Comment> getProcessedComments(JsonArray rawChildren) {
        List<Comment> comments = new ArrayList<>();
        
        if (rawChildren.size() == 0) {
            return comments;
        }
        
        JsonObject latest = rawChildren.get(0).getAsJsonObject()
                .getAsJsonObject("data");
        before = "t1_" + latest.get("id").getAsString();
        
        for (JsonElement x : rawChildren) {
            JsonObject child = x.getAsJsonObject().getAsJsonObject("data");
            String perma = child.get("id").getAsString();
            if (!seen.contains(perma)) {
                seen.add(perma);
                comments.add(new Comment(child));
            }
        }
        
        Collections.sort(comments);
        
        return comments;
    }
    
    private JsonObject getUpdate(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", userAgent);
        
        JsonObject obj = (JsonObject) parser.parse(new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")));

        return obj;
    }
    
    private URL getUrl(String before) throws MalformedURLException {
        String str = path;
        
        str += "?limit=100"; // This is the maximum limit.
        if (before != null) {
            str += "&before=" + before;
        }
        
        return new URL(str);
    }
    
    private static String getSubredditPath(String subreddit) {
        if (subreddit == null) {
            return "http://reddit.com/comments.json";
        }
        
        return "http://reddit.com/r/" + subreddit + "/comments.json";
    }
}
