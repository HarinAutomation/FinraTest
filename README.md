# Finra Test
## Install Java
1. Download Java SDK 1.8 
        https://www.oracle.com/java/technologies/javase-jdk8-downloads.html
2. Install Java 
3. Set environment variable with JAVA_HOME with jdk Location and Add also JAVA_HOME/bin to the $PATH

## How to Run Testcases
 
        1.Install Maven https://maven.apache.org/download.cgi
        2. set MAVEN_HOME and add MAVEN_HOME to the $PATH
        3. check if maven is installed properly mvn -version
        4. clone the code using git clone https://github.com/HarinAutomation/FinraTest.git
        5. navigate to the FinraTest folder and make sure you see pom.xml file  (ls -al pom.xml )
        6. run "mvn test" via command prompt
  
        
## Implementation of Testcases 

        We are utilizing the API http://deckofcardsapi.com/ and created testcases 
        for the following Operations:

        Creating a new Deck of cards : 
        https://deckofcardsapi.com/api/deck/new/

        Create a new Deck Of cards with POST Call to include Jockers: 
        https://deckofcardsapi.com/api/deck/new/?jokers_enabled=true

        Note: Rest assured doesn't support redirects by default, added new configs to make the post call   

        Draw 'n' number of cards from a Deck 
        https://deckofcardsapi.com/api/deck/<<deck_id>>/draw/?count=<numOfCardsToBeDrawn>

        Created 11 Testcases to validated different scenario for these operations
        with all the validation and has more scope to validate the responses with POJO's. 

## Results:


                        -------------------------------------------------------
                         T E S T S
                        -------------------------------------------------------
                        Running DeckAPITest
                        Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.155 sec

                        Results :

                        Tests run: 11, Failures: 0, Errors: 0, Skipped: 0

                        [INFO] ------------------------------------------------------------------------
                        [INFO] BUILD SUCCESS
                        [INFO] ------------------------------------------------------------------------
                        [INFO] Total time:  9.808 s
                        [INFO] Finished at: 2020-04-16T11:26:48-05:00
                        [INFO] ------------------------------------------------------------------------


