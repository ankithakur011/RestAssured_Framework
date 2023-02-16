package restAssured_Scripts;

import io.restassured.RestAssured;
import pojoSerialization.Location;
import pojoSerialization.AddPlace;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SerializationTest {
	
	public static void main(String args[])
	{
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
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
		
		given().log().all().queryParam("key", "qaclick123")
		.body(add)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).log().all();
	}

}
