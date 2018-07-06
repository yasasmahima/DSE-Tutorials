import java.io.{BufferedWriter, FileNotFoundException, FileWriter}

import scala.io.Source

object Task03 {
  def main(args: Array[String]): Unit = {

    try {
      val file = Source.fromFile("Scala Task 01.txt") //read file
      val x = file.getLines().toList.flatten //make flat map to get each character seperate
      println("Number of characters in the file : " + x.length)

      val y = x.filter (i => x.indexOf (i) == x.lastIndexOf (i))
      println("Number of unique characters in the file : " + y.length)

      val file2 = "Task02Result.txt"
      val writer = new BufferedWriter(new FileWriter(file2))
      writer.write("Number of unique characters in the file : " + y.length + "\n" + y) //print result to a text file
      writer.close

      val z = x.groupBy(identity).mapValues(_.size) //occurance for each character in a Map
      println(z)

    }catch{
      case e: FileNotFoundException => println("No file found")
    }
  }
}