
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;


public class DeckAPITest
{
    /*
        Test case 1 : Generate a new Deck without jockers
     */
    @Test
    public void testDeckCreation(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Assert.assertEquals(52,response.jsonPath().getInt("remaining"));
        Assert.assertNotNull(response.jsonPath().getString("deck_id"));
        Assert.assertTrue(response.jsonPath().getBoolean("success"));
        Assert.assertFalse(response.jsonPath().getBoolean("shuffled"));
    }

    /*
        Test case 2 : Generate a new Deck with jockers using a POST call.
     */
    @Test
    public void testDeckCreationWithJockers(){
        Response response = Common.getInstance().generateNewDeck(true);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Assert.assertEquals(54,response.jsonPath().getInt("remaining"));
        Assert.assertNotNull(response.jsonPath().getString("deck_id"));
        Assert.assertTrue(response.jsonPath().getBoolean("success"));
        Assert.assertFalse(response.jsonPath().getBoolean("shuffled"));

    }

    /*
        Test case 3 : Generate a new Deck and Draw a card and validate the response
     */
    @Test
    public void drawNumberOfCards(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),1);
        Common.getInstance().verifyStatusCodes(drawCardResponse,200,"HTTP/1.1 200 OK");
        Assert.assertEquals(response.jsonPath().getInt("remaining")-1 , drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertTrue(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(1,drawCardResponse.jsonPath().getList("cards.code").size());
        Assert.assertNotNull(drawCardResponse.jsonPath().getString("cards[0].code"));
        Assert.assertNotNull(drawCardResponse.jsonPath().getString("cards[0].image"));
        Assert.assertNotNull(drawCardResponse.jsonPath().getString("cards[0].value"));
        Assert.assertNotNull(drawCardResponse.jsonPath().getString("cards[0].suit"));
        Assert.assertNotNull(drawCardResponse.jsonPath().getString("cards[0].images.svg"));
        Assert.assertNotNull(drawCardResponse.jsonPath().getString("cards[0].images.png"));
    }

    /*
        Test case 4 : Generate a new Deck and Draw all the cards and then try to draw a card on an empty deck.
     */

    @Test
    public void drawNumberOfCardsWithZeroCardsAvailable(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),52);
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),1);
        Assert.assertEquals(0, drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertFalse(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(0,drawCardResponse.jsonPath().getList("cards.code").size());
        Assert.assertEquals("Not enough cards remaining to draw 1 additional",drawCardResponse.jsonPath().getString("error"));
    }


    /*
            Test case 5 : Generate a new Deck and Draw a card with a negative count (-1) expected to remove all the cards except 1 card.
    */
    @Test
    public void drawNumberOfCardsWithNegativeCount(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),-1);
        Assert.assertEquals(1, drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertTrue(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(51,drawCardResponse.jsonPath().getList("cards.code").size());
    }


    /*
                Test case 6 : Generate a new Deck
                 Draw a card with a count as (0)
                 expected to remove zero cards and retrun the remaining card count
                 without an error.
        */
    @Test
    public void drawNumberOfCardsWithZeroCount(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),0);
        Assert.assertEquals(response.jsonPath().getInt("remaining"), drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertTrue(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(0,drawCardResponse.jsonPath().getList("cards.code").size());
    }

    /*
                Test case 7 : Generate a new Deck
                 Draw a card with a count as negative value with more than the deck size
                 expected to remove zero cards and retrun the remaining card count
                 without an error.
        */

    @Test
    public void drawNumberOfCardsWithGreaterNegCount(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),-58);
        Assert.assertEquals(response.jsonPath().getInt("remaining"), drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertTrue(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(0,drawCardResponse.jsonPath().getList("cards.code").size());
    }


    /*
         Test case 8 : Generate a new Deck
                 Draw a card with a count  with more than the deck size
                 expected to remove all cards and retrun the remaining card count as 0
                 with an error saying "Not enough cards remaining to draw 58 additional"
     */
    @Test
    public void drawNumberOfCardsWithGreaterCount(){
        Response response = Common.getInstance().generateNewDeck(true);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),58);
        Assert.assertEquals(0, drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertFalse(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(response.jsonPath().getInt("remaining"),drawCardResponse.jsonPath().getList("cards.code").size());
        Assert.assertEquals("Not enough cards remaining to draw 58 additional",drawCardResponse.jsonPath().getString("error"));
    }

    /*
        Test case 9 : Generate a new Deck and Draw all the cards and then try to draw a card on an empty deck with count with a negative value
     */

    @Test
    public void drawNumberOfCardsWithNegativeCountOnEmptyDeck(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),52);
        Response drawCardResponse = Common.getInstance().drawNewCard(response.jsonPath().get("deck_id").toString().trim(),-1);
        Assert.assertEquals(0, drawCardResponse.jsonPath().getInt("remaining"));
        Assert.assertEquals(response.jsonPath().getString("deck_id"),drawCardResponse.jsonPath().getString("deck_id"));
        Assert.assertTrue(drawCardResponse.jsonPath().getBoolean("success"));
        Assert.assertEquals(0,drawCardResponse.jsonPath().getList("cards.code").size());
    }

     /*
        Test case 10 :Invalid Count value to draw a card from a deck
     */

    @Test
    public void invalidCountValue(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCardForInvalidCase(response.jsonPath().get("deck_id").toString().trim(),"test");
        Assert.assertEquals(500,drawCardResponse.getStatusCode());
        Assert.assertEquals("HTTP/1.1 500 Internal Server Error",drawCardResponse.getStatusLine());
    }

     /*
        Test case 11 :Invalid Count value to draw a card from a deck
     */

    @Test
    public void invalidDeckIdValue(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Response drawCardResponse = Common.getInstance().drawNewCardForInvalidCase("testtswt","1");
        Assert.assertEquals(500,drawCardResponse.getStatusCode());
        Assert.assertEquals("HTTP/1.1 500 Internal Server Error",drawCardResponse.getStatusLine());
    }

}

