package org.example;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<BOWModel> bows = ReutRead.getBOWModels();

        //GET POSITIVE AND NEGATIVE WORDS FROM GITHUB
        List<String> positiveWordListWithNull = ReutRead.getPositiveWordList();
        List<String> positiveWordList = positiveWordListWithNull.stream()
                .filter(s -> !s.isEmpty())
                .toList();
        List<String> negativeWordListWithNull = ReutRead.getNegativeWordList();
        List<String> negativeWordList = negativeWordListWithNull.stream()
                .filter(s -> !s.isEmpty())
                .toList();

        // Calculate polarity for each BOWModel and classify the news
        int cnt = 0;
        for (BOWModel model : bows) {
            int polarityScore = model.calculatePolarity(positiveWordList, negativeWordList);
            String classification;
            if (polarityScore > 0) {
                classification = "Positive News";
            } else if (polarityScore < 0) {
                classification = "Negative News";
            } else {
                classification = "Neutral News";
            }
            System.out.format("%d ||  %s  || %s", ++cnt, model.getTitle(), classification);
            System.out.println();
        }


    }
}