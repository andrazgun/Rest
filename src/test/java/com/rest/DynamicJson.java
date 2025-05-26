import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class DynamicJson {

    @Test
    public void addBook() throws IOException {
        RestAssured.baseURI = "https://rahulshettyacademy.com/";

        String filePath = "src/main/resources/Library/addBook.json";
        assertThat(Files.exists(Paths.get(filePath)))
                .as("JSON file must exist: %s", filePath)
                .isTrue();

        String requestBody = Files.readString(Paths.get(filePath));

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
        RestAssured.baseURI = "https://rahulshettyacademy.com/";
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
