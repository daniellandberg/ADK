/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
/* Modiefierat av Daniel Landberg och Fredrik Diffner            */
import java.util.List;

public class ClosestWords {
  StringBuilder stringBuilder = new StringBuilder();
  int closestDistance = 1000;
  String oldWord = "";


  int partDist(String w1, String w2, int w1len, int w2len, int[][] matrix) {
    //i rader, j kolumner. w1 = felstavat ord, w2 rättstavat
    //Matrisen redan definierad för alla i = 0 och j = 0.
    int res = 0; //resultatet
    int jstart = checkEq(w2, w2len); //Börja på den kolumn där första bokstaven som är olika från föregående ord finns
    int lowest = 0; //initierar kolumnens lägsta nummer

    //Yttre for-loop för varje kolumn
    for (int j = jstart; j <= w2len ;j++ ) {
      //Om kolumnens lägsta nummer > closestDistance kan vi bryta direkt. (Första gången är lowest = 0, träffar ej if-satsen)
      //Kollen görs i början på varje kolumn. lowest = förra kolumnens lägsta nummer
      if (lowest > closestDistance) return closestDistance + 1;

      lowest = matrix[0][j]; //Sätter lowest till cellen högst upp i kolumnen
      //Inre foor-loop för varje rad
      for (int i = 1; i <= w1len ;i++ ) {
        //Plats 0,0 i matrisen representerar tomma strängen. Så behöver ta i-1/j-1 för att komma åt motsvarande plats i ordet.
        if (w1.charAt(i-1) == w2.charAt(j-1)) res = matrix [i-1][j-1]; //Om vi har samma bokstav
        else res = matrix [i-1][j-1] + 1;
        int deleteLetter = matrix [i-1][j] + 1;
        int addLetter = matrix [i][j-1] + 1;

        if (res > addLetter) res = addLetter;
        if (res > deleteLetter) res = deleteLetter;
        matrix [i][j] = res;

        if (res < lowest) lowest = res; //Uppdaterar kolumnens minsta rad
      }
    }
    oldWord = w2; //Uppdaterar vilket ord som användes för att skapa senaste matrisen
    return res;
  }


  //Kontrollerar om nuvarande ordet liknar föregående ord, så att matrisen kan återanvändas.
  //Returnerar index på första kolumnen som skiljer i matrisen
  int checkEq(String word, int wordLen){
    int len = wordLen;
    if (oldWord.length() < wordLen) len = oldWord.length();   //Kontrollerar vilket ord som är kortast till for-loopen
    int eq = 0; //antalet lika bokstäver
    for (int i = 0; i < len-1; i++) {
      if (word.charAt(i) == oldWord.charAt(i)) eq++;
      else break;
    }
    return eq + 1;
  }


  public ClosestWords(String w, List<String> wordList, int[][] matrix) {
    for (String s : wordList) { //Jämför varje ord i ordlistan mot det felstavade
      int wLen = w.length();
      int sLen = s.length();
      //Om det rättstavade ordet är längre än det felstavade ordet plus minsta editeringsavståndet behöver vi inte räkna ut matrisen
      if (wLen + closestDistance < sLen) continue;
      else{
        int dist = partDist(w, s, wLen, sLen, matrix);
        if (dist < closestDistance) { //Om vi hittat ett nytt minsta editeringsavstånd nollställs stringbuildern, och vi gör en ny
          closestDistance = dist;
          stringBuilder.setLength(0);
          stringBuilder.trimToSize();
          stringBuilder.append(w);
          stringBuilder.append(" (");
          stringBuilder.append(dist);
          stringBuilder.append(") ");
          stringBuilder.append(s);
        }
        else if (dist == closestDistance) {
          stringBuilder.append(" ");
          stringBuilder.append(s);
        }
      }
    }
      //System.out.println("d(" + w + "," + s + ")=" + dist);
  }

  //Returnerar den ihopsatta stringbuildern
  StringBuilder getClosestWords() {
    return stringBuilder;
  }
}
