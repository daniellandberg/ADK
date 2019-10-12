public class WordsToHash {

    public static int takethree(String word) { // hashningen
        char [] character = new char [3];
        for (int i = 2; i >= 0; i--) {
            if (word.length() < i+1 ) {
                character[i]= '`';
            }
            else {
                character[i]= word.charAt(i);
                if (word.charAt(i) == 'ä' ){ //kollar om det är å så måste vi ha specialfall
                    character[i]= '{';
                }
                if (word.charAt(i) == 'å' ){ //kollar om det är ä så måste vi ha specialfall
                    character[i]= '|';
                }
                if (word.charAt(i) == 'ö' ){ //kollar om det är ö så måste vi ha specialfall
                    character[i]= '}';
                }
            }
        }
        int hascode = lettersToNumber ( character[0], character[1], character[2]);
        return hascode;
    }

    private static int lettersToNumber(char a, char b, char c) {
        int hashcode = 900*(a-'a'+1) + 30*(b-'a'+1) + (c-'a'+1); //hashar med 30^2 30^1 30^0
        return hashcode;
    }
}
