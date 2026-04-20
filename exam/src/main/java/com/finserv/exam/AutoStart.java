package com.finserv.exam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Component
public class AutoStart implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Calling API...");

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

       
        Map<String, String> request = new HashMap<>();
        request.put("name", "Nityanand Abasaheb Adsul");
        request.put("regNo", "07");
        request.put("email", "vidya123adsul@gmail.com");

        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<?, ?> responseBody = response.getBody();

        String webhookUrl = (String) responseBody.get("webhook");
        String accessToken = (String) responseBody.get("accessToken");

        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("Access Token: " + accessToken);

        // SQL Queries
        String finalQuery = "SELECT \r\n"
        		+ "    p.AMOUNT AS SALARY,\r\n"
        		+ "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,\r\n"
        		+ "    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,\r\n"
        		+ "    d.DEPARTMENT_NAME\r\n"
        		+ "FROM PAYMENTS p\r\n"
        		+ "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID\r\n"
        		+ "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID\r\n"
        		+ "WHERE DAY(p.PAYMENT_TIME) != 1\r\n"
        		+ "ORDER BY p.AMOUNT DESC\r\n"
        		+ "LIMIT 1;";

        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

       
        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

       
        restTemplate.postForEntity(webhookUrl, entity, String.class);

        System.out.println(" Submitted Successfully!");
    }
}