package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BOWModel {

    private String title;
    private List<String> names;
    private HashMap<String, Integer> cnt;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public HashMap<String, Integer> getCnt() {
        return cnt;
    }

    public void setCnt(HashMap<String, Integer> cnt) {
        this.cnt = cnt;
    }

    public int calculatePolarity(List<String> positiveWordList, List<String> negativeWordList) {
        int polarityScore = 0;
        StringBuilder matchedWords = new StringBuilder();

        for (Map.Entry<String, Integer> entry : cnt.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();

            if (positiveWordList.contains(word)) {
                polarityScore += count; // Add positive score
                matchedWords.append(word).append(" ");
            } else if (negativeWordList.contains(word)) {
                polarityScore -= count; // Subtract negative score
                matchedWords.append(word).append(" ");
            }
        }

        // Print the matched words for each news title
        System.out.println();
        if (!matchedWords.isEmpty())
            System.out.println("Matched Words: " + matchedWords.toString());
        else
            System.out.println("No matched Words");

        return polarityScore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
