import java.util.ArrayList;
import java.util.HashMap;

public class Deck {

    String DeckId;
    boolean isJockerEnabled;
    int numOfCardsLeft;
    ArrayList<String> piles;

    public boolean isJockerEnabled() {
        return isJockerEnabled;
    }

    public void setJockerEnabled(boolean jockerEnabled) {
        isJockerEnabled = jockerEnabled;
    }

    public int getNumOfCardsLeft() {
        return numOfCardsLeft;
    }

    public void setNumOfCardsLeft(int numOfCardsLeft) {
        this.numOfCardsLeft = numOfCardsLeft;
    }

    public String getDeckId() {
        return DeckId;
    }

    public void setDeckId(String deckId) {
        DeckId = deckId;
    }

    public void addNewPile(String pileName){
        this.piles.add(pileName);
    }

}
