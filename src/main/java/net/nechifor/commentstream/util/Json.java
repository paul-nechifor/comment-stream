package net.nechifor.commentstream.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * JSON utility methods.
 * @author Paul Nechifor
 */
public class Json {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().create();
    }
    
    private Json() {
    }
    
    public static <E> E fromString(String message, Class<E> type)
            throws IOException {
        return GSON.fromJson(message, type);
    }
    
    public static <E> E fromFilePath(File file, Class<E> type)
            throws IOException {
        return fromString(Util.readEntireFile(file), type);
    }
    
    public static String toString(Object object)
            throws IOException {
        return GSON.toJson(object);
    }
    
    public static void writeToFile(JsonElement e, File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        GSON.toJson(e, bw);
    }
}