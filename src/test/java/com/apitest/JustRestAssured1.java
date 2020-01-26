package com.apitest;

import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.with;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JustRestAssured1 {

    @Test
    public void getAllCountries() throws ParseException {
        String uri = "https://restcountries-v1.p.rapidapi.com";
        String path = "/all";

        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Host", "restcountries-v1.p.rapidapi.com");
        headers.put("X-RapidAPI-Key", "8bcb167f50msh8c2f0f0c3daa81ep162e14jsn12daa5cf7d03");

        Response response = with().given().baseUri(uri)
                .and().basePath(path)
                .and().headers(headers).get()
                .then().extract().response();

        String body = response.getBody().asString();

        // First way - - - to get the number of countries
        JsonPath.config = new JsonPathConfig();
        JsonPath jp = new JsonPath(body);
        List<String> allCountries = jp.get("name");
        System.out.println("Number of countries :  " + allCountries.size());

        // Second way - - - to get the number of countries
        List<String> list = jp.get("$");
        System.out.println("USING JSONPATH :   " + list.size());

        // Third way - - - using  googlecode json-simple
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        jsonArray = (JSONArray) jsonParser.parse(body);
        if (jsonArray != null) {
            System.out.println("Json array using Simple json Library : " + jsonArray.size());
        }
        JSONObject jsonObject = null;
        if (jsonArray != null) {
            jsonObject = (JSONObject) jsonParser.parse(jsonArray.get(0).toString());
        }

        System.out.println("Content of the first object in response body returned in JSON ARRAY :  " + jsonArray.get(0));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("name"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("topLevelDomain"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("alpha2Code"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("alpha3Code"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("callingCodes"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("capital"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("altSpellings"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("region"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("subregion"));
        System.out.println("json object for index 0  >>>>>>>>>>. " + jsonObject.get("borders"));
    }

    @Test
    public void usingPropertiesFile() throws IOException {
        // loading properties and initialize BufferedReader using FileReader
        Properties properties = new Properties();
        BufferedReader reader = new BufferedReader(new FileReader("./test.properties"));
        properties.load(reader);
        reader.close();

        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Host", properties.getProperty("host"));
        headers.put("X-RapidAPI-Key", properties.getProperty("key"));
        String uri = properties.getProperty("uri");
        String path = properties.getProperty("path");

        Serenity.setSessionVariable("myurl").to(uri);
        Serenity.setSessionVariable("myPath").to(path);

        Response response = with().when().baseUri(uri).basePath(path).and().headers(headers).get().then().extract().response();

        assertThat("Wrong status code returned, Please investigate",
                response.getStatusCode(), equalTo(200));

        assertThat("Response body is Empty, Please investigate",
                response.getBody(), notNullValue());


    }

    @Test
    public void usingSerenitySessionVariable() throws IOException {

        Properties properties = new Properties();
        BufferedReader reader = new BufferedReader(new FileReader("./test.properties"));
        properties.load(reader);
        reader.close();

        String uri = properties.getProperty("uri");
        String path = properties.getProperty("path");

        Serenity.setSessionVariable("myUrl").to(uri);
        Serenity.setSessionVariable("myPath").to(path);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Host", properties.getProperty("host"));
        headers.put("X-RapidAPI-Key", properties.getProperty("key"));

        String serenityUri=Serenity.sessionVariableCalled("myUrl");
        String serenityPath=Serenity.sessionVariableCalled("myPath");

        Response response=with().when().baseUri(serenityUri).basePath(serenityPath).headers(headers).get().then().extract().response();

        response.getBody().prettyPrint();

    }

}
