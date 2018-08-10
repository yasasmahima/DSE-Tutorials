import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors

object KMeansExample {

  def main(args: Array[String]) {

    val spark: SparkSession = SparkSession.builder() //create spark session
      .appName("Spark Task 02")
      .master("local[*]")
      .getOrCreate()
    val sc = spark.sparkContext

    // $example on$
    // Load and parse the data
    val data = sc.textFile("src/main/resources/kmeans_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()


    // Cluster the data into two classes using KMeans
    val numClusters = 2
    val numIterations = 20
    val clusters = KMeans.train(parsedData, numClusters, numIterations)


    val predictions = parsedData.map{r => (r, clusters.predict(r ))}

    predictions.foreach(println)
    // convert the rdd to a dataframe

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Save and load model
//    clusters.save(sc, "target/org/apache/spark/KMeansExample/KMeansModel")
//    val sameModel = KMeansModel.load(sc, "target/org/apache/spark/KMeansExample/KMeansModel")
    // $example off$

    sc.stop()
  }
}