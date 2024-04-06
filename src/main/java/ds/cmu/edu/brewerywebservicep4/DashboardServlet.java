package ds.cmu.edu.brewerywebservicep4;

import com.mongodb.client.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
//---------------------------------------------------------------------------
//@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
//public class DashboardServlet extends HttpServlet {
//
//    private static final Logger logger = LogManager.getLogger(DashboardServlet.class);
//    private static final String DATABASE_NAME = "brewery_logs";
//    private static final String COLLECTION_NAME = "request_logs";
//    private static final String MONGO_URI = "mongodb://sujlan:Power09876@ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017, ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017,ac-xtllk91-shard-00-02.vqdr82t.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1"; // Update with your MongoDB URI

//
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//
//        try {
//            // Retrieve analytics data and log data from MongoDB
//            List<String> analyticsData = retrieveAnalyticsData();
//            List<Document> logData = retrieveLogData();
//
//            System.out.println(logData);
//
//            // Generate HTML page for dashboard
//            out.println("<html><head><title>Dashboard</title></head><body>");
//            out.println("<h1>Operations Analytics</h1>");
//            out.println("<ul>");
//            for (String analyticsItem : analyticsData) {
//                out.println("<li>" + analyticsItem + "</li>");
//            }
//            out.println("</ul>");
//
//            out.println("<h1>Logs</h1>");
//            out.println("<table border=\"1\">");
//            out.println("<tr><th>Request Timestamp</th><th>Client IP Address</th><th>Request Parameters</th><th>API Request Timestamp</th><th>API Response Time</th><th>Response Data</th></tr>");
//            for (Document logEntry : logData) {
//                out.println("<tr>");
//                out.println("<td>" + logEntry.getProperty("requestTimestamp") + "</td>");
//                out.println("<td>" + logEntry.getProperty("clientIpAddress") + "</td>");
//                out.println("<td>" + logEntry.getProperty("requestParameters") + "</td>");
//                out.println("<td>" + logEntry.getProperty("apiRequestTimestamp") + "</td>");
//                out.println("<td>" + logEntry.getProperty("apiResponseTime") + "</td>");
//                out.println("<td>" + logEntry.getProperty("responseData") + "</td>");
//                out.println("</tr>");
//            }
//            out.println("</table>");
//            out.println("</body></html>");
//        } catch (Exception e) {
//            logger.error("Error generating dashboard: {}", e.getMessage());
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            out.println("<html><head><title>Error</title></head><body>");
//            out.println("<h1>Error</h1>");
//            out.println("<p>An error occurred while generating the dashboard.</p>");
//            out.println("</body></html>");
//        } finally {
//            out.close();
//        }
//    }
//
//    private List<String> retrieveAnalyticsData() {
//        List<String> analyticsData = new ArrayList<>();
//
//        // Example 1: Top 5 most requested states
//        analyticsData.add("Top 5 most requested states:");
//        List<Document> topStates = retrieveTopStates(5); // Implement this method to query MongoDB for the top states
//        for (Document state : topStates) {
//            analyticsData.add(state.getProperty("_id") + ": " + state.getProperty("count") + " requests");
//        }
//
//        // Example 2: Average API response time
//        long averageResponseTime = calculateAverageResponseTime(); // Implement this method to calculate the average response time
//        analyticsData.add("Average API response time: " + averageResponseTime + " milliseconds");
//
//        return analyticsData;
//    }
//
//    private List<Document> retrieveLogData() {
//        List<Document> logData = new ArrayList<>();
//
//        // Example: Retrieve the latest 10 log entries
//        MongoDatabase database = MongoClients.create(MONGO_URI).getDatabase(DATABASE_NAME);
//        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
//        logData = collection.find().limit(10).sort(new Document("requestTimestamp", -1)).into(new ArrayList<>());
//
//        return logData;
//    }
//
//    private List<Document> retrieveTopStates(int limit) {
//        List<Document> topStates = null;
//
//        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
//            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
//            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
//
//            // Aggregation pipeline to group requests by state and count them, then sort by count in descending order
//            List<Document> pipeline = Arrays.asList(
//                    new Document("$unwind", "$responseData"),
//                    new Document("$group", new Document("_id", "$requestParameters.location").append("count", new Document("$sum", 1))),
//                    new Document("$sort", new Document("count", -1)),
//                    new Document("$limit", limit)
//            );
//
//            // Execute the aggregation pipeline
//            AggregateIterable<Document> result = collection.aggregate(pipeline);
//
//            // Convert the result to a list
//            topStates = result.into(new ArrayList<>());
//        } catch (Exception e) {
//            logger.error("Error retrieving top states from MongoDB: {}", e.getMessage());
//        }
//
//        return topStates;
//    }
//} -----------------------
//@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
//public class DashboardServlet extends HttpServlet {
//    private static final Logger logger = LogManager.getLogger(DashboardServlet.class);
//    private static final String DATABASE_NAME = "brewery_logs";
//    private static final String COLLECTION_NAME = "request_logs";
//    private static final String MONGO_URI = "mongodb://sujlan:Power09876@ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017, ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017,ac-xtllk91-shard-00-02.vqdr82t.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1"; // Update with your MongoDB URI
//
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
//            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
//            MongoCollection<org.bson.Document> collection = database.getCollection(COLLECTION_NAME);
//
//            // Retrieve all documents from the collection
//            FindIterable<org.bson.Document> documents = collection.find();
//
//
//            // Generate HTML table
//            StringBuilder htmlTable = new StringBuilder();
//            htmlTable.append("<html><head><title>Dashboard</title></head><body><h1>Dashboard</h1><table border='1'><tr><th>ID</th><th>Request Timestamp</th><th>Client IP Address</th><th>Request Parameters</th><th>API Request Timestamp</th><th>API Response Time</th><th>Response Data</th></tr>");
//
//            for (Document doc : documents) {
//                htmlTable.append("<tr>");
//                htmlTable.append("<td>").append(doc.get("_id")).append("</td>");
//                htmlTable.append("<td>").append(doc.get("requestTimestamp")).append("</td>");
//                htmlTable.append("<td>").append(doc.get("clientIpAddress")).append("</td>");
//                htmlTable.append("<td>").append(doc.get("requestParameters")).append("</td>");
//                htmlTable.append("<td>").append(doc.get("apiRequestTimestamp")).append("</td>");
//                htmlTable.append("<td>").append(doc.get("apiResponseTime")).append("</td>");
//                htmlTable.append("<td>").append(doc.get("responseData")).append("</td>");
//                htmlTable.append("</tr>");
//            }
//
//            htmlTable.append("</table></body></html>");
//
//            // Set response content type
//            response.setContentType("text/html");
//
//            // Write HTML table to response
//            PrintWriter out = response.getWriter();
//            out.println(htmlTable.toString());
//
//        } catch (Exception e) {
//            logger.error("Error retrieving data from MongoDB: {}", e.getMessage());
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().println("Error retrieving data from MongoDB");
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Dashboard Servlet";
//    }
//}

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(DashboardServlet.class);
    private static final String DATABASE_NAME = "brewery_logs";
    private static final String COLLECTION_NAME = "request_logs";
    private static final String MONGO_URI = "mongodb://sujlan:Power09876@ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017, ac-xtllk91-shard-00-01.vqdr82t.mongodb.net:27017,ac-xtllk91-shard-00-02.vqdr82t.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Retrieve all documents from the collection
            List<Document> documents = collection.find().into(new ArrayList<>());

            // Calculate analytics
            List<String> top5Locations = AnalyticsCalculator.getTop5Locations(documents);
            double avgResponseTime = AnalyticsCalculator.getAvgResponseTime(documents);
            double avgBreweriesPerState = AnalyticsCalculator.getAvgBreweriesPerState(documents);

            // Generate HTML response
            StringBuilder htmlResponse = new StringBuilder();
            htmlResponse.append("<html><head><title>Dashboard</title></head><body>");

            // Operations Analytics
            htmlResponse.append("<h2>Operations Analytics</h2>");
            htmlResponse.append("<table border='1'>");
            htmlResponse.append("<tr><th>Top 5 Locations Searched</th></tr>");
            for (String location : top5Locations) {
                htmlResponse.append("<tr><td>").append(location).append("</td></tr>");
            }
            htmlResponse.append("</table>");
            htmlResponse.append("<p>Average API Response Time: ").append(avgResponseTime).append(" milliseconds</p>");
            htmlResponse.append("<p>Average Number of Breweries per State: ").append(avgBreweriesPerState).append("</p>");

            // Data Logs
            htmlResponse.append("<h2>Data Logs</h2>");
            htmlResponse.append("<table border='1'>");
            htmlResponse.append("<tr><th>ID</th><th>Request Timestamp</th><th>Client IP Address</th><th>Request Parameters</th><th>API Request Timestamp</th><th>API Response Time</th><th>Response Data</th></tr>");
            for (Document doc : documents) {
                htmlResponse.append("<tr>");
                htmlResponse.append("<td>").append(doc.get("_id")).append("</td>");
                htmlResponse.append("<td>").append(doc.get("requestTimestamp")).append("</td>");
                htmlResponse.append("<td>").append(doc.get("clientIpAddress")).append("</td>");
                htmlResponse.append("<td>").append(doc.get("requestParameters")).append("</td>");
                htmlResponse.append("<td>").append(doc.get("apiRequestTimestamp")).append("</td>");
                htmlResponse.append("<td>").append(doc.get("apiResponseTime")).append("</td>");
                htmlResponse.append("<td>").append(doc.get("responseData")).append("</td>");
                htmlResponse.append("</tr>");
            }
            htmlResponse.append("</table>");

            htmlResponse.append("</body></html>");

            // Set response content type
            response.setContentType("text/html");

            // Write HTML response to the output stream
            PrintWriter out = response.getWriter();
            out.println(htmlResponse.toString());

        } catch (Exception e) {
            logger.error("Error retrieving data from MongoDB: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving data from MongoDB");
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
        return "Dashboard Servlet";
    }
}
