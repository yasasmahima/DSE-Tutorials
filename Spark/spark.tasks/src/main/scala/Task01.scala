import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}

object Task01 {

  def main(args: Array[String]): Unit = {


    val spark: SparkSession = SparkSession.builder() //create spark session
      .appName("Spark example")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext // create spark context

    val rootLogger = Logger.getRootLogger() //get root logger and set level off
    rootLogger.setLevel(Level.OFF)


    val textFile = sc.textFile("src/main/resources/Spark Task 01.txt") //read text file


    val counts = textFile.flatMap(line => line.split(" ")) //count words
        .map(word => (word, 1))
        .reduceByKey(_ + _)

    println("Word count is : " + counts.count())

  }

}
