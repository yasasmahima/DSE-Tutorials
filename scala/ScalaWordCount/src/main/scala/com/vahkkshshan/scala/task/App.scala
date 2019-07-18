package com.vahkkshshan.scala.task

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}

import org.apache.log4j._

import scala.collection.mutable
import scala.io.{BufferedSource, Source}

/**
 * Hello world!
 *
 */
object App  {

  def main(args: Array[String]): Unit = {


    val logger=Logger.getLogger(getClass.getName)
    BasicConfigurator.configure()
    logger.info("Beginning logging")


    logger.debug("creating a buffered source")
    var fileContents: BufferedSource=null

    try{

      fileContents = Source.fromFile("data/Scala Task 01.txt")
      logger.warn("buffered source is not closed")

      logger.debug("Reading File")

    }catch{
      case exception: FileNotFoundException => println(" File not found Exception ::: "+exception)
        logger.error("File wasn't found --"+ exception)

    }
    val words = fileContents.getLines.mkString

    logger.debug("Formatting words without any special characters")
    var formattedWords= "[\\W&&[^\\s]&&[^++]]".r replaceAllIn(words," ")
    formattedWords= "[0-9]".r replaceAllIn(formattedWords,"")
    formattedWords=" +".r replaceAllIn(formattedWords,"")


    logger.debug("Converting the entire paragraph to char array")
    val wordList=formattedWords.toList


    logger.debug("printing number of total characters to console")
    println("number of character:"+ wordList.length)


    logger.debug("Converting char array to set")
    val wordSet=wordList.toSet

    logger.debug("printing unique character to console")
    println("number of unique character : "+wordSet.size)


    logger.debug("Creating file to write")
    val file = new File("out/unique words")

    logger.debug("creating buffered writer")
    val bw = new BufferedWriter(new FileWriter(file))

    logger.debug("writing each unique character to file unique words ")
    bw.write("unique chracaters are:--\n")

    for(char <- wordSet){
      bw.write(char+"\n")
    }

    logger.warn("Buffered writer not closed")

    bw.close()
    logger.debug("Buffered writer closed")


    logger.debug("Creating empty mutable map")
    val occur = mutable.Map[Char,Integer]()

    logger.debug("adding key value pairs for character occurence")
    for(word <- wordList){

      if(!occur.contains(word)){
        occur+=(word -> 1)
      }else{
        val count = occur(word)+1
        occur.update(word,count)

      }
    }

    logger.debug("adding k ->v to map finished")

    logger.debug("printing mutable map with character occurence to console")
    println("printing word occurence: "+occur)

    fileContents.close()
    logger.debug("bufferedSource closed")
    logger.debug("end of the line - program exit 0")
  }

}
