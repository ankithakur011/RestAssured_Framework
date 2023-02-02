package restAssured_Scripts;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import resources.ReusableMethod;

public class JIRAServerAPI {

	public static void main(String[] args) {
		
		//JIRA API Page - https://docs.atlassian.com/software/jira/docs/api/REST/9.6.0/#api/2
		
		SessionFilter session = new SessionFilter();
		
		//Step 1 - Cookie Authentication with relaxed HTTP Validation
		RestAssured.baseURI = "http://localhost:8888";
		given().log().all().relaxedHTTPSValidation().header("content-type","application/json")
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
				+ "        \"summary\": \"SBI Card Issue\",\r\n"
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
		given().log().all().header("content-type","application/json").pathParams("issueIdOrKey", "10014")
		.body("{\r\n"
				+ "    \"body\": \"This is third automation comment\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session)
		.when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		//Step 4 - Add Attachment
		given().log().all().header("X-Atlassian-Token","no-check").pathParams("issueIdOrKey", issueID).filter(session)
		
		//Code for adding attachment
		.header("content-type","multipart/form-data").multiPart("File",new File("Attachment.txt"))
		
		.when().post("rest/api/2/issue/{issueIdOrKey}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		
		//Step 5 - Get JIRA Issue and extract comment body with Path params, Query Params and session filter
		
		String issueDetails = given().log().all().pathParams("issueIdOrKey", "10014").queryParam("fields", "comment")
		.filter(session)
		.when().get("/rest/api/2/issue/{issueIdOrKey}")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = ReusableMethod.rawToJson(issueDetails);
		int commentSize = js.get("fields.comment.comments.size()");
		System.out.println("Comments size: "+commentSize);
		String extractedComment = null;
		
		for(int i=0;i<commentSize;i++)
		{	
			System.out.println("Value: "+js.get("fields.comment.comments["+i+"].id"));
			String id = js.get("fields.comment.comments["+i+"].id");
			if(id.equalsIgnoreCase("10010"))
			{
				extractedComment = js.get("fields.comment.comments["+i+"].body").toString();
				System.out.println("Comment is: "+extractedComment);
				break;
			}
		}
		
		Assert.assertEquals(extractedComment, "This is third automation comment");
	}
}
