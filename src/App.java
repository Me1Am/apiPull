/*Gson*/
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;

/*API Pull and Save*/
import java.util.*;
import java.net.URL;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;

public class App {
    /*API Pull*/
    static String inline = "";
    static int responseCode;       
    static String[] urlNames = {"ranking", "matches"};
    static String[] urls = {"https://www.thebluealliance.com/api/v3/event/2023mdbet/rankings", 
                            "https://www.thebluealliance.com/api/v3/event/2023mdbet/matches"};
                            
    static URL url;
    static Scanner sc;
    static String failedPulls;
    static HttpURLConnection conn;
    
    /*Gson Parser*/
    static Gson gson;
    static Reader reader;

    static Map<?, ?> map;

    public static void main(String[] args) throws Exception {
        pull(); //Pull
        
    }
    
    static void apiPull(String name, String endpoint) throws Exception {
        /*Setup*/
        url = new URL(endpoint);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");   //Command
        conn.setRequestProperty("X-TBA-Auth-Key", "3cXAtKkiWqBmhkl2dSqNQm4scotKMTSTHkzWH2g93GahrlHYvRq1rqfggAHmWGit");  //Header
        conn.connect();
        responseCode = conn.getResponseCode();

        /*Pull*/
        if(responseCode != 200) {    //If response code isn't 200(success)
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }   
        else {
            sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                inline += sc.nextLine();
                inline += "\n";
            }
            sc.close();
        }
        saveToFile(name);

    }

    static void pull() throws Exception {
        failedPulls = "";

        for (int i = 0; i <= urlNames.length; i++){
            try {
                apiPull(urlNames[i], urls[i]);
                Thread.sleep(1000);
            } catch(RuntimeException | IOException e) {
                System.out.println("Failed with: " + e + "\nTrying again");
                Thread.sleep(1000);
                try {
                    apiPull(urlNames[i], urls[i]);
                    Thread.sleep(1000);
                } catch(RuntimeException | IOException f) {
                    System.out.println("Failed with: " + f + "\nAborting");
                    failedPulls.concat("\n" + urlNames[i]);    //Add failed pull to list
                    Thread.sleep(1000);
                }
            }
        }
        System.out.println("Failed pulls: " + failedPulls);

    }

    static void parse() throws Exception {
        /*Parser Ssetup*/
        try {
            gson = new Gson();
            reader = Files.newBufferedReader(Paths.get("test.json"));    //Create a reader
            map = gson.fromJson(reader, Map.class);   //Convert json to map
    
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue() + "\n");    //Print the map entries
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void saveToFile(String name) {
        try (FileWriter fullPull = new FileWriter(name + ".json")) {
            fullPull.write(inline);
            fullPull.flush();
            System.out.println("Saved Full " + name + " Pull Successfully");
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}

