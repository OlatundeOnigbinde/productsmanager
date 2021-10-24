package com.lpg.pmapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductsmanagerApplicationTests {

	@LocalServerPort
	int port;

	@BeforeEach
	void setup(){
		RestAssured.port = port;
	}

	String sessionToken = "MS0xNjM1MDI1MjQzNzgxLW9sYXR1bmRlb25pZ2JpbmRlQGdtYWlsLmNvbS0wODAzMzE3MjU4OQ==";

//	@Test()
//	@Order(1)
	void testRegistration() {
		// Create an object to ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode requestParams = objectMapper.createObjectNode();
		requestParams.put("firstname", "John");
		requestParams.put("lastname", "Snow");
		requestParams.put("phoneNumber", "08012345678");
		requestParams.put("email", "johnsnow@gmail.com");
		requestParams.put("password", "password123");

		Response response = given().
				contentType(ContentType.JSON).
				body(requestParams).
				when().post("/user/register");
		System.out.println(">>>>>>>>>>>>>>>>>>>Response testRegistration>>>>>>>>>>>>>>>>>>>");

		response.then().assertThat().statusCode(200).log().all();
	}

	@Test
	@Order(2)
	void testLogin() {
		// Create an object to ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode requestParams = objectMapper.createObjectNode();
		requestParams.put("email", "johnsnow@gmail.com");
		requestParams.put("password", "password123");

		Response response = given().
				contentType(ContentType.JSON).
				body(requestParams).
				when().post("/user/login");
		System.out.println(">>>>>>>>>>>>>>>>>>>Response testLogin>>>>>>>>>>>>>>>>>>>");

		sessionToken = response.then().assertThat().statusCode(200).log().all().extract().path("sessionToken");
	}

//	@Test
	void testGetProducts() {
		given().
				when().header("sessionToken", sessionToken).
				get("/product").then().assertThat().statusCode(200).log().all();
	}

//	@Test
	void testProductsByCategory() {
		System.out.println(">>>>>>>>>>>>>>>>>>>testProductsByCategory>>>>>>>>>>>>>>>>>>>");
		given().pathParam("categoryId", 1).
				when().header("sessionToken", sessionToken).
				get("/product/getproducts/{categoryId}").then().assertThat().statusCode(200).log().all();
	}

//	@Test
	void testGetCategories() {
		System.out.println(">>>>>>>>>>>>>>>>>>>testGetCategories>>>>>>>>>>>>>>>>>>>");
		given().
				when().header("sessionToken", sessionToken).
				get("/product/categories").then().assertThat().statusCode(200).log().all();
	}
//	@Test
//	@Order(3)
	void testCreateCategory() {
		Response response = given().
				contentType(ContentType.JSON).queryParam("categoryName","Furniture").
				when().header("sessionToken", sessionToken).post("/product/categories");

		System.out.println(">>>>>>>>>>>>>>>>>>>Response testCreateCategory>>>>>>>>>>>>>>>>>>>");

		response.then().assertThat().statusCode(200).log().all();
	}

//	@Test
//	@Order(4)
	void testCreateProduct() {
		// Create an object to ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode requestParams = objectMapper.createObjectNode();
		requestParams.put("productName", "Knife Set");
		requestParams.put("description", "A set of knives in all shapes and sizes.");
		requestParams.put("categoryId", 1);
		requestParams.put("lastPurchasedDateStr", "2021-10-01");

		Response response = given().header("sessionToken", sessionToken).
				contentType(ContentType.JSON).
				body(requestParams).
				when().post("/product");
		response.then().assertThat().statusCode(200).log().all();

		requestParams = objectMapper.createObjectNode();
		requestParams.put("productName", "Plate Rack");
		requestParams.put("description", "A storage solution for plates.");
		requestParams.put("categoryId", 1);
		requestParams.put("lastPurchasedDateStr", "2021-10-01");

		response = given().header("sessionToken", sessionToken).
				contentType(ContentType.JSON).
				body(requestParams).
				when().post("/product");
		response.then().assertThat().statusCode(200).log().all();

		requestParams = objectMapper.createObjectNode();
		requestParams.put("productName", "Microwave");
		requestParams.put("description", "Cook food quick with this handy microwave.");
		requestParams.put("categoryId", 1);
		requestParams.put("lastPurchasedDateStr", "2021-10-01");

		response = given().header("sessionToken", sessionToken).
				contentType(ContentType.JSON).
				body(requestParams).
				when().post("/product");
		System.out.println(">>>>>>>>>>>>>>>>>>>Response testCreateProduct>>>>>>>>>>>>>>>>>>>");

		response.then().assertThat().statusCode(200).log().all();

	}

//	@Test
	void testEditProduct() {
		// Create an object to ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode requestParams = objectMapper.createObjectNode();
		requestParams.put("productName", "Knife Set");
		requestParams.put("description", "A set of knives in all shapes and sizes.");
		requestParams.put("categoryId", 1);
		requestParams.put("lastPurchasedDateStr", "2021-10-01");

		Response response = given().header("sessionToken", sessionToken).
				pathParam("productId", 1).
				contentType(ContentType.JSON).
				body(requestParams).
				when().post("/product/edit/{productId}");
		System.out.println(">>>>>>>>>>>>>>>>>>>Response testEditProduct>>>>>>>>>>>>>>>>>>>");

		response.then().assertThat().statusCode(200).log().all();

	}

//	@Test
	void testDeleteProduct() {
		System.out.println(">>>>>>>>>>>>>>>>>>>testDeleteProduct>>>>>>>>>>>>>>>>>>>");
		given().
				when().header("sessionToken", sessionToken).
				pathParam("productId", 1).
				delete("/product/delete/{productId}").then().assertThat().statusCode(200).log().all();
	}
}
