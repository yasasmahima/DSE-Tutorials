import counters.Counter;
import counters.OccurencesOfWords;
import counters.UniqeWordCount;
import counters.WordCount;
import records.Record;
import records.TextFileRecord;

import java.util.Map;

public class App {
    public static void main(String[] args) {

        Record textRecord = new TextFileRecord("D:\\Zone24x7\\Java_Task1\\src\\main\\resources\\Java Task 01.txt");

//        Word Count
        Counter wordCount = new WordCount(textRecord);
        System.out.println("Total Number of Words in the Text File is : "+wordCount.getWordCount());

//        Unique Word Count
        wordCount=new UniqeWordCount(textRecord);
        System.out.println("Total Number of  unique words in the Text File is : "+wordCount.getWordCount());

//        Occurrences of Words
        OccurencesOfWords map = new OccurencesOfWords(textRecord);
        for (Map.Entry<String,Integer> entry : map.wordCount().entrySet()) {
            System.out.println(entry.getKey()+"=>"+entry.getValue());

        }


    }
}
