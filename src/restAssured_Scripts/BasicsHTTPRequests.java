package restAssured_Scripts;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import resources.Payload;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class BasicsHTTPRequests {
	
	public static void main(String[] args) {
		
		//Given - used to declare all the Pre-requisite like query params, authorization, header, body
		//When - used to pass the HTTP request type (PUT,POST,PUT,DELETE) along with resource as a parameter
		//Then - used to validate the response and extract and key value for assertion if necessary
		
		/*
		 * Important pointers:
		 * 
		 * RestAssured.baseURI - To pass URI
		 * Given() method start from query parameters
		 * when() method is followed by HTTP method
		 * then() method is followed by assertThat() hamcrest method for assertion
		 * extract().response().asString() to extract complete response of the request
		 * body("scope", equalTo("APP")) - equalTo() method is used for the response comparision
		 * JsonPath class is used to extract complete response and parse json parameters to get the exact values 
		 */
		
		//POST Request and storing key value from the response code 
		
		RestAssured.baseURI="http://rahulshettyacademy.com/";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.body())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println("Response is: "+response);
		
		JsonPath js = new JsonPath(response);
		String place_id = js.get("place_id");
		System.out.println("Place ID value is: "+place_id);
		
		
		//PUT Request - Update Address value
		
		String newAddress = "70 Summer walk, Greenwich";
		given().log().all().queryParam("place_id", place_id).queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+ place_id +"\",\r\n"
				+ "\"address\":\""+ newAddress +"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//GET Request - To get the updated value of Address
		
		String get_Response = given().log().all().queryParam("place_id", place_id).queryParam("key", "qaclick123")
		.when().get("maps/api/place/get/json")
		.then().log().all().statusCode(200).extract().response().body().asString();
		
		JsonPath js1 = new JsonPath(get_Response);
		String actualAddress = js1.get("address");
		System.out.println("Value of actual address from the GET request is: "+actualAddress);
		
		Assert.assertEquals(actualAddress, newAddress);
		
		
	}

}
