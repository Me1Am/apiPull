import com.google.gson.Gson;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.HttpURLConnection;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;


public class App {
    public static String ranking = "https://www.thebluealliance.com/api/v3/event/2023mdbet/rankings";
    public static String insights = "https://www.thebluealliance.com/api/v3/event/2023mdbet/insights";
    public static String eventOPR = "https://www.thebluealliance.com/api/v3/event/2023mdbet/oprs";
    public static String matches = "https://www.thebluealliance.com/api/v3/event/2023mdbet/matches";

    public static void main(String[] args) throws Exception {
        pullAPI("Ranking", ranking);
        pullAPI("Insight", insights);
        pullAPI("OPR", eventOPR);
        pullAPI("Match", matches);

        /*Parse*/
        /*JSON parser setup
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("test.json"));    //Create a reader
            Map<?, ?> map = gson.fromJson(reader, Map.class);   //Convert json to map
            
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue() + "\n");    //Print the map entries
            }

            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();

        }*/

    
        
    }
    static void pullAPI(String name, String endpoint) throws Exception {
        /*Setup*/
        URL url = new URL(endpoint); 
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");   //Command
        conn.setRequestProperty("X-TBA-Auth-Key", "3cXAtKkiWqBmhkl2dSqNQm4scotKMTSTHkzWH2g93GahrlHYvRq1rqfggAHmWGit");  //Header
        conn.connect(); //Connect
        int responsecode = conn.getResponseCode();  //Get server response code
        String inline = "";        

        /*Pull*/
        if(responsecode != 200) {    //If response code isn't 200(success), then run
            throw new RuntimeException("HttpResponseCode: " +responsecode);

        }   
        else {
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                inline += sc.nextLine();
                inline += "\n";
            }
            sc.close();
        }

        /*Save to File*/
        try (FileWriter fullPull = new FileWriter(name+".json")) {
            fullPull.write(inline);
            fullPull.flush();
            System.out.println("Saved Full " + name + " Pull Successfully");
        
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

