package com.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HTTPBinApiTests {

    @Test
    public void getRequestWithoutAuth() {
        // Base URL
        RestAssured.baseURI = "https://httpbin.org";

        // Perform GET request
        Response response = given()
                .when()
                .get("/get")
                .then()
                .statusCode(200) // Assert HTTP status code is 200
                .extract()
                .response();

        // Print the response
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate headers or data in the response
        Assert.assertNotNull(response.jsonPath().getString("url"), "URL field should not be null!");
    }

    @Test
    public void postRequestWithoutAuth() {
        // Base URL
        RestAssured.baseURI = "https://httpbin.org";

        // JSON payload
        String requestBody = """
                {
                    "name": "John Doe",
                    "job": "Developer"
                }""";

        // Perform POST request
        Response response = given()
                .contentType(ContentType.JSON) // Set Content-Type header
                .body(requestBody)             // Attach payload
                .when()
                .post("/post")
                .then()
                .statusCode(200) // HTTPBin echoes the POST payload, so 200 is expected
                .extract()
                .response();

        // Print the response
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate the echoed payload
        String echoedName = response.jsonPath().getString("json.name");
        Assert.assertEquals(echoedName, "John Doe", "Name field does not match!");
    }

    @Test
    public void deleteRequestWithoutAuth() {
        // Base URL
        RestAssured.baseURI = "https://httpbin.org";

        // Perform DELETE request
        Response response = given()
                .when()
                .delete("/delete")
                .then()
                .statusCode(200) // HTTPBin echoes back DELETE, so 200 is expected
                .extract()
                .response();

        // Print the response
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate the URL in the response
        Assert.assertNotNull(response.jsonPath().getString("url"), "URL should be present in the response!");
    }

    @Test
    public void getRequestWithAuth() {
        // Base URL
        RestAssured.baseURI = "https://httpbin.org";

        // Username and password
        String username = "user";
        String password = "passwd";

        // Perform GET request with Basic Auth
        Response response = given()
                .auth()
                .basic(username, password) // Use Basic Authentication
                .when()
                .get("/basic-auth/" + username + "/" + password)
                .then()
                .statusCode(200) // 200 indicates successful authentication
                .extract()
                .response();

        // Print the response
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate authentication success
        boolean authenticated = response.jsonPath().getBoolean("authenticated");
        Assert.assertTrue(authenticated, "Authentication should be successful!");
    }

    @Test
    public void postRequestWithBearerAuth() {
        // Base URL
        RestAssured.baseURI = "https://httpbin.org";

        // Bearer token
        String token = "your_bearer_token";

        // JSON payload
        String requestBody = """
                {
                    "message": "Hello, authenticated world!"
                }""";

        // Perform POST request with Bearer Token
        Response response = given()
                .header("Authorization", "Bearer " + token) // Add Bearer token
                .contentType(ContentType.JSON)             // Set Content-Type
                .body(requestBody)                         // Attach payload
                .when()
                .post("/post")
                .then()
                .statusCode(200) // HTTPBin echoes the payload, so 200 is expected
                .extract()
                .response();

        // Print the response
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate the echoed payload
        String echoedMessage = response.jsonPath().getString("json.message");
        Assert.assertEquals(echoedMessage, "Hello, authenticated world!", "Message does not match!");
    }
}
