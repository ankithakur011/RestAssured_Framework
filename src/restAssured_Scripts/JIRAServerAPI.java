package restAssured_Scripts;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import resources.ReusableMethod;

public class JIRAServerAPI {

	public static void main(String[] args) {
		
		
		SessionFilter session = new SessionFilter();
		
		//Step 1 - Cookie Authentication
		RestAssured.baseURI = "http://localhost:8888";
		given().log().all().header("content-type","application/json")
		.body("{ \"username\": \"ankitthakur011\", \"password\": \"At91$London\" }")
		
		//Code for session filter
		.filter(session)
		
		.when().post("/rest/auth/1/session")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		//Step 2 - Create New Issue
		String response2 = given().log().all().header("content-type","application/json").queryParam("updateHistory", "false")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": \r\n"
				+ "        {\r\n"
				+ "            \"key\": \"RES\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \"HDFC Card Issue\",\r\n"
				+ "        \"description\":\"Creating an issue using automation\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session)
		.when().post("/rest/api/2/issue")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js1 = ReusableMethod.rawToJson(response2);
		int issueID = js1.getInt("id");
		System.out.println("Issue ID: "+issueID);
		
		//Step 3 - Add Comment
		given().log().all().header("content-type","application/json").pathParams("issueIdOrKey", issueID)
		.body("{\r\n"
				+ "    \"body\": \"This is my first comment\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session)
		.when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
	}

}
