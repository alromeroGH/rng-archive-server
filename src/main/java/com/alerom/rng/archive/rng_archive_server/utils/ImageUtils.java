package com.alerom.rng.archive.rng_archive_server.utils;

import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidImageException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class ImageUtils {
    public static String saveBase64Image(String base64Image, String uploadDir) throws IOException {
        String[] parts = base64Image.split(",");

        if (parts.length < 2) {
            throw new InvalidImageException("Invalid Base64 image format");
        }

        String base64Data = parts[1];

        String fileName = UUID.randomUUID().toString() + ".png";

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        File file = new File(uploadDir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        return fileName;
    }
}