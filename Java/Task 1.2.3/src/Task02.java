import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Task02 {
    public static void main(String[] args) {

        FileReader fr = null;
        try {
            fr = new FileReader("Java Task 01.txt"); //read the file
            HashMap<String, Integer> wordsCount = findUniqueWrds(fr); //pass the file reader to the method
            int counter = 0;

            for (Map.Entry<String, Integer> entry: wordsCount.entrySet()){
                if(entry.getValue() == 1){
                    counter++;
                }
            }

            System.out.println("number of unique words are: " + wordsCount.size()); //print the count


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int countTheWords(FileReader fr) throws IOException{
        Scanner sc = new Scanner(fr);//scan the file
        int count=0;
        while(sc.hasNext()){ //count the words by space
            sc.next();
            count++; //increment counter
        }
        return count;
    }

    public static HashMap<String,Integer> findUniqueWrds(FileReader fr) throws IOException{

        Scanner sc = new Scanner(fr);//scan the file
        HashMap<String,Integer> uniqueWords = new HashMap<>();
        while(sc.hasNext()){ //count the words by space
            String word = sc.next().toLowerCase();
            if (uniqueWords.containsKey(word)){
                uniqueWords.put(word,uniqueWords.get(word)+1); //increment counter
            }else{
                uniqueWords.put(word, 1); //add the word to the counter
            }
        }
        return uniqueWords;
    }
}
