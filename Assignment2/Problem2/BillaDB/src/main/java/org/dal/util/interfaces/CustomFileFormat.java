package org.dal.util.interfaces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface CustomFileFormat {

    boolean writeToFile(byte[] dataBytes, String filePath, String fileName) throws UnsupportedEncodingException;
    byte[] readFile(String filePath, String fileName) throws IOException;
    boolean createFolder(String filePath);
    public boolean deleteFile(String filePath, String fileName) throws UnsupportedEncodingException;

    }
