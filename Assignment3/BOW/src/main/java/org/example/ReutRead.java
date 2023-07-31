package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReutRead {

    private static List<String> titleList;

    public static List<BOWModel> bows;

    public static List<BOWModel> getBOWModels() {
        List<BOWModel> models = new ArrayList<>();
        List<String> titles = readTitleInFiles();

        for (String title : titles) {
            BOWModel model = new BOWModel();
            model.setNames(Arrays.asList(title.split("\\s+")));
            model.setCnt(getWordCounts(title));
            model.setTitle(title);
            models.add(model);
        }

        return models;
    }

    private static HashMap<String, Integer> getWordCounts(String title) {
        HashMap<String, Integer> wordCounts = new HashMap<>();
        String[] words = title.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase();
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        return wordCounts;
    }

    public static List<String> readTitleInFiles() {

        titleList = new ArrayList<>();
        try {
            String file1 = "reut2-009.sgm";
            String file2 = "reut2-014.sgm";
            List<String> files = Arrays.asList(file1, file2);

            for (String file : files) {

                String fileContents = new String(Files.readAllBytes(Paths.get(file)));
                // Define the regex patterns to capture relevant information
                Pattern reutersPattern = Pattern.compile("<REUTERS[^>]*>(.*?)</REUTERS>", Pattern.DOTALL);
                Pattern titlePattern = Pattern.compile("<TITLE[^>]*>(.*?)</TITLE>");
                Matcher reutersMatcher = reutersPattern.matcher(fileContents);

                // Iterate through each <REUTERS> tag
                while (reutersMatcher.find()) {
                    String reutersTag = reutersMatcher.group(1);

                    // Extract title, body, and dateline information from each <REUTERS> tag
                    Matcher titleMatcher = titlePattern.matcher(reutersTag);

                    String title = null;
                    if (titleMatcher.find()) {
                        title = titleMatcher.group(1).trim();
                        titleList.add(title);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleList;
    }

    public static List<String> getNegativeWordList() {

        try {
            String fileName = "negative-words.txt";
            String fileContents = new String(Files.readAllBytes(Paths.get(fileName)));

            String[] fileContentArray = fileContents.split(";");
            String negativeString = fileContentArray[fileContentArray.length-1];

            String[] negativeWordArray = negativeString.split("\\r?\\n|\\r");
            return Arrays.asList(negativeWordArray);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getPositiveWordList() {

        try {
            String fileName = "positive-words.txt";
            String fileContents = new String(Files.readAllBytes(Paths.get(fileName)));

            String[] fileContentArray = fileContents.split(";");
            String negativeString = fileContentArray[fileContentArray.length-1];

            String[] negativeWordArray = negativeString.split("\\r?\\n|\\r");
            return Arrays.asList(negativeWordArray);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
