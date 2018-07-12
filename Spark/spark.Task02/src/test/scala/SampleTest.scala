import junit.framework.Test
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.{AnalysisException, SparkSession}
import org.junit
import org.junit.{After, Before}
import SparkTask02.spark
import org.scalatest.{BeforeAndAfter, FlatSpec, FunSuite}

class SampleTest extends FunSuite with BeforeAndAfter {

  private var sc: SparkContext = _
  private var rootLogger : Logger = _
  private var testCSVfile: CSVProcessor = _

  before{
    sc = spark.sparkContext // create spark context
    rootLogger = Logger.getRootLogger() //get root logger and set level off
    rootLogger.setLevel(Level.ERROR)
  }

  test("Catching exception for null as csv_path"){
    testCSVfile = new CSVProcessor(spark,sc, null)
    intercept[IllegalArgumentException] {testCSVfile.readFile()}
  }

  test("Catching exception for Invalid Path"){
    testCSVfile = new CSVProcessor(spark,sc, "Invalid Path")
    intercept[AnalysisException] {testCSVfile.readFile()}
  }







  after{
    if(sc != null)sc.stop()
  }

}
