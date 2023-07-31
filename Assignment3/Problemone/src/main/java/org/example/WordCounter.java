package org.example;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class WordCounter {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Word Counter")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        JavaRDD<String> textLines = sc.textFile("reut2-009.sgm");

        JavaRDD<String> textContents = textLines
                .map(line -> line.replaceAll("<.*?>", ""));

        JavaRDD<String> words = textContents.flatMap(line -> Arrays.asList(line.split("\\W+")).iterator());

        JavaRDD<String> cleanedWords = words.map(word -> word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase())
                .filter(word -> word.length() > 0);

        Map<String, Long> wordCounts = cleanedWords.countByValue();

        // Sort the words based on occurrence count
        List<Map.Entry<String, Long>> sortedWords = wordCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());
        
        System.out.println(sortedWords.size());

        // Print the most and least occurred words
        if (!sortedWords.isEmpty()) {
            System.out.println("Most Occurred Word: " + sortedWords.get(0).getKey() + " (" + sortedWords.get(0).getValue() + " occurrences)");
            System.out.println("Least Occurred Word: " + sortedWords.get(sortedWords.size() - 1).getKey() + " (" + sortedWords.get(sortedWords.size() - 1).getValue() + " occurrences)");

            // Save sorted words to a file
            String outputFilePath = "output.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                for (Map.Entry<String, Long> entry : sortedWords) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }
                System.out.println("Sorted word counts saved to " + outputFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No words found in the text.");
        }

        spark.stop();
    }
}
