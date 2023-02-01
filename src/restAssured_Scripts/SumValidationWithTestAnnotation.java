package restAssured_Scripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import resources.Payload;

public class SumValidationWithTestAnnotation {
	
	//Below code
	
	@Test
	public void sumOfCourses() {
		
		JsonPath js  = new JsonPath(Payload.complexJSONBody());
		int courseSize = js.getInt("courses.size()");
		System.out.println("Course size: "+courseSize);
		int sumOfTheCourse,totalSum = 0;
		
		for(int i=0;i<courseSize;i++)
		{
			sumOfTheCourse = (js.getInt("courses["+i+"].price"))*(js.getInt("courses["+i+"].copies"));
			totalSum = totalSum + sumOfTheCourse;
		}
		
		System.out.println("Total sum is: "+totalSum);
		Assert.assertEquals(totalSum, js.getInt("dashboard.purchaseAmount"));
		
	}

}
