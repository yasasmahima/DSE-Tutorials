package scalding

import com.twitter.scalding._


/**
  * @author Aathif Sanaya
  * @date 04.09.2018
  */
class MergeRecDataJob(args:Args) extends Job(args) {
  /**
    * args(propertiesPath) - path of the given File - eg: hdfs@\\172.16.16/properties.file
    * args(outputPath) - Path for the output result - eg: hdfs@\\172.16.16/tmp/output.csv
    * args(PathDelimeter) - Seperation of Path in the input File - eg: ,
    * args(ContentDelimeter) - Seperation of Key Values  - eg: \"\t"" , \"""\s+""""
    * args("validateData") - Flag if Column should be equal or not - eg : true/any
    */



  /**
    * Check Arguement for checkColumnCount
    * throw exception if invalid Argument occured
    */
  private val checkColumnCount = if (args("validateData").toLowerCase == "true") true
                                else false


  /**
    * Validate ,Read Properties file and Flatten each path seperated by given delimeter
    * Convert TypedPipe to List
    */
  private val propertiesFilePipe = TypedPipe.from(TextLine(args("propertiesPath"))).filter{line => line != "" }.flatMap(line => line.split(args("PathDelimeter")))
  private val PathArray = propertiesFilePipe.toIterableExecution.waitFor(this.scaldingConfig,this.mode).get.toList

  private var mergedFile = getPipe(PathArray.head)  //Get first File as Typed pipe

  private val No_of_cols = getListFromPipe(mergedFile)(0).size // Get Number of elements in file


  /**
    * Loop until all files are unioned with the first file
    * throw error if no.of.elements in files are unequal
    */
  private var a = 1
  for( a <- 1 until PathArray.length){

    val nextPipe = getPipe(PathArray(a))

    if(getListFromPipe(nextPipe)(0).size != No_of_cols) {
      if (checkColumnCount) throw new Error("Unequal Number of elements in files for the Given delemeter " + args("ContentDelimeter"))
    }
    mergedFile = mergedFile++nextPipe
  }


  /**
    * Write output Pipe according to given File seperating delimeter
    */
    mergedFile.map(row =>  makeStringValues(args("ContentDelimeter"),row)).write(TypedTsv(args("outputPath")))(this.flowDef, this.mode)


  /**
    * Get TypedPipe from given path
    * Filter out null lines and split according to given delimeter
    * @param path FilePath
    * @return Typed Pipe
    */
  private def getPipe(path:String) : TypedPipe[Array[String]] = {
    val pipe = TypedPipe.from(TextLine(path))
      .filter{ line => line != "" }
      .map { line => line.split(args("ContentDelimeter"))
      }

    pipe
  }


  /**
    * Get String representation of Array with given regrex
    * @param regrex delimeter
    * @param arr String Array
    * @return String representation
    */
  private def makeStringValues(regrex:String , arr:Array[String]) : String = {
    var s = ""
    var i = 0
    for ( i <- 0 until arr.size){
      if(i == arr.size - 1) s = s + arr(i)
      else s = s + arr(i) + regrex
    }
    s
  }


  /**
    * Get converted List of the given Pipe
    * @param typedPipe pipe to be converted to List
    * @return List representation of pipe
    */
  private def getListFromPipe(typedPipe: TypedPipe[Array[String]]) : List[Array[String]] = {
    typedPipe.toIterableExecution.waitFor(this.scaldingConfig,this.mode).get.toList
  }

}
