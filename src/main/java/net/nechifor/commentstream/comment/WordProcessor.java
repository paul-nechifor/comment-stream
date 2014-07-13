package net.nechifor.commentstream.comment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordProcessor extends Processor {
    private static final String REGEX = "\\w(\\w|[-']+)*\\w?";
    private static final Pattern PATTERN = Pattern.compile(REGEX,
            Pattern.CASE_INSENSITIVE |
            Pattern.UNICODE_CASE |
            Pattern.UNICODE_CHARACTER_CLASS);
    private static final String[] THIS_ONE = new String[0];

    @Override
    public void transform(Comment comment) throws UnsupportedEncodingException {
        String body = comment.raw.get("body").getAsString();
        List<String> words = new ArrayList<>();
        
        Matcher m = PATTERN.matcher(body);
        
        while (m.find()) {
            words.add(m.group());
        }
        
        comment.data.put("words", words.toArray(THIS_ONE));
    }
}
