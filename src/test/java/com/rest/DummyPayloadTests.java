package com.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyPayloadTests {

    private JsonNode rootNode;
    private JsonPath jsonPath;

    @BeforeClass
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.readTree(new File("src/main/resources/dummyPayload.json"));

        String jsonContent = Files.readString(Paths.get("src/main/resources/dummyPayload.json"));
        jsonPath = new JsonPath(jsonContent);
    }

    @Test
    public void printNumberOfCoursesJsonNode() {
        JsonNode courses = rootNode.path("courses");
        int numberOfCourses = courses.size();
        System.out.println("Number of courses returned by API: " + numberOfCourses);
        assertThat(numberOfCourses)
                .as("Check courses array is not empty")
                .isGreaterThan(0);
    }

    @Test
    public void printPurchaseAmount1() {
        int purchaseAmount = rootNode.path("dashboard").path("purchaseAmount").asInt();
        System.out.println("Purchase Amount: " + purchaseAmount);
        assertThat(purchaseAmount).isGreaterThan(0);
    }

    @Test
    public void printTitleOfFirstCourse1() {
        JsonNode firstCourse = rootNode.path("courses").get(0);
        String firstCourseTitle = firstCourse.path("title").asText();
        System.out.println("Title of the first course: " + firstCourseTitle);
        assertThat(firstCourseTitle)
                .isNotEmpty()
                .as("First course title should not be empty");
    }

    @Test
    public void printAllCourseTitlesAndPrices1() {
        JsonNode courses = rootNode.path("courses");
        System.out.println("All course titles and their prices:");
        for (JsonNode course : courses) {
            String title = course.path("title").asText();
            int price = course.path("price").asInt();
            System.out.println("Course: " + title + ", Price: " + price);

            assertThat(title).isNotEmpty();
            assertThat(price).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    public void printCopiesSoldByRPACourse1() {
        JsonNode courses = rootNode.path("courses");
        int copiesSold = -1;

        for (JsonNode course : courses) {
            String title = course.path("title").asText();
            if ("RPA".equalsIgnoreCase(title)) {
                copiesSold = course.path("copies").asInt();
                break;
            }
        }
        System.out.println("Number of copies sold by RPA course: " + copiesSold);
        assertThat(copiesSold)
                .as("Copies sold by RPA course should be greater than zero")
                .isGreaterThan(0);
    }

    @Test
    public void verifyPurchaseAmountEqualsSumOfPriceTimesCopies1() {
        int purchaseAmount = rootNode.path("dashboard").path("purchaseAmount").asInt();
        JsonNode courses = rootNode.path("courses");
        int total = 0;
        for (JsonNode course : courses) {
            int price = course.path("price").asInt();
            int copies = course.path("copies").asInt();
            total += price * copies;
        }
        assertThat(purchaseAmount)
                .as("Check purchaseAmount equals sum of price * copies in courses")
                .isEqualTo(total);
    }

    @Test
    public void printNumberOfCoursesJsonPath() {
        List<?> courses = jsonPath.getList("courses");
        System.out.println("Number of courses returned by API: " + courses.size());
        assertThat(courses)
                .as("Courses list should not be empty")
                .isNotEmpty();
    }

    @Test
    public void printPurchaseAmount2() {
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase Amount: " + purchaseAmount);
        assertThat(purchaseAmount)
                .as("Purchase amount should be a positive integer")
                .isGreaterThan(0);
    }

    @Test
    public void printTitleOfFirstCourse2() {
        String firstTitle = jsonPath.getString("courses[0].title");
        System.out.println("Title of the first course: " + firstTitle);
        assertThat(firstTitle)
                .as("First course title should not be empty")
                .isNotBlank();
    }

    @Test
    public void printAllCourseTitlesAndPrices2() {
        List<String> titles = jsonPath.getList("courses.title");
        List<Integer> prices = jsonPath.getList("courses.price");

        System.out.println("All course titles and their prices:");
        for (int i = 0; i < titles.size(); i++) {
            System.out.println("Course: " + titles.get(i) + ", Price: " + prices.get(i));
            assertThat(titles.get(i)).isNotBlank();
            assertThat(prices.get(i)).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    public void printCopiesSoldByRPACourse2() {
        List<String> titles = jsonPath.getList("courses.title");
        List<Integer> copies = jsonPath.getList("courses.copies");

        int rpaCopies = -1;
        for (int i = 0; i < titles.size(); i++) {
            if ("RPA".equalsIgnoreCase(titles.get(i))) {
                rpaCopies = copies.get(i);
                break;
            }
        }
        System.out.println("Copies sold by RPA course: " + rpaCopies);
        assertThat(rpaCopies)
                .as("Copies sold by RPA course must be greater than 0")
                .isGreaterThan(0);
    }

    @Test
    public void verifyPurchaseAmountEqualsSumOfPriceTimesCopies2() {
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        List<Integer> prices = jsonPath.getList("courses.price");
        List<Integer> copies = jsonPath.getList("courses.copies");

        int total = 0;
        for (int i = 0; i < prices.size(); i++) {
            total += prices.get(i) * copies.get(i);
        }

        System.out.println("Calculated Total: " + total);
        System.out.println("Purchase Amount: " + purchaseAmount);

        assertThat(purchaseAmount)
                .as("Check purchaseAmount equals sum of price * copies in courses")
                .isEqualTo(total);
    }

    @Test
    public void printData() {
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        String courseFirstTitle = jsonPath.getString("courses[0].title");
        System.out.println("Purchase amount " + purchaseAmount);
        System.out.println("First course title " + courseFirstTitle);

    }

    @Test
    public void printAllCourseTitlesAndPrices3() {
        int count = jsonPath.getInt("courses.size()");
        for (int i = 0; i < count; i++) {
            String courseTitles = jsonPath.get("courses[" + i + "].title");
            int coursePrices = jsonPath.get("courses[" + i + "].price");
            System.out.println("Course title: " + courseTitles + ", price: " + coursePrices);
        }
    }

    @Test
    public void printCopiesSoldByRPACourse3() {
        int count = jsonPath.getInt("courses.size()");
        for (int i = 0; i < count; i++) {
            String courseTitles = jsonPath.get("courses[" + i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")) {
                int copies = jsonPath.get("courses[" + i + "].copies");
                System.out.println("Number of RPA copies: " + copies);
                break;
            }
        }
    }

}
