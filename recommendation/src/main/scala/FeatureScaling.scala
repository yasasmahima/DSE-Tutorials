import org.apache.spark.sql.functions.{min, _}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.Row


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
    case _ => {println("Invalid Arguement")
      -0.0}
  }

}
