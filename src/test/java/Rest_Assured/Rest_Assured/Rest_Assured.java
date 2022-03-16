package Rest_Assured.Rest_Assured;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import POJO.POJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Rest_Assured {

	
	@Test(enabled=false)
	
	public void API()
	
	{
		Response res= RestAssured.get("http://localhost:3000/ibmteam/7");
		String responsebody= res.asString();
		/*
		 * System.out.println(responsebody); System.out.println(res.getStatusCode());
		 * System.out.println(res.getStatusLine());
		 * System.out.println(res.getHeaders());
		 */
		System.out.println(res.jsonPath().getString("address"));
		
	}
	
	@Test(enabled=false)
	
	public void API_Test2()
	{
		
		RestAssured.baseURI="https://petstore.swagger.io/v2";
	//	RestAssured.given().get("/ibmteam/9").then().statusCode(200).log().all();
	//	RestAssured.given().delete("/ibmteam/9").then().statusCode(200).log().all();
		RestAssured.given().queryParam("username", "z012").queryParam("password", "123").when().get("/user/login").then().statusCode(200).log().body();
		
	}
	//DAY2
	@Test(enabled=false)
	
	public void Jsson_sending_request_by_converting_to_string()
	{
	
		RestAssured.baseURI="http://localhost:3000";
			
	String reqbody="{\"empname\":\"asdff\",\"address\":\"delhi\"}";
	RestAssured.given().contentType(ContentType.JSON)
	.body(reqbody).when().post("/ibmteam").then().statusCode(201).log().all();
	}
	
	@Test(enabled=false)
	public void Jsson_sending_request_from_file() throws IOException
	{
	
		RestAssured.baseURI="http://localhost:3000";
			FileInputStream file= new FileInputStream(".\\JSON\\abc.json");
	String reqbody="{\"empname\":\"asdff\",\"address\":\"delhi\"}";
	RestAssured.given().contentType(ContentType.JSON)
	.body(IOUtils.toString(file,"UTF-8")).when().post("/ibmteam").then().statusCode(201).log().all();
	}
	
	
	
	
	//DAY3
	/*
	 * @Test(enabled=false) public void pojosample() throws JsonProcessingException
	 * { POJO pojoobject=new POJO(); pojoobject.setEmpname("asdff");
	 * pojoobject.setAddress("delhi");
	 * 
	 * 
	 * System.out.println(pojoobject.getEmpname());
	 * System.out.println(pojoobject.getAddress());
	 * 
	 * ObjectMapper obj=new ObjectMapper(); String
	 * body=obj.writerWithDefaultPrettyPrinter().writeValueAsString(pojoobject);
	 * System.out.println(body);
	 * 
	 * 
	 * RestAssured.baseURI="http://localhost:3000";
	 * RestAssured.given().contentType(ContentType.JSON)
	 * .body(body).when().post("/ibmteam").then().statusCode(201).log().all(); }
	 */
	
	@Test
	public void createUser(ITestContext var1,ITestContext var2) throws JsonProcessingException
	{
		System.out.println("CREATEUSER");
		POJO pojoobject=new POJO();
		pojoobject.setUsername("anagha");
		pojoobject.setFirstName("anagha");
		pojoobject.setLastName("cs");
		pojoobject.setEmail("anagha123@gmail.com");
		pojoobject.setPassword("123456789");
		pojoobject.setPhone("9876543210");
		pojoobject.setUserStatus(0);
		
		
		ObjectMapper obj=new ObjectMapper();
		String body=obj.writerWithDefaultPrettyPrinter().writeValueAsString(pojoobject);
		System.out.println(body);
		
		
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		RestAssured.given().contentType(ContentType.JSON)
		.body(body).when().post("/user").then().statusCode(200).log().all();
		
		var1.setAttribute("Username", pojoobject.getUsername());
		var2.setAttribute("password", pojoobject.getPassword());
		System.out.println("Username"+pojoobject.getUsername());
		System.out.println("password"+pojoobject.getPassword());
	}
	
	@Test(dependsOnMethods = "createUser")
	public void Modify(ITestContext var1,ITestContext var2) throws JsonProcessingException
	{
		System.out.println("MODIFYUSER");
		POJO pojoobject=new POJO();
		pojoobject.setFirstName("anaghaxcs");
		ObjectMapper obj=new ObjectMapper();
		String uname=var1.getAttribute("Username").toString();
		String body=obj.writerWithDefaultPrettyPrinter().writeValueAsString(pojoobject);
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		RestAssured.given().queryParam("username", uname).contentType(ContentType.JSON)
		.body(body).when().put("/user/"+pojoobject.getUsername()).then().statusCode(200).log().all();
		
	}
	@Test(dependsOnMethods = "createUser")
	public void Login(ITestContext var1,ITestContext var2) throws JsonProcessingException
	{
		System.out.println("LOGIN");
		String uname=var1.getAttribute("Username").toString();
		String passwd=var2.getAttribute("password").toString();
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		RestAssured.given().queryParam("username", uname,"password",passwd).when().get("/user/login?username="+uname+"&password="+passwd).then().statusCode(200).log().all();
		
	}
	@Test(dependsOnMethods = "Login")
	public void Logout() throws JsonProcessingException
	{
		System.out.println("LOGOUT");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		RestAssured.given().get("/user/logout").then().statusCode(200).log().all();
		
	}
	@Test(dependsOnMethods = "Logout")
	public void Delete(ITestContext var1) throws JsonProcessingException
	{
		System.out.println("DELETE");
		String uname=var1.getAttribute("Username").toString();
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		RestAssured.given().delete("/user/"+uname).then().statusCode(200).log().all();
		
	}
}
