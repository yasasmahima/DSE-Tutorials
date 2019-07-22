package hasal

import java.io.{BufferedWriter, FileWriter}
import org.apache.log4j.{Logger, BasicConfigurator}

object WordCount {

  def main(args: Array[String]): Unit ={

    val logger = Logger.getLogger(getClass.getName)
    BasicConfigurator.configure()
    logger.info("Program Starting")
    var lines :String = ""
    try {
      logger.info("Reading file: Scala Task 01.txt")
      val source = scala.io.Source.fromFile("in/Scala Task 01.txt")
      logger.info("Getting the whole file as a String")
      lines = try source.mkString finally source.close()
    }
    catch{
      case x: java.io.FileNotFoundException => {
        logger.info("Caught exception while reading file: Scala Task 01.txt")
        println("This file cannot be found")
      }
    }

    logger.info("Replacing all the irrelevant characters with empty strings")
    lines = lines.replaceAll("[0-9,\\(.)\"\'\n\t\\s]", "")
    lines = lines.replaceAll("-", "")

    logger.info("Getting every character to a list")
    val characterList = lines.toList
    logger.info("Printing the total number of characters")
    println("Number of characters: "+ characterList.size)

    logger.info("Getting every character to a set")
    val characterSet = lines.toSet
    val file = "out/uniqueCharacters.txt"

    logger.info("Writing unique characters to file: uniqueCharacters.txt")
    val writer = new BufferedWriter(new FileWriter(file))
    writer.write("Unique characters are : \n")
    for(character <- characterSet){
      writer.write(character+"\n")
    }
    writer.close()

    logger.info("Printing the number of unique characters")
    println("Number of unique characters: "+ characterSet.size)

    logger.info("Getting the occurrence of each character")
    val characterMap = characterList.groupBy(i=>i).mapValues(_.size)

    logger.info("Printing the occurrence of each character")
    println("Occurrences of characters: ")
    characterMap.foreach(tuple => println(tuple._1+" : "+tuple._2))

    logger.info("Program finished with exit code 0")

  }

}
