package restAssured_Scripts;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojoDeserialization.Api;
import pojoDeserialization.GetCourses;
import pojoDeserialization.WebAutomation;

public class OAUTH2_Authentication {

	public static void main(String[] args) throws InterruptedException {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		String[] courseTitles= { "Selenium Webdriver Java","Cypress","Protractor"};
		
		//GET Request - To get Authorisation Code - Google has disable this feature due to security concern
		
		/*
		System.setProperty("webdriver.chrome.driver", "C://seleniumDrivers//chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='identifierId']")).sendKeys("ankit.s.thakur011@gmail.com");
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='identifierId']")).sendKeys("//*[@name='password']");
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		//driver.findElement(By.xpath("//*[@id='identifierId']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		String url = driver.getCurrentUrl();
		*/
		
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWtgzh6QLj9Z0tKqJioHvFRuEzTW0_EAo_V0sTs-7jKCEZ7VL21mz5mpNKGiOEnTwUoLFQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=2&prompt=none";
		String authorization_code = url.substring(url.indexOf("code=") + 5 , url.indexOf("&scope"));
		System.out.println("Authorization code is: "+authorization_code);
		
		//POST Request - To get Access Token
		
		String access_response = given().urlEncodingEnabled(false).relaxedHTTPSValidation().log().all().queryParam("code", authorization_code)
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParam("grant_type", "authorization_code")
		.when().post("https://www.googleapis.com/oauth2/v4/token")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		JsonPath js2 = new JsonPath(access_response);
		String access_token = js2.get("access_token").toString();
		System.out.println("Access token: "+access_token);
		
		
		//GET Request - To submit the actual API request to retrieve courses
		/**** POJO Class Example ****/
		
		GetCourses gc = given().queryParam("access_token", access_token).expect().defaultParser(Parser.JSON)
		.when().get("getCourse.php").as(GetCourses.class);
		
		
		//Retrieving values from the POJO Class
		System.out.println("LinkedIn: "+gc.getLinkedIn());
		System.out.println("Instructor: "+gc.getInstructor());
		
		//Retrieve price for 'SoapUI Webservices testing' course title dynamically  
		for(int i=0;i<gc.getCourses().getApi().size();i++)
		{
			List<Api> courseTitle = gc.getCourses().getApi();
			if (courseTitle.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println("Course Title is: "+courseTitle.get(i).getCourseTitle());
				System.out.println("Price is: "+courseTitle.get(i).getPrice());
			}
		}
		
		// Way 1 - Retrieve all course title from webAutomation course
		List<WebAutomation> Course = gc.getCourses().getWebAutomation();
		for(int i=0;i<Course.size();i++)
		{
			System.out.println(Course.get(i).getCourseTitle());
		}
		
		//Way 2 - Get the course names of WebAutomation with ArrayList assertion check
		ArrayList<String> a= new ArrayList<String>();
		List<WebAutomation> w=gc.getCourses().getWebAutomation();
		
		for(int j=0;j<w.size();j++)
		{
			a.add(w.get(j).getCourseTitle());
		}
		
		List<String> expectedList=	Arrays.asList(courseTitles);
		Assert.assertTrue(a.equals(expectedList));
		
	}

}
