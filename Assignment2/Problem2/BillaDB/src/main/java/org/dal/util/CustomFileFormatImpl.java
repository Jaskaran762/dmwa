package org.dal.util;

import org.dal.util.interfaces.CustomFileFormat;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CustomFileFormatImpl implements CustomFileFormat {

    public boolean createFolder(String filePath) {
        try {
            String currentWorkingDir = System.getProperty("user.dir");
            String relativePath = currentWorkingDir + "/target/";

            Path encryptedFolderPath = Paths.get(relativePath + filePath);
            if (!Files.exists(encryptedFolderPath)) {
                Files.createDirectories(encryptedFolderPath);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean writeToFile(byte[] dataBytes, String filePath, String fileName) throws UnsupportedEncodingException {
        try {
            String currentWorkingDir = System.getProperty("user.dir");
            String relativePath = currentWorkingDir + "/target/";

            Path encryptedFolderPath = Paths.get(relativePath + filePath);
            Path encryptedFilePath = Paths.get(relativePath + filePath + "/" + fileName + ".billa");

            if (!Files.exists(encryptedFolderPath)) {
                Files.createDirectories(encryptedFolderPath);
            }
            if (!Files.exists(encryptedFilePath)) {
                Files.createFile(encryptedFilePath);
            }
            Files.write(encryptedFilePath, dataBytes, StandardOpenOption.APPEND);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public byte[] readFile(String filePath, String fileName) throws IOException {

        String currentWorkingDir = System.getProperty("user.dir");
        String relativePath = currentWorkingDir + "/target/";
        Path encryptedFilePath = Paths.get(relativePath + filePath + "/" + fileName + ".billa");

        byte[] encryptedContent = Files.readAllBytes(encryptedFilePath);
        return encryptedContent;
    }

    @Override
    public boolean deleteFile(String filePath, String fileName) throws UnsupportedEncodingException {
        try {
            String currentWorkingDir = System.getProperty("user.dir");
            String relativePath = currentWorkingDir + "/target/";

            Path encryptedFilePath = Paths.get(relativePath + filePath + "/" + fileName + ".billa");

            Files.deleteIfExists(encryptedFilePath);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
