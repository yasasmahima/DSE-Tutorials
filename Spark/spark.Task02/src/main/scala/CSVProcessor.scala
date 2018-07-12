import com.sun.javaws.exceptions.InvalidArgumentException
import org.apache.spark.SparkContext
import org.apache.spark.sql
import org.apache.spark.sql.{AnalysisException, DataFrame, SparkSession}
import org.apache.spark.sql.functions.{asc, avg, desc, max, mean, min, sum}


class CSVProcessor(spark:SparkSession, sc:SparkContext, var csvPath: String) {

  import spark.implicits._
  var csvFile : DataFrame = null

  @throws(classOf[AnalysisException])
  @throws(classOf[IllegalArgumentException])
  def readFile(): Unit = {
    csvFile = spark.read.format("csv").option("header", "true").load(csvPath)
    println("File Read Successfully")
  }

  @throws(classOf[NullPointerException])
  def totalNumberOfAthletes():Long = {
    return csvFile.select("ID").distinct.count
  }

  @throws(classOf[NullPointerException])
  def MedalWinners(medal:String):Unit = {
    csvFile.filter("medal = '"+medal+"'").distinct().show()
  }

  @throws(classOf[NullPointerException])
  def highestMedalsCountryYear(medal:String):Unit = {
    val temp = csvFile.filter("medal = '"+medal+"'")
      .groupBy("NOC","Year").count().orderBy(desc("count")).limit(1)

    if(temp.select("count").as[String].collect().isEmpty)throw new IllegalArgumentException
    else temp.show()
//    val z = temp.select("count").as[String].collect()(0)

  }

  @throws(classOf[NullPointerException])
  def findAverge(att:String,gender:String): Unit ={
   val temp = csvFile.filter("sex = '"+gender+"'").distinct().
      agg(avg(att)as("Average "+att+" of "+gender))

    if(temp.select("Average "+att+" of "+gender).as[String].collect()(0)==null)throw new IllegalArgumentException
    else temp.show()
  }

  @throws(classOf[NullPointerException])
  def genderRatio(): Unit= {
    val male = csvFile.filter("sex = 'M'").groupBy("Year","ID").count().drop("count")
      .groupBy("Year").count().withColumnRenamed("count","count M")

    val female = csvFile.filter("sex = 'F'").groupBy("Year","ID").count().drop("count")
      .groupBy("Year").count().withColumnRenamed("count","count F")

    male.join(female,"Year").withColumn("ratio Male",$"count M"/ ($"count F" + $"count M")).
      withColumn("ratio Female",$"count F"/ ($"count F" + $"count M")).drop("count M","count F").show()
  }

  @throws(classOf[NullPointerException])
  def medalDistributionByCountry():Unit={
    csvFile.filter("medal != 'NA'").groupBy("NOC").count().show()
  }

  @throws(classOf[NullPointerException])
  def mostPopularSport():Unit={
    csvFile.groupBy("Sport").count().orderBy(desc("count")).limit(1).show()
  }

}
