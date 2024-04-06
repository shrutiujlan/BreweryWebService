package ds.cmu.edu.brewerywebservicep4;

import java.util.*;
import org.bson.Document;

public class AnalyticsCalculator {

    public static List<String> getTop5Locations(List<Document> documents) {
        Map<String, Integer> locationCount = new HashMap<>();

        for (Document doc : documents) {
            String requestParameters = doc.getString("requestParameters");
            String[] params = requestParameters.split("=");
            if (params.length == 2 && params[0].equals("location")) {
                String state = params[1];
                locationCount.put(state, locationCount.getOrDefault(state, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedLocations = new ArrayList<>(locationCount.entrySet());
        sortedLocations.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> top5Locations = new ArrayList<>();
        for (int i = 0; i < Math.min(5, sortedLocations.size()); i++) {
            top5Locations.add(sortedLocations.get(i).getKey());
        }

        return top5Locations;
    }

    public static double getAvgResponseTime(List<Document> documents) {
        long totalResponseTime = 0;
        for (Document doc : documents) {
            totalResponseTime += doc.getLong("apiResponseTime");
        }
        return (double) totalResponseTime / documents.size();
    }

    public static double getAvgBreweriesPerState(List<Document> documents) {
        Map<String, Integer> stateBreweryCount = new HashMap<>();

        for (Document doc : documents) {
            List<String> breweries = (List<String>) doc.get("responseData");
            String requestParameters = doc.getString("requestParameters");
            String[] params = requestParameters.split("=");
            if (params.length == 2 && params[0].equals("location")) {
                String state = params[1];
                stateBreweryCount.put(state, stateBreweryCount.getOrDefault(state, 0) + breweries.size());
            }
        }

        int totalStates = stateBreweryCount.size();
        int totalBreweries = stateBreweryCount.values().stream().mapToInt(Integer::intValue).sum();

        return (double) totalBreweries / totalStates;
    }
}
