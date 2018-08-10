import com.twitter.scalding
import com.twitter.scalding.{Csv, Tsv}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql

import scala.collection.immutable.ListMap

object Main {

  Logger.getLogger("org").setLevel(Level.OFF)

  val sc =
    SparkSession.builder.
      master("local[*]")
      .appName("movie recommendation")
      .getOrCreate()

  import sc.implicits._

  def main(args: Array[String]): Unit = {

//    val contentBased = new ContentBasedRecommendation(4, "src/main/resources/",sc)
//
//    contentBased.run(163056)
//    val predictions = contentBased.predict(10)
//    predictions.foreach(println)

    val ratings = Csv("src/main/resources/ratings.csv",separator = ",",('user, 'movie, 'rating,'timestamp)).read.project()

    println(ratings)


  }

}
