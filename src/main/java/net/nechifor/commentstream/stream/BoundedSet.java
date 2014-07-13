package net.nechifor.commentstream.stream;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class BoundedSet<E> {
    private final Set<E> set = new HashSet<>();
    private final LinkedList<E> list = new LinkedList<>();
    private final int limit;
    private int count = 0;
    
    public BoundedSet(int limit) {
        this.limit = limit;
    }
    public boolean contains(E e) {
        return set.contains(e);
    }
    
    public void add(E e) {
        if (count == limit) {
            E old = list.removeFirst();
            set.remove(old);
        } else {
            count++;
        }
        
        list.add(e);
        set.add(e);
    }
}
