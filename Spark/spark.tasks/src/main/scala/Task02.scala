import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import java.io._

object Task02 {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder() //create spark session
      .appName("Spark example")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext // create spark context

    val rootLogger = Logger.getRootLogger() //get root logger and set level off
    rootLogger.setLevel(Level.OFF)

    val firstNames = sc.textFile("src/main/resources/First Names.txt") //read text files
    val lastNames = sc.textFile("src/main/resources/Last Names.txt")

    val firstNamesMap = firstNames.flatMap(name=> name.split(",")).map(x => (x.split(" ")(1), x.split(" ")(0)))
    val lastNamesMap = lastNames.map(x=> (x.split(" ")(0),x.split(" ")(1)))

    val fullNames = firstNamesMap.join(lastNamesMap).sortByKey(true)

    fullNames.coalesce(1).saveAsTextFile("src/main/resources/Full Names.txt")

    //val pw = new PrintWriter(new File("src/main/resources/Full Names.txt" ))
//    fullNames.map(x=>pw.println(x))
    //pw.close()





//    val ids = List(fullNames.keys)
//    val names = List(fullNames.values)




//    fullNames.foreach (x => println (x._1 + "->" + x._2))
//    fullNames.foreach(line => println(line(0)))
    //fullNames.foreach(println)
  }
}
