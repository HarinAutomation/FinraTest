import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.junit.Assert;

public class Common {

    final String BASEURI = "http://deckofcardsapi.com/api/deck";
    private static Common common = null;
    public RequestSpecification httpRequest = null;


    private Common(){
        RestAssured.baseURI = BASEURI;
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy() {
            protected boolean isRedirectable(String method) {
                return true;
            }
        };

        RestAssured.config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().httpClientFactory(() -> {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setRedirectStrategy(redirectStrategy);
            return httpClient;
        }));

        httpRequest= RestAssured.given();
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
        if(isJockerEnabled) {
            Response response =  httpRequest.queryParam("jokers_enabled", "true").contentType(ContentType.JSON).post("/new");

            return response;
        }else
        {
            return httpRequest.when().get("/new");
        }
    }

    public Response drawNewCard(String deckId,int numberOfCards){
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
