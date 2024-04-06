<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
</head>
<body>
<h1>Dashboard</h1>

<h2>Operations Analytics</h2>
<table border="1">
    <tr>
        <th>Top 5 Locations Searched</th>
    </tr>
    <c:forEach var="location" items="${top5Locations}">
        <tr>
            <td>${location}</td>
        </tr>
    </c:forEach>
</table>

<p>Average API Response Time: ${avgResponseTime} milliseconds</p>
<p>Average Number of Breweries per State: ${avgBreweriesPerState}</p>

<h2>Data Logs</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Request Timestamp</th>
        <th>Client IP Address</th>
        <th>Request Parameters</th>
        <th>API Request Timestamp</th>
        <th>API Response Time</th>
        <th>Response Data</th>
    </tr>
    <c:forEach var="document" items="${documents}">
        <tr>
            <td>${document._id}</td>
            <td>${document.requestTimestamp}</td>
            <td>${document.clientIpAddress}</td>
            <td>${document.requestParameters}</td>
            <td>${document.apiRequestTimestamp}</td>
            <td>${document.apiResponseTime}</td>
            <td>${document.responseData}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
