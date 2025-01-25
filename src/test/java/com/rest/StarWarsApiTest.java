package com.rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class StarWars {

    @Test
    public void getPersonDetails() {
        // Base URL
        RestAssured.baseURI = "https://swapi.dev/api";

        // GET request for Luke Skywalker
        Response response = given()
                .when()
                .get("/people/1")
                .then()
                .statusCode(200)  // Assert status code
                .extract()
                .response();

        // Validate the name in the response
        String name = response.jsonPath().getString("name");
        Assert.assertEquals(name, "Luke Skywalker", "Name does not match!");
    }
}
