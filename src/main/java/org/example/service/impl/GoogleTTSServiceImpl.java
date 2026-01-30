package org.example.service.impl;

import org.example.service.GoogleTTSService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GoogleTTSServiceImpl implements GoogleTTSService {

    private static final String CACHE_DIR = "audio_cache/";
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.google.com/translate_tts?ie=UTF-8&tl=en&client=tw-ob&q=";



    public GoogleTTSServiceImpl() {
        try {
            Files.createDirectories(Paths.get(CACHE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public String getAudioPath(String audioFileName) {
        if (audioFileName == null || audioFileName.trim().isEmpty()) return null;

        try {
            String safeFileName = audioFileName.replaceAll("[^a-zA-Z0-9]", "") + ".mp3";
            String filePath = CACHE_DIR + safeFileName;
            File audioFile = new File(filePath);
            if (audioFile.exists()) {
                System.out.println("Using cached audio: " + filePath);
                return audioFile.toURI().toString();
            }

            System.out.println("Downloading audio for: " + audioFileName);

            String encodedText = URLEncoder.encode(audioFileName , StandardCharsets.UTF_8);
            String fullUrl = GOOGLE_TRANSLATE_URL + encodedText;

            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                try (InputStream in = conn.getInputStream();
                     FileOutputStream out = new FileOutputStream(audioFile)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

                    System.out.println("Downloaded successfully: " + filePath);
                    return audioFile.toURI().toString();
                }
            } else {
                System.err.println("Google TTS Error: Response Code " + conn.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải âm thanh: " + e.getMessage());
        }
        return null;
    }
}