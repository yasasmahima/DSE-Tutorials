import java.io.{BufferedWriter, FileNotFoundException, FileWriter}

import scala.io.Source

object Task02 {
  def main(args: Array[String]): Unit = {

    try {
      val file = Source.fromFile("Scala Task 01.txt") //read file
      val x = file.getLines().toList.flatten //make flat map to get each character seperate
      println("Number of characters in the file : " + x.length)

      val y = x.filter (i => x.indexOf (i) == x.lastIndexOf (i))
      println("Number of unique characters in the file : " + y.length)

      val file2 = "Task02Result.txt"
      val writer = new BufferedWriter(new FileWriter(file2))
      writer.write("Number of unique characters in the file : " + y.length + "\n" + y)
      writer.close

    }catch{
      case e: FileNotFoundException => println("No file found")
    }
  }
}