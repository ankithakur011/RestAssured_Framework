package restAssured_Scripts;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import resources.Payload;
import resources.ReusableMethod;

import static io.restassured.RestAssured.*;

public class DynamicJsonWithDataProvider {
	
	//Through DataProvider TestNG annotation
	
	@Test(dataProvider="BooksData")
	public void dynamicAddJson(String isbn, String aisle) {
		
		RestAssured.baseURI="http://216.10.245.166";
		
		String response = given().log().all().header("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle))
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
