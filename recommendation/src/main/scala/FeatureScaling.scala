import org.apache.spark.sql.functions.{min, _}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.Row
import scala.collection.mutable.ArrayBuffer


object FeatureScaling {

  def extractYear(str : String) : Double = {
    val p = "(19|20)\\d\\d".r
    val l1 = p.findAllIn(str).toList
      if (l1.isEmpty) 0 else l1.last.toDouble
  }

  def ScaleValue(att: Double , minScale:Double, maxScale:Double): Double = {
    (att - minScale)/(maxScale - minScale)
  }


  def GetMaxMinValues(data: DataFrame, colName:String) = {

    val min_max = data.agg(min(colName), max(colName)).head()
    val minimum = min_max.getDouble(0)
    val maximum = min_max.getDouble(1)
    (minimum,maximum)
  }

  def booleanGenres(genres:String) ={
    (
      if (genres.contains("no genres listed"))1.0 else 0.0,
      if(genres.contains("Action")  )1.0 else 0.0,
      if(genres.contains("Adventure") )1.0 else 0.0,
      if(genres.contains("Animation") )1.0 else 0.0,
      if(genres.contains("Children") )1.0 else 0.0,
      if(genres.contains("Comedy") )1.0 else 0.0,
      if( genres.contains("Crime") )1.0 else 0.0,
      if(genres.contains("Documentary") )1.0 else 0.0,
      if(genres.contains("Drama") )1.0 else 0.0,
      if(genres.contains("Fantasy") )1.0 else 0.0,
      if( genres.contains("Film-Noir") )1.0 else 0.0,
      if( genres.contains("Horror") )1.0 else 0.0,
      if( genres.contains("Musical") )1.0 else 0.0,
      if(genres.contains("Mystery") )1.0 else 0.0,
      if( genres.contains("Romance") )1.0 else 0.0,
      if( genres.contains("Sci-Fi") )1.0 else 0.0,
      if( genres.contains("Thriller") )1.0 else 0.0,
      if( genres.contains("War") )1.0 else 0.0,
      if(genres.contains("Western") )1.0 else 0.0,
    )
  }

  def extractDouble(x: Any): Double = x match {
    case n: java.lang.Number => n.doubleValue()
    case _ => {println("Invalid Arguement - "+ x)
      -0.0}
  }

  def toArray(x:Double):Array[Double] = {
    var temp = new Array[Double](1)
    temp(0) = x
    temp
  }

  def custom1(list: List[(Double,Double)]) : Array[ArrayBuffer[Double]] = {
    val arr = new Array[ArrayBuffer[Double]](2)
    arr(0) = new ArrayBuffer[Double]()
    arr(1) = new ArrayBuffer[Double]()
    list.foreach(d1 => (arr(0).append(d1._1)  , arr(1).append(d1._2)))
    arr
  }

}
