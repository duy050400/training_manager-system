package com.mockproject.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FileUtils {
    public static byte[] compressFile(byte[] uncompressedData) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(uncompressedData);
        deflater.finish();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(uncompressedData.length);
        byte[] bufferBytes = new byte[10*1024];
        while (!deflater.finished()){
            int size = deflater.deflate(bufferBytes);
            byteArrayOutputStream.write(bufferBytes,0,size);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decompressFile(byte[] compressedData) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] bufferBytes = new byte[10*1024];
        while (!inflater.finished()){
            int count = inflater.inflate(bufferBytes);
            outputStream.write(bufferBytes, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    public static byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = Files.readAllBytes(file.toPath());
        Base64.getEncoder().encode(bytes);
        return bytes;
    }
}
