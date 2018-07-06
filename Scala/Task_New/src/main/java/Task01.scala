import java.io.FileNotFoundException
import scala.io.Source
import org.apache.log4j.{BasicConfigurator, Level, Logger,PropertyConfigurator}


class Task01  {

  val LOGGER = Logger.getLogger(Task01.getClass)
  //BasicConfigurator.configure()
   try {
     PropertyConfigurator.configure("src/log4j.properties")
      val file = Source.fromFile("Scala Task 01.txt") //read file
      val x = file.getLines().toList.flatten //make flat map to get each character seperate

      LOGGER.info("Task 01 - Number of characters in the file : " + x.length)//logger info
      //printf("Hello")
    } catch {
      case e: FileNotFoundException => LOGGER.error("No file found") //logger error
    }

}

object Task01 extends App{
  val task = new Task01
}

