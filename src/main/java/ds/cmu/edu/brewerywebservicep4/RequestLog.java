package ds.cmu.edu.brewerywebservicep4;

import java.time.LocalDateTime;
import java.util.List;

public class RequestLog {
    private LocalDateTime requestTimestamp;
    private String clientIpAddress;
    private String requestParameters;
    private LocalDateTime apiRequestTimestamp;
    private long apiResponseTime;
//    private String responseData;
    private List<String> breweryNames;

    // Constructor


    public RequestLog(LocalDateTime requestTimestamp, String clientIpAddress, String requestParameters,
                      LocalDateTime apiRequestTimestamp, long apiResponseTime, List<String> breweryNames) {
        this.requestTimestamp = requestTimestamp;
        this.clientIpAddress = clientIpAddress;
        this.requestParameters = requestParameters;
        this.apiRequestTimestamp = apiRequestTimestamp;
        this.apiResponseTime = apiResponseTime;
        this.breweryNames = breweryNames;
    }

    // Getters and setters for all fields

    public LocalDateTime getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(LocalDateTime requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public LocalDateTime getApiRequestTimestamp() {
        return apiRequestTimestamp;
    }

    public void setApiRequestTimestamp(LocalDateTime apiRequestTimestamp) {
        this.apiRequestTimestamp = apiRequestTimestamp;
    }

    public long getApiResponseTime() {
        return apiResponseTime;
    }

    public void setApiResponseTime(long apiResponseTime) {
        this.apiResponseTime = apiResponseTime;
    }


    public List<String> getBreweryNames() {
        return breweryNames;
    }

    public void setBreweryNames(List<String> breweryNames) {
        this.breweryNames = breweryNames;
    }
}
