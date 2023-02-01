package restAssured_Scripts;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import resources.Payload;

public class ComplexJSONParser {

	public static void main(String[] args) {
		
		JsonPath js = new JsonPath(Payload.complexJSONBody());
		
		//1. Print no. of courses returned by API
		int noOfCourses = js.getInt("courses.size()");
		System.out.println("Number of courses are: "+noOfCourses);
		
		//2.Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase amount: "+purchaseAmount);
		
		//3. Print Title of the first course
		String titleFirstCourse = js.get("courses[0].title");
		System.out.println("Title First Course: "+titleFirstCourse);
		
		//4. Print All course titles and their respective Prices
		
		for(int i=0;i<js.getInt("courses.size()");i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			System.out.println("Course title: "+courseTitle);
			System.out.println("Course price: "+coursePrice);
		}
		
		//5. Print no of copies sold by RPA Course
		for(int i=0;i<noOfCourses;i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			if(courseTitle.equalsIgnoreCase("RPA"))
			{
				System.out.println("Copies sold by RPA Course: "+js.getInt("courses["+i+"].copies"));
				break;
			}
		}
		
		//6. Verify if Sum of all Course prices matches with Purchase Amount
		int totalPurchaseAmount = js.getInt("dashboard.purchaseAmount");
		int purchaseAmt=0;
		for(int i=0;i<js.getInt("courses.size()");i++)
		{
			purchaseAmt = js.getInt("courses["+i+"].price")*js.getInt("courses["+i+"].copies");
			purchaseAmt = purchaseAmt + purchaseAmt;
		}
		System.out.println("Puchase amount calculated: "+purchaseAmt);
		Assert.assertEquals(purchaseAmt, totalPurchaseAmount);
		
	}

}
