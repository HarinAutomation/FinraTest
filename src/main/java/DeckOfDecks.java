import java.util.ArrayList;

public class DeckOfDecks {

    ArrayList<Deck> deckOfDecks = new ArrayList<Deck>();
    int numOfDecks = 0;

    public void addToDeck(Deck deck){
        this.deckOfDecks.add(deck);
        numOfDecks++;
    }

    public Deck getDeckByIndex(int index){
        return deckOfDecks.get(index);
    }

    public int getNumOfDecks(){
        return numOfDecks;
    }

    public void removeDeck(Deck deck){
        this.deckOfDecks.remove(deck);
    }

}
