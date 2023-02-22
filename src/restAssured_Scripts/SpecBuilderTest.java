package restAssured_Scripts;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoSerialization.AddPlace;
import pojoSerialization.Location;

public class SpecBuilderTest {
	
	public static void main(String args[])
	{
		
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		AddPlace add = new AddPlace();
		Location loc = new Location();
		loc.setLat(-38.383491);
		loc.setLng(33.427362);
		add.setLocation(loc);
		add.setAccuracy(50);
		add.setName("Frontline house");
		add.setPhone_number("(+91) 983 893 3937");
		add.setAddress("29, side layout, cohen 09");
		
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		add.setTypes(myList);
		
		add.setWebsite("http://google.com");
		add.setLanguage("French-IN");
		
		//Request Specification - set keyword
		RequestSpecification request = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		
		//Response Specification - expect keyword
		ResponseSpecification response = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

		
		RequestSpecification req = given().spec(request).body(add);
		Response res = req.when().post("/maps/api/place/add/json")
		.then().spec(response).extract().response();
		
		System.out.println("Response: "+res.asString());
		
		
	}

}
