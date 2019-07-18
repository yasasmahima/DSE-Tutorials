package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Tester {
    public static void main(String[] args) throws FileNotFoundException {

        File file =new File("./src/in/Java Task 01.txt");
        Scanner sc=new Scanner(file);
        HashMap<String, Integer> listOfWords = new HashMap<>();
        String delimiters=" ";

        List<String> wordList=new ArrayList<>();

        while (sc.hasNextLine()){

            String[] words=sc.nextLine().split(delimiters);

            wordList.addAll(Arrays.asList(words));

        }
        for (int i = 0; i < wordList.size(); i++) {
            String updatedWord = wordList.get(i);
            updatedWord = updatedWord.replaceAll("[,()\"\"]","").replaceAll("\\[\\d+\\]","");
            // remove typical characters
            // remove numbers like [99]
            updatedWord = updatedWord.replaceAll("\\.$","").toLowerCase();
            //to prevent decimals being ignored
            wordList.set(i, updatedWord);

            if (!listOfWords.containsKey(updatedWord)){
                listOfWords.put(updatedWord, 1);
            }else{
                listOfWords.put(updatedWord,listOfWords.get(updatedWord)+1);
            }

        }
        for (String i : listOfWords.keySet()) {
            System.out.println(i+" : "+listOfWords.get(i));
        }

        System.out.println("The size is "+wordList.size());










    }
}
