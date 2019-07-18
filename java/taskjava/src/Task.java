import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Task {

    public static void main(String[] args) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("data/Java Task 01.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String words="";
        while(sc.hasNext()) words+=sc.next()+" ";

        String formatWords = words.replaceAll("[\\W&&[^\\s]&&[^-]&&[^++]]","").replaceAll("[0-9]","").toLowerCase();

        String[] singleWords= formatWords.split(" +");



        System.out.println("Number of words: "+singleWords.length);

        HashSet<String> unique=new HashSet<>();

        unique.addAll(Arrays.asList(singleWords));

        System.out.println("Number of unique words: "+unique.size());


        HashMap<String,Integer> occur = new HashMap<>();

        for (int i=0;i<singleWords.length;i++){

            String word = singleWords[i];
            if(!occur.containsKey(word)){

                occur.put(word,1);
            }else{
                int count = occur.get(word)+1;
                occur.remove(word);
                occur.put(word,count);
            }
        }

        System.out.println("\nOccurence of words:\n");
        for (Map.Entry m:occur.entrySet()){
            System.out.println(m.getKey()+" :"+m.getValue()+" times.");
        }



    }
}
