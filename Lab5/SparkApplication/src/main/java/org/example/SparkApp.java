package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.explode;

import java.nio.file.Paths;

public class SparkApp {
	public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
                .appName("Spark App")
                .master("local")
                .getOrCreate();

        Dataset<Row> weatherData = spark.read().option("multiline", true).json("weather.json");

        StructType feelsLikeSchema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("day", DataTypes.DoubleType, true),
                DataTypes.createStructField("night", DataTypes.DoubleType, true),
                DataTypes.createStructField("eve", DataTypes.DoubleType, true),
                DataTypes.createStructField("morn", DataTypes.DoubleType, true)
        });

        // Filter the data where feels_like.day > 15
        Dataset<Row> filteredData = weatherData.select(col("lat"), col("lon"), col("timezone"), col("timezone_offset"),
                        explode(col("daily")).as("daily"))
                .select("lat", "lon", "timezone", "timezone_offset", "daily.*")
                .withColumn("feels_like", col("feels_like").cast(feelsLikeSchema))
                .filter(col("feels_like.day").gt(15));

        // Exclude unnecessary fields
        Dataset<Row> finalData = filteredData.drop("feels_like.night", "feels_like.eve", "feels_l ike.morn",
                "pressure", "humidity", "dew_point", "wind_speed", "wind_deg", "wind_gust", "weather",
                "clouds", "pop", "rain", "uvi");

        finalData.write().json("file:///updated_weather.json");

        spark.stop();

    }
}