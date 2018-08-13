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

    val contentBased = new ContentBasedRecommendation(4, "src/main/resources/",sc)
    contentBased.predict(1405)
    contentBased.show(5)

    println("-" * 50)

    val collborative = new CollaborativeRecommendation(4, "src/main/resources/",sc)
    collborative.predict(1405)
    collborative.show(5)


  }

}
