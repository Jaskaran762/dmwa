package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ReutRead {

    private static final MongoClient mongoClient;
    private static final MongoCollection<Document> collection;

    static {
        String username = "singhjaskaran762";
        String password = "Canadajob1873";
        String clusterName = "cluster0.yqdt7pe.mongodb.net";
        String databaseName = "ReuterDb";

        // Build the MongoDB connection URL
        String uri = "mongodb+srv://" + username + ":" + password + "@" + clusterName + "/" + databaseName + "?retryWrites=true&w=majority";

        // Set up the MongoDB client with the connection URL
        mongoClient = MongoClients.create(uri);
        collection = buildConnection();
    }

    public static MongoCollection<Document> buildConnection() {
        MongoDatabase database = mongoClient.getDatabase("ReuterDb");
        System.out.println("Connection built");
        return database.getCollection("news");
    }

    public static void closeConnection() {
        mongoClient.close();
    }


    public static void createDocumentinCollection(String title, String body, String dateline) {

        Document document = new Document()
                .append("title", title)
                .append("body", body)
                .append("dateline", dateline);

        collection.insertOne(document);
    }


    public static void main(String[] args) {
        try {
            String file1 = "reut2-009.sgm";
            String file2 = "reut2-014.sgm";
            List<String> files = Arrays.asList(file1, file2);

            for (String file : files) {

                String fileContents = new String(Files.readAllBytes(Paths.get(file)));
                // Define the regex patterns to capture relevant information
                Pattern reutersPattern = Pattern.compile("<REUTERS[^>]*>(.*?)</REUTERS>", Pattern.DOTALL);
                Pattern titlePattern = Pattern.compile("<TITLE[^>]*>(.*?)</TITLE>");
                Pattern bodyPattern = Pattern.compile("<BODY[^>]*>(.*?)(</BODY>)", Pattern.DOTALL);
                Pattern datelinePattern = Pattern.compile("<DATELINE[^>]*>(.*?)(</DATELINE>)", Pattern.DOTALL);

                Matcher reutersMatcher = reutersPattern.matcher(fileContents);

                // Iterate through each <REUTERS> tag
                while (reutersMatcher.find()) {
                    String reutersTag = reutersMatcher.group(1);

                    // Extract title, body, and dateline information from each <REUTERS> tag
                    Matcher titleMatcher = titlePattern.matcher(reutersTag);
                    Matcher bodyMatcher = bodyPattern.matcher(reutersTag);
                    Matcher datelineMatcher = datelinePattern.matcher(reutersTag);

                    String title = null, body = null, dateline = null;
                    if (titleMatcher.find()) {
                        title = titleMatcher.group(1).trim();
                    }
                    if (bodyMatcher.find()) {
                        body = bodyMatcher.group(1).trim();
                    }
                    if (datelineMatcher.find()) {
                        dateline = datelineMatcher.group(1).trim();
                    }
                    createDocumentinCollection(title, body, dateline);
                }
            }
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
