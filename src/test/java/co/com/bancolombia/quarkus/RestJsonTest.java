package co.com.bancolombia.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * JVM mode tests.
 */
@QuarkusTest
public class RestJsonTest {

    @Test
    public void fruits() {

        /* Assert the initial fruits are there */
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        "$.size()", is(2),
                        "name", containsInAnyOrder("Apple", "Pineapple"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit"));

        /* Add a new fruit */
        given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/fruits")
                .then()
                .statusCode(200)
                .body(
                        "$.size()", is(3),
                        "name", containsInAnyOrder("Apple", "Pineapple", "Pear"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit", "Winter fruit"));
    }

    @Test
    public void legumes() {
        given()
                .when().get("/legumes")
                .then()
                .statusCode(200)
                .body("$.size()", is(2),
                        "name", containsInAnyOrder("Carrot", "Zucchini"),
                        "description", containsInAnyOrder("Root vegetable, usually orange", "Summer squash"));


        given()
                .body("{\"name\": \"Bean\", \"description\": \"Latin American legume\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/legumes")
                .then()
                .statusCode(200)
                .body(
                        "$.size()", is(3),
                        "name", containsInAnyOrder("Carrot", "Zucchini", "Bean"),
                        "description", containsInAnyOrder("Root vegetable, usually orange", "Summer squash", "Latin American legume"))
                .header("COMO2","Bean");
        given()
                .body("{\"name\": \"Bean1\", \"description\": \"Latin American legume\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/legumes")
                .then()
                .statusCode(200)
                .body(
                        "$.size()", is(4),
                        "name", containsInAnyOrder("Carrot", "Zucchini", "Bean1", "Bean"),
                        "description", containsInAnyOrder("Root vegetable, usually orange", "Summer squash", "Latin American legume", "Latin American legume"))
                .header("COMO","Bean1");
    }

}