import org.apache.spark.sql.{DataFrame, SparkSession}

abstract class RecommendationEngine(val usr_id : Int, val dataPath : String, var sc: SparkSession ){

  var data: DataFrame
  def predict(item: Int) : Any
  def loadData : Unit
  def show(nOfPred : Int) : Any
}
