import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

public class Common {

    final String BASEURI = "http://deckofcardsapi.com/api/deck";
    private static Common common = null;
    public RequestSpecification httpRequest = null;

    private Common(){
        RestAssured.baseURI = BASEURI;
    }

    public static Common getInstance(){
        if(common == null){
            return new Common();
        }
        else {
            return common;
        }
    }

    public Response generateNewDeck(boolean isJockerEnabled){
        //https://deckofcardsapi.com/api/deck/new/
        httpRequest= RestAssured.given();
        httpRequest.header("Content-Type", "application/json");
        if(isJockerEnabled) {
            return httpRequest.queryParams("jokers_enabled", "true").get("/new");
        }else
        {
            return httpRequest.when().get("/new");
        }
    }

    public Response drawNewCard(String deckId,int numberOfCards){
        httpRequest= RestAssured.given();
        //https://deckofcardsapi.com/api/deck/<<deck_id>>/draw/
        return httpRequest.queryParam("count",numberOfCards).get("/"+deckId+"/draw");
    }

    public void verifyStatusCodes(Response response,int statusCode, String statusLine ){
        Assert.assertEquals("application/json",response.header("Content-Type"));
        Assert.assertEquals("gzip",response.header("Content-Encoding"));
        Assert.assertEquals(statusCode,response.getStatusCode());
        Assert.assertEquals(statusLine,response.getStatusLine());
    }
}
