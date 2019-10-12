/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Main {

  public static List<String> readWordList(BufferedReader input) throws IOException {
    LinkedList<String> list = new LinkedList<String>();
    while (true) {
      String s = input.readLine();
      if (s.equals("#"))
        break;
      list.add(s);
    }
    return list;
  }

  public static void main(String args[]) throws IOException {
      //  long t1 = System.currentTimeMillis();
    int matrixSize = 35;
    int[][] matrix = new int[matrixSize][matrixSize];
    //Definiera hela första raden och första kolumnen
    for (int i = 0; i < matrixSize; i++){
      matrix[i][0] = i;
      matrix[0][i] = i;
    }
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    // Säkrast att specificera att UTF-8 ska användas, för vissa system har annan
    // standardinställning för teckenkodningen.
    List<String> wordList = readWordList(stdin);
    String word;
    while ((word = stdin.readLine()) != null) {
      ClosestWords closestWords = new ClosestWords(word, wordList, matrix);
      System.out.println(closestWords.getClosestWords());
    }
      //  long tottime = (System.currentTimeMillis() - t1);
      //  System.out.println("CPU time: " + tottime + " ms");

  }
}
