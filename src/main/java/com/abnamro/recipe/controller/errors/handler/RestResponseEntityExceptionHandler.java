package com.abnamro.recipe.controller.errors.handler;

import com.abnamro.recipe.controller.errors.BadRequestAlertException;
import com.abnamro.recipe.service.dto.ErrorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice("com.abnamro.recipe.controller")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    ObjectMapper om = new ObjectMapper();
    @ExceptionHandler(value = { BadRequestAlertException.class })
    protected ResponseEntity<String> bad_request_exception(BadRequestAlertException ex, WebRequest request)
    {
        String bodyOfResponse = "";
        try {
            bodyOfResponse = om.writeValueAsString(ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.toString()).message(ex.getMessage()).build());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(bodyOfResponse, httpHeaders, HttpStatus.BAD_REQUEST);
    }

}
