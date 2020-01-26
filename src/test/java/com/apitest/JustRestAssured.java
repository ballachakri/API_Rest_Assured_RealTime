package com.apitest;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JustRestAssured {
    // <groupId>io.rest-assured</groupId>------>  <artifactId>rest-assured</artifactId>
    @Test
    public void justRestAssuredLib1() {
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
        Response responseGet = get("/Hyderabad");
        String body = responseGet.getBody().asString();
        System.out.println(body);
    }

    @Test
    public void justRestAssuredLib2() {

        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
        Response response = with().given().get("/Hyderabad");
        String body = response.body().asString();
        System.out.println(body);
    }

    @Test
    public void justRestAssuredLib3() {
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
        Response response = with().given().get("/Hyderabad").then().extract().response();
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test
    public void justRestAssuredLib4() {
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
        Response response = with().given().get("/Delhi").then().extract().response();
        String body = response.body().asString();
        int sCode = response.statusCode();
        assertThat("Wrong Status code, please investigate",
                sCode, equalTo(200));
        JsonPath.config = new JsonPathConfig();
        JsonPath jp = new JsonPath(body);
        String city = jp.get("City");
        assertThat("Wrong city returned, please investigate",
                city, equalTo("Delhi"));
    }


    @Test
    public void justRestAssuredLib5() {
     String uri="http://restapi.demoqa.com/utilities/weather";
     String path="/city";
     Response response=with().given().baseUri(uri).and().basePath(path).get("/Hyderabad").then().extract().response();
     System.out.println(response.getBody().asString());
    }

    @Test
    public void justRestAssuredLib6() {

        String uri="http://restapi.demoqa.com/utilities/weather";
        String path="/city";
        Response response=with().given().baseUri(uri).and().basePath(path).get("/Hyderabad").then().extract().response();
        String body=response.getBody().asString();

        int sCode=response.getStatusCode();
        System.out.println("Status Code :  "+sCode);
        String sLine=response.getStatusLine();
        System.out.println("Status Line :  "+sLine);
        String ct=response.getContentType();
        System.out.println("Content Type :  "+ct);
        Headers header= response.getHeaders();
        System.out.println("All Headers content :  "+header);

        JsonPath jp=new JsonPath(body);
        String city=jp.get("City");
        System.out.println("City Node USING JsonPath : " +city);
        Map<String, Object> allNodes=jp.getMap("$");
        System.out.println("Number properties in json object : "+ city +" : "+allNodes.size());
        Set<String> allKeys=allNodes.keySet();
        for(String key: allKeys){
            System.out.println("Keys  : "+ key +  " : Values :  "+allNodes.get(key));
        }

        System.out.println(" Just to compare response >>>>>>>> : ");
        response.getBody().prettyPrint();
    }
}
