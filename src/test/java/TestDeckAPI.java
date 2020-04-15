
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestDeckAPI
{
    DeckOfDecks deckOfDecks = null;

    @Before
    public void init(){
        deckOfDecks = new DeckOfDecks();
    };

    @Test
    public void testDeckCreation(){
        Response response = Common.getInstance().generateNewDeck(false);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Assert.assertEquals(52,response.jsonPath().getInt("remaining"));

    }

    @Test
    public void testDeckCreationWithJockers(){
        Response response = Common.getInstance().generateNewDeck(true);
        Common.getInstance().verifyStatusCodes(response,200,"HTTP/1.1 200 OK");
        Assert.assertEquals(54,response.jsonPath().getInt("remaining"));
    }

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

    }

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

}

