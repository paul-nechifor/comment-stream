package net.nechifor.commentstream.comment;

public abstract class Processor {
    public abstract void transform(Comment comment) throws Exception;
    public void transform(CommentSet set) throws Exception {
        for (Comment c : set.comments) {
            transform(c);
        }
    }
}
