package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given().auth().basic("test", "test")
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello, you are test, with groups bar, foo"));
    }
}