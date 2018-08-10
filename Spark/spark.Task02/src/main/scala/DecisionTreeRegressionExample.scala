
package org.apache.spark.examples.mllib

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils
// $example off$

object DecisionTreeRegressionExample {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder() //create spark session
      .appName("Spark Task 02")
      .master("local[*]")
      .getOrCreate()
    val sc = spark.sparkContext

    // $example on$
    // Load and parse the data file.
    val data = MLUtils.loadLibSVMFile(sc, "src/main/resources/sample_libsvm_data.txt")
    // Split the data into training and test sets (30% held out for testing)
    val splits = data.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))

    // Train a DecisionTree model.
    //  Empty categoricalFeaturesInfo indicates all features are continuous.
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "variance"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainRegressor(trainingData, categoricalFeaturesInfo, impurity,
      maxDepth, maxBins)

    // Evaluate model on test instances and compute test error
    val labelsAndPredictions = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    labelsAndPredictions.foreach(println)
    val testMSE = labelsAndPredictions.map{ case (v, p) => math.pow(v - p, 2) }.mean()
    println(s"Test Mean Squared Error = $testMSE")
    println(s"Learned regression tree model:\n ${model.toDebugString}")

    // Save and load model
//    model.save(sc, "target/tmp/myDecisionTreeRegressionModel")
//    val sameModel = DecisionTreeModel.load(sc, "target/tmp/myDecisionTreeRegressionModel")
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
