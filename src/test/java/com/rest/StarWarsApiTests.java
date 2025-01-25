package com.rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class StarWarsApiTests {

    @Test
    public void getPersonDetails() {
        // Base URL
        RestAssured.baseURI = "https://swapi.dev/api";

        // GET request for Luke Skywalker
        Response response = given()
                .log()
                .all()
                .when()
                .get("/people/1")
                .then()
                .statusCode(200)  // Assert status code
                .extract()
                .response();

        // Print the full response JSON to the console
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate the name in the response
        String name = response.jsonPath().getString("name");
        Assert.assertEquals(name, "Luke Skywalker", "Name does not match!");
    }

    @Test
    public void getPeopleWithQueryParams() {
        // Base URL
        RestAssured.baseURI = "https://swapi.dev/api";

        // Perform GET request with a query parameter for pagination
        Response response = given()
                .queryParam("page", 1)  // Add query parameter ?page=1
                .when()
                .get("/people")
                .then()
                .statusCode(200)  // Assert status code is 200
                .extract()
                .response();

        // Print the response JSON to the console
        System.out.println("Response Body: " + response.asPrettyString());

        // Validate that the response contains people data
        int count = response.jsonPath().getInt("count");
        System.out.println("Total People Count: " + count);
        Assert.assertTrue(count > 0, "Count should be greater than 0!");

        // Print the first person’s name from the response
        String firstPerson = response.jsonPath().getString("results[0].name");
        System.out.println("First Person Name: " + firstPerson);
    }
}
