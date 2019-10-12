import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Settup {
    int currentHash = 0;
    int currentByteAdresses = 0;
    int currentByteIndex=0;
    int[] ABCarray = new int[27000];

    public static void main(String[] args) {

        Settup go = new Settup();
        try {
            go.readFromSort();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readFromSort() throws Exception{
        try (FileInputStream inputStream = new FileInputStream("teststort");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
             OutputStream outputStreamAdress = new BufferedOutputStream(new FileOutputStream("Adress"));
             OutputStream outputStreamIndex = new BufferedOutputStream(new FileOutputStream("Index"));
             OutputStream outputStreamABC = new BufferedOutputStream(new FileOutputStream("ABC"));


        ) {
            String currentWord = null; //TODO: initalisera current word
            String stringIn;



            while ((stringIn = bufferedReader.readLine()) != null) {
                String[] stringInArr = stringIn.split(" ");
                if (currentWord != null && stringInArr[0].equals(currentWord)) {  //om ordet vi kollar på är lika med ordet vi sparat:
                    addToAdresses(stringInArr[1] + " ", outputStreamAdress);        //så ska vi bara skicka vidare byteadressen som en sträng och lägga in den i filen adress
                }
                else {
                    currentWord = stringInArr[0]; //om orden är lika lägg till i adress filen samt i index
                    addToIndex(currentWord, outputStreamIndex);
                    addToAdresses("\n" + stringInArr[1] + " ", outputStreamAdress);
                }
            }
            arrayToFile(outputStreamABC);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void arrayToFile(OutputStream output) throws IOException {
        for (int i : ABCarray) {
            String adress = i + "\n";
            byte[] bytearr = adress.getBytes();
            output.write(bytearr);
        }
    }

    private void addToAdresses (String ByteAdress, OutputStream outputStreamAdress) throws Exception{
        byte[] bytearr = ByteAdress.getBytes("ISO-8859-1"); //Gör om från string till bytes
        outputStreamAdress.write(bytearr);    //skriver till adress
        currentByteAdresses = currentByteAdresses + bytearr.length;     //Uppdaterar nuvarande byteadress i adress filen.
    }

    private void addToIndex (String word, OutputStream outputStreamIndex) throws Exception{
        String string = word + " " + currentByteAdresses + "\n";
        byte[] bytearr = string.getBytes("ISO-8859-1");
        outputStreamIndex.write(bytearr); //skriver ordet i filen
        addToABC(word);
        currentByteIndex = currentByteIndex + bytearr.length;     //Uppdaterar nuvarande byteadress i index filen.
    }

    private void addToABC (String word) {
        int index = WordsToHash.takethree(word); ///hashningen som vi använde förut---
        if (index != currentHash) {
            for (int i = currentHash + 1; i <= index; i++) { //lägger in curentbyte på alla platser som blir "tomma"
                ABCarray[i] = currentByteIndex;

            }
        }
        currentHash = index;                            //sparar över nytt index
    }


}
