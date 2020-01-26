package com.apitest;

import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import org.junit.Assert;
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
import static org.hamcrest.MatcherAssert.assertThat;

public class JustRestAssured_E_R {

    @Test
    public void testWithEr() throws IOException {

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

        ExtractableResponse er = with().given().baseUri(uri).basePath(path).headers(headers).when().get()
                .then().extract();


        assertThat("Wrong status code, Please investigate",
                er.statusCode(), equalTo(200));


        Headers allHeaders = er.response().getHeaders();

        assertThat("Wrong headers returned, Please investigate",
                allHeaders, notNullValue());

        String str = er.response().getBody().asString();
        JsonPath jsonPath = new JsonPath(str);

        List<String> allCountries = jsonPath.get("name");

        Assert.assertThat("Number of countries returned is incorrect, Please investigate",
                allCountries.size(), equalTo(250));

    }
}
