import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import scala.collection.immutable.ListMap

class ContentBasedRecommendation(usr_id:Int , dataPath:String, sc:SparkSession) extends RecommendationEngine(usr_id, dataPath, sc) {

  override var data: DataFrame = _

  private var arrangedData : DataFrame= _

  private var similarities  = Map[Double,(Int,String)]()

  import sc.implicits._


  override def predict(movieId: Int) = {

    ArrangeData

    val movieYear = FeatureScaling.extractYear(data.filter("movieId =="+ movieId).select("title").as[String].collect()(0))

    val year = data.select("movieId","title").map(t => (t.getInt(0),FeatureScaling.extractYear(t.getString(1))))

    val averageYear = year.toDF().filter("_2 != '"+0.0+"'").agg(avg("_2")).head().getDouble(0)

    val filteredYears = year.toDF().map(r=> (r.getInt(0),{
      if (r.getDouble(1) == 0.0) averageYear else r.getDouble(1)
    })).toDF()


    val yearDiff = FeatureScaling.GetMaxMinValues(filteredYears,"_2")

    val maxYearGap = Math.abs(yearDiff._2 - yearDiff._1)

    val yearGapcol = filteredYears.map(row => (row.getInt(0),
      1-(FeatureScaling.ScaleValue(Math.abs(row.getDouble(1) - movieYear) , 0 , maxYearGap)))).withColumnRenamed("_1", "movieId")
        .withColumnRenamed("_2", "yearGapInverse")

    arrangedData = arrangedData.join(yearGapcol, "movieId").drop("title")

    val temp1 = arrangedData.filter("movieId == "+movieId).drop("movieId").collect.map(_.toSeq).map(_.toArray)
    val sMovieFea = temp1(0).map(x=> FeatureScaling.extractDouble(x))

    val temp2 = arrangedData.filter("movieId != "+movieId).drop("movieId").collect.map(_.toSeq).map(_.toArray)
    val oMovieFea = temp2.map(row=> row.map(x=> FeatureScaling.extractDouble(x)))

    val temp3 = arrangedData.filter("movieId != "+movieId).select("movieId").collect().map(_.toSeq).map(_.toArray).map(row => row(0).asInstanceOf[Int])
    val temp4 = data.filter("movieId != "+movieId).select("title").collect().map(_.toSeq).map(_.toArray).map(row => row(0).toString)

    var index = 1

    for(index <- 1 to temp3.size){

      similarities += ( CosineSimilarity.cosineSimilarity(oMovieFea(index-1),sMovieFea) -> ( temp3(index-1) , temp4(index-1)))
    }

    similarities = ListMap(similarities.toSeq.sortWith(_._1 > _._1):_*)

  }


  def ArrangeData: Unit = {

    loadData

    var genresFiltered = data.map(row=> FeatureScaling.booleanGenres(row.getString(2))).toDF()

    data = data.withColumn("rowId1", monotonically_increasing_id())
    genresFiltered = genresFiltered.withColumn("rowId2", monotonically_increasing_id())

    arrangedData = data.as("df1").join(genresFiltered.as("df2"), data("rowId1") === genresFiltered("rowId2"), "inner")
      .drop("rowId1","rowId2","genres")
  }



  override def loadData: Unit = {
    data  = sc.read
          .format("csv")
          .option("header", "true")
          .option("inferSchema", "true")
          .load(dataPath+"movies.csv")
  }

  override def show(nOfPred: Int): Any = {
      similarities.take(nOfPred).foreach(println)
  }
}
