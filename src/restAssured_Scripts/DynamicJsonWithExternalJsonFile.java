package restAssured_Scripts;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import resources.ReusableMethod;

public class DynamicJsonWithExternalJsonFile {
	
	//Data through external JSON file
	
	@Test(dataProvider="BooksData")
	public void dynamicAddJson(String isbn, String aisle) throws IOException {
		
		RestAssured.baseURI="http://216.10.245.166";
		
		String response = given().log().all().header("Content-Type","application/json")
		//Below code		
		.body(ReusableMethod.GenerateStringFromResource("C:\\Users\\ankit\\Documents\\Addbookdetails.json"))
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = ReusableMethod.rawToJson(response);
		String id = js.getString("ID");
		System.out.println("Value of the ID: "+id);
		
	}
	
	@Test(dataProvider="BooksData")
	public void dynamicDeleteJson(String isbn, String aisle) {
		
		RestAssured.baseURI="http://216.10.245.166";
		String ID = isbn + aisle;
		System.out.println("Delete ID value: "+ID);
		given().log().all().header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"ID\" : \""+ID+"\"\r\n"
				+ "} ")
		.when().delete("Library/DeleteBook.php")
		.then().assertThat().statusCode(200);
		
	}
	
	@DataProvider(name="BooksData")
	public Object[][] testData() {
		return new Object[][] {	{"klno","857436"},{"juehf","654654"},{"gwgw","6418"}	};
	}

}
