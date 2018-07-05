import java.io.*;
import java.util.Scanner;

public class Task01 {

    public static void main(String[] args) {

        FileReader fr = null;
        try {
            fr = new FileReader("Java Task 01.txt"); //read the file
            int wordsCount = countTheWords(fr); //pass the file reader to the method
            System.out.println("word count is: " + wordsCount); //prin the count

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int countTheWords(FileReader fr){
        Scanner sc = new Scanner(fr);//scan the file
        int count=0;
        while(sc.hasNext()){ //count the words by space
            sc.next();
            count++;
        }
        return count;
    }
}
