import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.{AnalysisException, SparkSession}
import SparkTask02.{spark,sc}
import org.scalatest.{BeforeAndAfter, FlatSpec, FunSuite}

class SampleTest extends FunSuite with BeforeAndAfter {

  private var sc: SparkContext = _
  private var rootLogger : Logger = _
  private var testCSVfile: CSVProcessor = _

  import spark.sqlContext.implicits._

//  before{
//    sc = spark.sparkContext // create spark context
//    rootLogger = Logger.getRootLogger() //get root logger and set level off
//    rootLogger.setLevelgu(Level.ERROR)
//  }

  test("Catching exception for null as csv_path"){
    testCSVfile = new CSVProcessor(spark,sc, null)
    intercept[IllegalArgumentException] {testCSVfile.readFile()}
  }

  test("Catching exception for Invalid Path"){
    testCSVfile = new CSVProcessor(spark,sc, "Invalid Path")
    intercept[AnalysisException] {testCSVfile.readFile()}
  }

  test("Checking if the file avoids duplicate IDs"){
    testCSVfile = new CSVProcessor(spark,sc, "src/test/resources/sample.csv")
    testCSVfile.readFile()
    assert(testCSVfile.totalNumberOfAthletes() === 6)
  }

  test("Checking if the popular sport is correct"){
    testCSVfile = new CSVProcessor(spark,sc, "src/test/resources/sample.csv")
    testCSVfile.readFile()
    assert(testCSVfile.mostPopularSport().equals("Speed Skating"))
  }

  test("Checking if average of an attrubute is correct"){
    testCSVfile = new CSVProcessor(spark,sc, "src/test/resources/sample.csv")
    testCSVfile.readFile()
    assert(testCSVfile.findAverge("Height","M")===179.33333333333334)
  }

  test("Checking if average of an invalid attribute"){
    testCSVfile = new CSVProcessor(spark,sc, "src/test/resources/sample.csv")
    testCSVfile.readFile()
    intercept[IllegalArgumentException] {testCSVfile.findAverge("Sex","M")}
  }

  test("Check if gender ratio is correct"){
    testCSVfile = new CSVProcessor(spark,sc, "src/test/resources/sample.csv")
    testCSVfile.readFile()
    val temp1 = Seq(("1994", 0.5, 0.5)).toDF("Year","ratio Male","ratio Female").collect()
    val temp = testCSVfile.genderRatio().filter("Year = 1994").collect()
    assert(temp === temp1)
  }


//
//  after{
//    if(sc != null)sc.stop()
//  }

}
