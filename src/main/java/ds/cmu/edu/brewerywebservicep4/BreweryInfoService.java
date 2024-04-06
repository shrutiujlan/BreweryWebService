package ds.cmu.edu.brewerywebservicep4;//package ds.cmu.edu.brewerywebservicep4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebServlet(name = "BreweryInfoService", urlPatterns = {"/breweries"})
public class BreweryInfoService extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(BreweryInfoService.class);
    private static final String API_BASE_URL = "https://api.openbrewerydb.org/v1/breweries";
    private static final String MONGO_URI = "mongodb://sujlan:Power09876@ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017, ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017,ac-xtllk91-shard-00-02.vqdr82t.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1"; // Update with your MongoDB URI
    private static final String DATABASE_NAME = "brewery_logs";
    private static final String COLLECTION_NAME = "request_logs";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Processing request...");
        response.setContentType("application/json;charset=UTF-8");

        // Extracting request information
        LocalDateTime requestTimestamp = LocalDateTime.now();
        String clientIpAddress = request.getRemoteAddr();
        String requestParameters = request.getQueryString();
        LocalDateTime apiRequestTimestamp = LocalDateTime.now();
        long startTime = System.currentTimeMillis(); // Start timing

        // Get the location parameter from the request
        String location = request.getParameter("location");
        if (location == null || location.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"error\": \"Missing location parameter\"}");
            return;
        }

        // Construct the API URL with the location parameter
        String apiUrl = API_BASE_URL + "?by_state=" + location;

        try {
            // Send GET request to the OpenBreweryDB API
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            // Parse JSON response and extract brewery names
            List<String> breweryNames = extractBreweryNames(responseContent.toString());

            // Serialize brewery names to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String breweryNamesJson = objectMapper.writeValueAsString(breweryNames);

            // Set the response content type and write the brewery names JSON to the client
            response.setContentType("application/json");
            response.getWriter().println(breweryNamesJson);

            // Log API response time
            long endTime = System.currentTimeMillis(); // End timing
            long apiResponseTime = endTime - startTime;

            // Create RequestLog object
            RequestLog requestLog = new RequestLog(requestTimestamp, clientIpAddress, requestParameters,
                    apiRequestTimestamp, apiResponseTime, breweryNames);

            // Store RequestLog object in MongoDB
            storeRequestLog(requestLog);

        } catch (IOException e) {
            // Handle any errors that may occur during the HTTP request
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private List<String> extractBreweryNames(String jsonResponse) {
        List<String> breweryNames = new ArrayList<>();

        try {
            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Iterate through each brewery entry and extract the name
            for (JsonNode breweryNode : root) {
                String name = breweryNode.get("name").asText();
                breweryNames.add(name);
            }
        } catch (IOException e) {
            logger.error("Error extracting brewery names from JSON response: {}", e.getMessage());
        }

        return breweryNames;
    }

    private void storeRequestLog(RequestLog requestLog) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            // Access the database
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

            // Access the collection (or create it if it doesn't exist)
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Create a document with the RequestLog object fields
            Document document = new Document()
                    .append("requestTimestamp", requestLog.getRequestTimestamp().toString())
                    .append("clientIpAddress", requestLog.getClientIpAddress())
                    .append("requestParameters", requestLog.getRequestParameters())
                    .append("apiRequestTimestamp", requestLog.getApiRequestTimestamp().toString())
                    .append("apiResponseTime", requestLog.getApiResponseTime())
                    .append("responseData", requestLog.getBreweryNames());

            // Insert document into the collection
            collection.insertOne(document);

            logger.info("Request information stored in MongoDB.");

        } catch (Exception e) {
            logger.error("Error storing request information in MongoDB: {}", e.getMessage());
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "BreweryInfoService Servlet";
    }
}
