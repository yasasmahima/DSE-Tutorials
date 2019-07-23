package com.scalaTask.scala

import java.io.{File, FileNotFoundException, IOException, PrintWriter}

import scala.collection.immutable.HashMap
import scala.io.Source
import org.apache.log4j.{BasicConfigurator, Logger}


object WordFinder {
  def main(args: Array[String]): Unit = {

    var log=Logger.getLogger(getClass.getName)
    BasicConfigurator.configure()
    val filename = "./src/main/scala/com/scalaTask/scala/in/Scala Task 01.txt"

    var deletedSpaces :String=""
    log.info("Creating Hashmap")
    var hashMap: HashMap[Char,Integer]=new HashMap()

    log.info("Concatenating Lines")

    try{
      for (line <- Source.fromFile(filename).getLines()){
        deletedSpaces=deletedSpaces+line
      }
    }catch {
      case ex: FileNotFoundException=>{
        println("File Not Found")
        log.info("File Not Found")
      }
      case ex: IOException=>{
        println("IO Error")
        log.info("IO Error")
      }
    }


    deletedSpaces=deletedSpaces.replaceAll(" ","")
    log.info("Inserting individual character to array")
    val splitted=deletedSpaces.toArray


    println(deletedSpaces)
    log.info("Adding to hashMap")
    for (digit <- splitted){
      if (!hashMap.contains(digit)){
        hashMap+=(digit -> 1)
      }else{
        val accumulator=hashMap(digit)+1;
        hashMap+=(digit -> accumulator)
      }
    }


    log.info("Creating new Text file")

    try{
      val pw=new PrintWriter(new File("Characters.txt"))

      log.info("Writing to text file")

      pw.write("Character\tKey\n")
      for ((key,value)<-hashMap) {
        pw.write(key+"\t\t\t"+value+"\n")
      }
      pw.close()
    } catch{
      case exception: IOException=>{
        println("IOEXception")
        log.info("IO Exception occurred")

      }
    }
    println(s"Elements of Hashmap = $hashMap")
    println("The number of elements is "+hashMap.size)


  }
}
