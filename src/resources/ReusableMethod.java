package resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.restassured.path.json.JsonPath;

public class ReusableMethod {
	
	public static JsonPath rawToJson(String response) {
		
		JsonPath js = new JsonPath(response);
		return js;
	}
	
	public static String GenerateStringFromResource(String path) throws IOException {
		
		return new String(Files.readAllBytes(Path.of(path)));
		
	}

}
