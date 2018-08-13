import org.apache.spark.sql.{DataFrame, SparkSession}


class CollaborativeRecommendation(usr_id:Int , dataPath:String, sc:SparkSession) extends RecommendationEngine (usr_id, dataPath, sc) {

  import sc.implicits._

  override var data: DataFrame = _

  private var similarities =  List[(Int,String,Double)]()

  override def predict(movieId: Int) = {

    loadData

    val movies  = sc.read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(dataPath+"movies.csv").drop("genres")

    val ratDiff = FeatureScaling.GetMaxMinValues(data,"rating")
    val scaledRatings = data.map(row => (row.getInt(0),row.getInt(1),FeatureScaling.ScaleValue(row.getDouble(2),ratDiff._1-0.5,ratDiff._2))).toDF()
      .withColumnRenamed("_1","userId").withColumnRenamed("_2","movieId").join(movies,"movieId").drop("genres")

    val combinedData = data.join(scaledRatings,Seq("movieId", "userId"),"inner")
      .drop("rating").withColumnRenamed("_3","rating")

    val combinedData2 = combinedData.select("*").filter("movieId =="+movieId).withColumnRenamed("movieId","movieId2")
      .withColumnRenamed("rating", "rating2").drop("title")

    val cd = combinedData2.join(combinedData,"userId").drop("userId").filter("movieId !="+movieId)

    val cd2 = cd.rdd.groupBy(row=> (row.getInt(0),row.getInt(2),row.getString(4)))

    val cd3 = cd2.map(row => (row._1,row._2.map(row2=> (row2.getDouble(1),row2.getDouble(3)))))

    val cd4 = cd3.map(row=> (row._1,FeatureScaling.custom1(row._2.toList)))

    val cd5 = cd4.map(row=> (row._1._2,row._1._3, CosineSimilarity.cosineSimilarity(row._2(0).toArray, row._2(1).toArray))).collect().toList

    similarities = cd5.sortBy(_._3)(Ordering[Double].reverse)

  }


  override def loadData: Unit = {
    data  = sc.read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(dataPath+"ratings.csv")
      .drop("timestamp")
  }

  override def show(nOfPred: Int) = {
    similarities.take(nOfPred).foreach(println)
  }
}
