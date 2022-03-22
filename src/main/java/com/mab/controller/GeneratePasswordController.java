package com.mab.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mab.format.RequestBodyFormat;
import com.mab.format.ResponseFormat;
import com.mab.util.GeneratePasswordUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GeneratePasswordController {

    // Creating Object of ObjectMapper define in Jakson Api
    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/")
    public String helloPasswordGenerate() {
        return "Generate Password Service is Running";
    }

    @PostMapping("/generatepassword")
    public ResponseEntity<ResponseFormat> apiGeneratePassword(HttpServletRequest request,
            @RequestBody RequestBodyFormat requestBodyFormat) {
        log.info("Request Client : " + request.getRemoteAddr());

        try {
            // get Oraganisation object as a json string
            String jsonString = objectMapper.writeValueAsString(requestBodyFormat);

            // Displaying JSON String
            log.info("Request Message : " + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Length Password : " + requestBodyFormat.getLengthPassword());
        GeneratePasswordUtil generatePasswordUtil = new GeneratePasswordUtil();
        String randomPassword = generatePasswordUtil.randomPasssword(requestBodyFormat.getLengthPassword());

        /* Informasi Tentang Nama Method */
        String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

        /* Memanggil Class Response yang telah dibuat */
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setTimestamp(new Date());
        responseFormat.setStatus(HttpStatus.OK.value());
        responseFormat.setError("");
        responseFormat.setMessage(randomPassword);
        responseFormat.setPath(request.getRequestURI() + " | " + nameofCurrMethod);

        ResponseEntity<ResponseFormat> responseEntity = null;
        responseEntity = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(responseFormat);
        try {
            // get Oraganisation object as a json string
            String jsonString = objectMapper.writeValueAsString(responseEntity);

            // Displaying JSON String
            log.info("Respose Message : " + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseEntity;
    }

}
