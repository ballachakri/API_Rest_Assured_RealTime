package com.apitest;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.io.BufferedReader;
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

public class JustRestAssured_V_R {

    @Test
    public void testWithVR() throws IOException {

        Properties properties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./test.properties"));
        properties.load(bufferedReader);
        bufferedReader.close();

        final String uri = properties.getProperty("uri");
        final String path = properties.getProperty("path");

        final String host = properties.getProperty("host");
        final String key = properties.getProperty("key");

        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Host", host);
        headers.put("X-RapidAPI-Key", key);

        ValidatableResponse vr = with().when().baseUri(uri).basePath(path).headers(headers).get()
                .then();

        assertThat("Wrong status code returned, Please investigate",
                vr.extract().response().getStatusCode(), equalTo(200));

        assertThat("Response body is empty, Please investigate",
                vr.extract().response().getBody(), notNullValue());

//      Or --- To assert Count of countries

        String str=vr.extract().response().getBody().asString();

        JsonPath jsonPath=new JsonPath(str);
        List<String> allCountries=jsonPath.get("name");

        assertThat("Number of countries returned is incorrect, Please investigate",
                allCountries.size(), equalTo(250));


    }


}
