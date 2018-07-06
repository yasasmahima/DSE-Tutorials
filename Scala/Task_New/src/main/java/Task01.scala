import java.io.FileNotFoundException
import scala.io.Source
import org.apache.log4j.{Level, Logger}


trait LogHelper {
  val loggerName = this.getClass.getName
  lazy val logger = Logger.getLogger(loggerName)
}


class Task01 extends LogHelper {
   try {
      val file = Source.fromFile("Scala Task 01.txt") //read file
      val x = file.getLines().toList.flatten //make flat map to get each character seperate

      logger.info("Number of characters in the file : " + x.length)//logger info

    } catch {
      case e: FileNotFoundException => logger.error("No file found") //logger error
    }

}

object Task01 extends App{
  val task = new Task01
}

