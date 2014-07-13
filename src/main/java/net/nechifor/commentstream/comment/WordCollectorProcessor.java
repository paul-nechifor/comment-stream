package net.nechifor.commentstream.comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCollectorProcessor extends Processor {
    public WordCollectorProcessor() {
    }

    @Override
    public void transform(CommentSet set) {
        Map<String, SameWord> wordCounts = new HashMap<>();
        SameWord sameWord;
        
        for (Comment c : set.comments) {
            if (!c.data.containsKey("words")) {
                continue;
            }
            
            for (String word : (String[]) c.data.get("words")) {
                String lower = word.toLowerCase();
                sameWord = wordCounts.get(lower);
                
                if (sameWord == null) {
                    sameWord = new SameWord(lower);
                    wordCounts.put(lower, sameWord);
                }
                
                sameWord.countWord(word);
            }
        }
        
        List<SameWord> allWords = new ArrayList<>(wordCounts.values());
        Map<String, Integer> wordMap = getWordMap(allWords);
        set.data.put("words", wordMap);
    }

    @Override
    public void transform(Comment comment) {
        // It's not used. Only bulk comments are written.
    }
    
    private Map<String, Integer> getWordMap(List<SameWord> allWords) {
        Collections.sort(allWords);
        
        Map<String, Integer> ret = new HashMap<>();
        
        for (SameWord sameWord : allWords) {
            ret.put(sameWord.computeTopSpelling(), sameWord.count);
        }
        
        return ret;
    }
}

class SameWord implements Comparable<SameWord> {
    final String lowerWord;
    final Map<String, SingleWord> spellings = new HashMap<>();
    int count;
    
    SameWord(String lowerWord) {
        this.lowerWord = lowerWord;
        this.count = 0; // Not 1 since countWord must be called.
    }
    
    void countWord(String word) {
        SingleWord w = spellings.get(word);
        if (w != null) {
            w.count++;
        } else {
            w = new SingleWord(word);
            spellings.put(word, w);
        }
        count++;
    }
    
    String computeTopSpelling() {
        List<SingleWord> l = new ArrayList<>(spellings.values());
        SingleWord top = l.get(0);
        
        SingleWord sw;
        for (int i = 1, len = l.size(); i < len; i++) {
            sw = l.get(i);
            if (sw.count > top.count) {
                top = sw;
            }
        }
        
        return top.word;
    }

    @Override
    public int compareTo(SameWord o) {
        return o.count - count;
    }
}

class SingleWord implements Comparable<SingleWord> {
    final String word;
    int count;
    
    SingleWord(String word) {
        this.word = word;
        this.count = 1;
    }

    @Override
    public int compareTo(SingleWord o) {
        return o.count - count;
    }
}