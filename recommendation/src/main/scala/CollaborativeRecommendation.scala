import com.twitter.scalding.Tsv
import org.apache.spark.sql.{DataFrame, SparkSession}


class CollaborativeRecommendation(usr_id:Int , dataPath:String, sc:SparkSession) extends RecommendationEngine (usr_id, dataPath, sc) {

  import sc.implicits._

  override var data: DataFrame = _

  override def predict(nOfPred : Int): String = ???

  override def run(movieId: Int): Unit = {
    return false
  }

  override def loadData: Unit = {

    val t = Tsv
    data  = sc.read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(dataPath)
      .drop("timestamp")
  }

  override def ArrangeData: Unit = {

    val ratDiff = FeatureScaling.GetMaxMinValues(data,"rating")
    val scaledRatings = data.map(row => FeatureScaling.ScaleValue(row.getDouble(2),ratDiff._1,ratDiff._2)).toDF().show(20)

  }


}
