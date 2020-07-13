import counters.Counter;
import counters.OccurencesOfWords;
import counters.WordCount;
import org.junit.Test;
import records.Record;
import records.TextFileRecord;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WordCounterTest {

    @Test
    public void shouldReturnCorrectWordCount_when_GiveAValidFile()  {

        Record textRecord = new TextFileRecord("D:\\Zone24x7\\Java_Task1\\src\\main\\resources\\Java Task 01.txt");
        Counter wordCount = new WordCount(textRecord);

        assertThat(wordCount.getWordCount(),is(149));

    }




}
