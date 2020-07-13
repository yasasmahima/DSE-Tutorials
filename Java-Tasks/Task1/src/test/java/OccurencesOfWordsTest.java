import counters.Counter;
import counters.OccurencesOfWords;
import counters.UniqeWordCount;
import org.junit.Test;
import records.Record;
import records.TextFileRecord;


public class OccurencesOfWordsTest {

    @Test
    public void shouldReturnUniqeWordCount_when_GiveAValidFile() {

        Record textRecord = new TextFileRecord("D:\\Zone24x7\\Java_Task1\\src\\main\\resources\\Java Task 01.txt");
        OccurencesOfWords map = new OccurencesOfWords(textRecord);
        System.out.println(map.wordCount());

    }
}
