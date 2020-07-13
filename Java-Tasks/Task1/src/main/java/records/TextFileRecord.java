package records;

import java.io.*;

public class TextFileRecord implements Record {

    private final String path;

    public TextFileRecord(String path) {
        this.path = path;

    }

    public BufferedReader getReader() {

        BufferedReader bufferedReader = null;
        try {

            FileReader fileReader = new FileReader(path);
             bufferedReader= new BufferedReader(fileReader);

        }catch (FileNotFoundException e){

            System.out.println("File is not valid");

        }
        return bufferedReader;
    }



}
