/*Http Client*/
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*Gson*/
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*Save*/
import java.util.*;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class App {
    /*API Pull*/
    static String inline = "";
    static int responseCode;       
    static String[] urlNames = {"ranking", "matches", "opr"};
    static String[] urls = {"https://www.thebluealliance.com/api/v3/event/2023mdbet/rankings", 
                            "https://www.thebluealliance.com/api/v3/event/2023mdbet/matches",
                            "https://www.thebluealliance.com/api/v3/event/2023mdbet/oprs"};
    
    static String[] oprDataNames = {"oprs"};
    static String[] rankingDataNames = {"rankings"};
    static String[] matchesDataNames = {"alliances"};


    static HttpClient client;
    static HttpRequest request;
    static HttpResponse response;
    
    /*Gson Parser*/
    static Gson gson;
    static Reader reader;
    static Map<?, ?> map;

    public static void main(String[] args) throws Exception {
        pull(); //Pull Data
        //parse();    //Parse Data
        testParse();
    }
    
    static void getData(String name, String endpoint) throws Exception {
        /*Http Client Setup*/
        client = HttpClient.newHttpClient();
        request =   HttpRequest.newBuilder(URI.create(endpoint)).header("X-TBA-Auth-Key", "3cXAtKkiWqBmhkl2dSqNQm4scotKMTSTHkzWH2g93GahrlHYvRq1rqfggAHmWGit").build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
        /*Pull*/
        if(response.statusCode() != 200) {    //If response code isn't 200(success)
            throw new RuntimeException("HttpResponseCode: " + response.statusCode());
        }   
        else {
            saveToFile(name);

        }

    }

    static void pull() throws Exception {
        for (int i = 0; i < urlNames.length; i++){
            try {
                getData(urlNames[i], urls[i]);
                Thread.sleep(1000);
            } catch(RuntimeException | IOException e) {
                System.out.println("Failed with: " + e + "\nTrying again");
                Thread.sleep(1000);
                try {
                    getData(urlNames[i], urls[i]);
                    Thread.sleep(1000);
                } catch(RuntimeException | IOException f) {
                    System.out.println("Failed with: " + f + "\nAborting");
                    Thread.sleep(1000);
                }
            }
        }

    }

    static void parse() throws Exception {
        /*Parser Ssetup*/
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            for(String fileName : urlNames){
                /*Prepare*/
                reader = Files.newBufferedReader(Paths.get(fileName + ".json"));    //Get file   //Create a reader
                map = gson.fromJson(reader, Map.class);   //Convert json to map
                
                for (String dataName : getOPRDataNames()) {
                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        if(entry.getKey().equals(dataName)){
                            System.out.println(entry.getKey() + " = " + entry.getValue() + "\n");    //Print the map entries
            
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    static String[] getOPRDataNames() {
        return oprDataNames;
    }

    static String[] getRankingDataNames() {
        return rankingDataNames;
    }

    static String[] getMatchesRankingNames() {
        return matchesDataNames;
    }

    static void saveToFile(String name) {
        try (FileWriter fullPull = new FileWriter(name + ".json")) {
            fullPull.write(response.body().toString());
            fullPull.flush();
            System.out.println("Saved Full " + name + " Pull Successfully");
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    static void testParse() throws Exception {
        /*Parser Ssetup*/
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            /*Prepare*/
            reader = Files.newBufferedReader(Paths.get("opr.json"));    //Get file   //Create a reader
            map = gson.fromJson(reader, Map.class);   //Convert json to map
            
            for (Map.Entry<?, ?> entry : map.entrySet()) {  //For first(main) list
                if(entry.getKey().equals("oprs")){
                    System.out.println(entry.getKey());
                    for (Map.Entry<?, ?> oprEntry : map.entrySet()) {   //For data
                            System.out.println(oprEntry.getKey() + " = " + oprEntry.getValue() + "\n");    //Print the map entries
                    
                        }
        
                }
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

