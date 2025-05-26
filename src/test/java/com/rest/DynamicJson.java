package com.rest;

import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class DynamicJson {

    @DataProvider(name = "bookData")
    public Object[][] getBookData() {
        return new Object[][]{
                {"abc", "1234", "Test Automation", "Andrei"},
                {"bcd", "5678", "API Testing", "John Doe"},
                {"xyz", "9012", "DevOps Handbook", "Jane Smith"}
        };
    }

    @Test(dataProvider = "bookData")
    public void addBook(String isbn, String aisle, String bookName, String author) {
        RestAssured.baseURI = ConfigLoader.get("baseURI");

        String requestBody = """
            {
              "name": "%s",
              "isbn": "%s",
              "aisle": "%s",
              "author": "%s"
            }
            """.formatted(bookName, isbn, aisle, author);

        String responseBody =
                given()
                        .log().all()
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .when()
                        .post("/Library/Addbook.php")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().asString();

        System.out.println("AddBook Response: " + responseBody);
    }

    @DataProvider(name = "bookIdData")
    public Object[][] getBookIdData() {
        return new Object[][]{
                {"abc", "1234"},
                {"bcd", "5678"},
                {"xyz", "9012"}
        };
    }

    @Test(dataProvider = "bookIdData")
    public void deleteBook(String isbn, String aisle) {
        RestAssured.baseURI = ConfigLoader.get("baseURI");

        String bookId = isbn + aisle;
        String requestBody = """
        {
          "ID": "%s"
        }
        """.formatted(bookId);

        String responseBody =
                given()
                        .log().all()
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .when()
                        .delete("/Library/DeleteBook.php")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().asString();

        System.out.println("DeleteBook Response: " + responseBody);
    }

    @Test
    public void addBook1() throws IOException {
        RestAssured.baseURI = ConfigLoader.get("baseURI");

        String filePath = "src/main/resources/Library/addBook.json";
        Path path = Paths.get(filePath);
        assertThat(Files.exists(path))
                .as("JSON file must exist: %s", filePath)
                .isTrue();

        String requestBody = Files.readString(path);

        String response =
                given()
                        .log().all()
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .when()
                        .post("/Library/Addbook.php")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().asString();

        System.out.println("AddBook Response: " + response);
    }

    @Test
    public void getBookById() {
        RestAssured.baseURI = ConfigLoader.get("baseURI");
        String bookId = "bcd2926";

        String response =
                given()
                        .log().all()
                        .queryParam("ID", bookId)
                        .when()
                        .get("/Library/GetBook.php")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().asString();

        System.out.println("GetBookByID Response: " + response);
    }
}
