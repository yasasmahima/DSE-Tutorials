import org.apache.spark.sql.{Column, SparkSession}
import org.apache.spark.sql.functions.{concat, lit}
import org.apache.log4j.{Level, Logger}

object Task02 {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder() //create spark session
      .appName("Spark example")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext // create spark context

    val rootLogger = Logger.getRootLogger() //get root logger and set level off
    rootLogger.setLevel(Level.OFF)

    val firstNames = sc.textFile("src/main/resources/First Names.txt")//read text files
    val lastNames = sc.textFile("src/main/resources/Last Names.txt")

    val firstNamesMap = firstNames.flatMap(name=> name.split(",")).map(x => (x.split(" ")(1), x.split(" ")(0)))
    val lastNamesMap = lastNames.map(x=> (x.split(" ")(0),x.split(" ")(1)))

   val firstNamesDF = spark.createDataFrame(firstNamesMap).toDF("id" , "fName")

   val lastNamesDF = spark.createDataFrame(lastNamesMap).toDF("id" , "lName")

   var fullNamesDF = firstNamesDF.join(lastNamesDF, "id")

    fullNamesDF = fullNamesDF.select(concat(fullNamesDF("fName"), lit(" "), fullNamesDF("lName")) as "Full Name")

    fullNamesDF.show()

   // fullNamesDF.collect()
    //fullNamesDF.write.mode("overwrite").save("src/main/resources/FullNames.txt")
    //fullNamesDF.coalesce(1).write.text("src/main/resources/FullNames.txt")

fullNamesDF.repartition(1).write.mode("overwrite").text("src/main/resources/FullNames/")

//    fullNamesDF.write.format("com.databricks.spark.csv").option("header", "false").save("src/main/resources/FullNames")


  }
}
