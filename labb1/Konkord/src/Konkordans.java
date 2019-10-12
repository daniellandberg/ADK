import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Konkordans {

    static int[]  ABCarray = new int [27001];
    public static void main(String[] args) throws Exception {
        String input = args[0].toLowerCase();
        readABC();
        findValues(input);
    }
    private static void readABC()  { //läser in fil och skapar en array med alla olika hash som vi skapade i settup
        try (FileInputStream inputStream = new FileInputStream("ABC");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         ) {
            String stringIn;


            int i = 0;
            while ((stringIn = bufferedReader.readLine()) != null) { //läs tills tom
                ABCarray[i]= Integer.parseInt(stringIn);
                i++;
                }

        }catch (IOException e) {
                System.out.println(e);
                 }
    }
    private static void findValues(String word) throws Exception { // Tar ut rätt positoner så att vi ska kunna binärsöka senare.
        RandomAccessFile Index = new RandomAccessFile("Index", "r"); //använder randomacessfile för att ha tillgång till seek()och getpointer()
        int wordhash = WordsToHash.takethree(word);
        int min = ABCarray[wordhash]; //tar ut en min och en max. Därmellan vet vi att ordet ligger om det finns
        int max = ABCarray[wordhash+1];
        binSearch(min, max, word, Index); //skickar vidare till binärsök
        }

    private static void binSearch(int min, int max, String word, RandomAccessFile Index) throws Exception {
        int mid = ((max-min)/2) +min; //tar ut mid mellan alla orden i adress
        Index.seek(mid);
        Index.readLine();
        mid = (int) Index.getFilePointer();  //uppdaterar mid till raden vi läser från
        String[] midword = Index.readLine().split(" "); //delar på mellanslag
        if ((max-min) < 1000) { //använder linjärsök när det är mindre än 1000 bytes
            linearSearch(min, max, word, Index);
        }
        else if (word.compareTo(midword[0]) < 0) { // använder compare. ger neg eller pos värde beroende på om strängen är större eller mindre
            max = mid;
            binSearch(min, max, word, Index); //uppdaterar max och bin söker igen
        }
        else if (word.compareTo(midword[0]) > 0) { // använder compare. ger neg eller pos värde beroende på om strängen är större eller mindre
            min = mid;
            binSearch(min, max, word, Index); //uppdaterar max och bin söker igen
        }
        else {
            int byteAdress = Integer.parseInt(midword[1]); // om vi inte träffar någon av ovanstående else if så står vi rakt på ordet
            String[] midPlus1 = Index.readLine().split(" "); //skapar lista med [ord,adress]
            int byteAdressPlus1;
            if (midPlus1 == null) { //om det skulle vara sista ordet i filen (specialfall)
              byteAdressPlus1 = (int) Index.length();  // längd på filen
            } else {
              byteAdressPlus1 = Integer.parseInt(midPlus1[1]); //gör om adress från byte till int för midPlus1
            }
            printKonkordans(byteAdress, byteAdressPlus1);
        }
    }

    private static void linearSearch(int min, int max, String word, RandomAccessFile Index) throws Exception {
        Index.seek(min);
        while ((int) Index.getFilePointer() < max) { // för alla ord under max
            String[] tempWord = Index.readLine().split(" ");
            if (word.compareTo(tempWord[0]) == 0){ // jämför orden och tar endast ut om det är en match, dvs det är ordet vi söker
              int byteAdress = Integer.parseInt(tempWord[1]); //isf tar vi ut vår adress och gör till int
              String[] tempWordPlus1 = Index.readLine().split(" ");
              int byteAdressPlus1;
              if (tempWordPlus1 == null) { //om det skulle vara sista ordet i filen (specialfall)
                byteAdressPlus1 = (int) Index.length();  //längd på filen
              } else {
                byteAdressPlus1 = Integer.parseInt(tempWordPlus1[1]); //gör om adress från byte till int för tempplus1
              }
              printKonkordans(byteAdress, byteAdressPlus1);
              return;
            }
        }
        System.out.println("The word is not present in the text");
    }

    private static void printKonkordans(int byteAdress, int byteAdressPlus1) throws Exception{
        RandomAccessFile Adress = new RandomAccessFile("Adress", "r");
        Adress.seek(byteAdress+1); //tittar på adressen på nästkommande
        int byteSeq = byteAdressPlus1 - byteAdress; //får en int på hur lång vår sekvens av byteadresser är
        byte[] byteArray = new byte[byteSeq]; //skapar en bytearray lika lång som vår sekvens av adresser är
        Adress.readFully(byteArray); //readfully av en bytearray betyder att vi läser just dem bytesen. Tack vare seek 2 rader upp börjar vi på rätt ställe
        String[] adresses = new String(byteArray).split("\\s"); //splittar på \\s typ " " och sparar som array
        int occurrences = adresses.length; // tar längden av listan ovan vilket ger oss hur många gånger ordet finns i filen
        System.out.println("Occurrences: " + adresses.length);
        if (occurrences > 25) {
          System.out.println("Do you wanna print them all? (y/n)");
          Scanner scanner = new Scanner(System.in);
          String input = scanner.next();
          if (input.equals("n")){
            return;
          }
          else if (!input.equals("y")){
            System.out.println("What?");
            return;
          }
        }
        String[] text = getText(adresses); //kallar på gettext för att få tillbaka orden. Kommer som 30 byte + ord + 30 byte på varje plats i arrayen
        for (String textLine : text){
            System.out.println(textLine);
        }

    }

    private static String[] getText(String[] adresses) throws Exception {
        RandomAccessFile Text = new RandomAccessFile("korpus", "r");
        String[] textArray = new String[adresses.length];
        for (int i = 0; i < adresses.length; i++){
          int byteLength = adresses[i].length(); //tar ut längden av ordet
          int byteAdress = Integer.parseInt(adresses[i]); //gör om från byte till int
          int byteStart = byteAdress - 30; //tar byteadressen och hoppar tillbaka 30 bytes för att få omfång om var ordet ligger i texten
          if (byteStart < 0){ //specialfall om det är tidigt ord
            byteStart = 0;
          }
          int byteStop = byteAdress+ byteLength + 30; //gör precis som med bytestart fast gåt fram 30 bytes
          if (byteStop > (int) Text.length()){ //specialfall. om vi går över längden av filen
            byteStop = (int) Text.length();
          }
          int byteSeq = byteStop - byteStart; //tar ut delta för att få en långd vad vi vill läsa
          byte[] byteSeqArray = new byte[byteSeq]; //gör en bytearray av den längden
          Text.seek(byteStart); //seekar på rätt ställe i texten, dvs bytestart
          Text.readFully(byteSeqArray);   //läser exakt så många bytes som vi sparat på byteSeqArray
          String textString = new String(byteSeqArray, "ISO-8859-1").replace("\n", " "); //gör till sträng samt ersätt radbry med " "
          textArray[i] = textString; // lägger in i textarray för att retunera
        }
        return textArray;
    }


}
