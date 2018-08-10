import org.apache.spark.sql.{DataFrame, SparkSession}

abstract class RecommendationEngine(val usr_id : Int, val dataPath : String, var sc: SparkSession ){

  var data: DataFrame
  def predict(nOfPred : Int) : Any
  def run(int: Int) : Unit
  def loadData : Unit
  def ArrangeData : Unit
}
