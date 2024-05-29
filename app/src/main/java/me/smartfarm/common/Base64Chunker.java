package me.smartfarm.common;

import java.util.ArrayList;
import java.util.List;

public class Base64Chunker {
    private static final int CHUNK_SIZE = 1000; // Adjust chunk size as needed

    // Method to chunk the Base64 string into smaller chunks
    public static List<String> chunkBase64String(String base64String) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < base64String.length(); i += CHUNK_SIZE) {
            int endIndex = Math.min(i + CHUNK_SIZE, base64String.length());
            chunks.add(base64String.substring(i, endIndex));
        }
        return chunks;
    }

    // Method to reconstruct the original Base64 string from chunks
    public static String reconstructBase64String(List<String> chunks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String chunk : chunks) {
            stringBuilder.append(chunk);
        }
        return stringBuilder.toString();
    }
}
