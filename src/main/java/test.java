import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Simple Java example showing:
 * - Making HTTP GET requests to REST APIs
 * - Using GSON to parse JSON responses into Java objects
 * - How Java handles JSON serialization/deserialization
 */
public class test {

    // POJO (Plain Old Java Object) class that maps to the Cat API response
    // GSON automatically maps JSON fields to Java object properties
    public static class Cat {
        public String id;
        public String url;
        public int width;
        public int height;

        @Override
        public String toString() {
            return "Cat{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    // Method to make HTTP GET request and return response as String
    public static String getHttpResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Set request method to GET
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Get response code (200 = success)
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read response body
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    // Method to fetch random cat and convert to Java object using GSON
    public static Cat getRandomCat() throws Exception {
        String url = "https://api.thecatapi.com/v1/images/search";
        
        // Step 1: Make HTTP GET request
        String jsonResponse = getHttpResponse(url);
        System.out.println("\nRaw JSON Response: " + jsonResponse);

        // Step 2: Use GSON to convert JSON String to Java objects
        Gson gson = new Gson();
        
        // The API returns an array, so we parse it as an array
        Cat[] cats = gson.fromJson(jsonResponse, Cat[].class);
        
        // Return the first cat from the array
        return cats.length > 0 ? cats[0] : null;
    }

    public static void main(String[] args) {
        try {
            System.out.println("=== Java REST API Call with GSON Demo ===\n");

            // Example 1: Fetch a random cat
            System.out.println("Example 1: Fetching a random cat...");
            Cat randomCat = getRandomCat();
            
            if (randomCat != null) {
                System.out.println("\nParsed Cat Object (GSON converted JSON to Java):");
                System.out.println(randomCat);
                System.out.println("Accessing individual fields:");
                System.out.println("  - Cat ID: " + randomCat.id);
                System.out.println("  - Image URL: " + randomCat.url);
                System.out.println("  - Size: " + randomCat.width + "x" + randomCat.height);
            }

            // Example 2: Fetch cats with limit parameter
            System.out.println("\n\nExample 2: Fetching 3 cats with query parameter...");
            String urlWithLimit = "https://api.thecatapi.com/v1/images/search?limit=4";
            String jsonResponse = getHttpResponse(urlWithLimit);
            
            Gson gson = new Gson();
            Cat[] multipleCats = gson.fromJson(jsonResponse, Cat[].class);
            
            System.out.println("Got " + multipleCats.length + " cats:");
            for (int i = 0; i < multipleCats.length; i++) {
                System.out.println((i + 1) + multipleCats[i].url);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
