package com.github.catalin.cretu.verspaetung.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiResponse<T extends ErrorsResponse> extends ResponseEntity<T> {

    private ApiResponse(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static <T extends ErrorsResponse> ApiResponse<T> ok(final T body) {
        return new ApiResponse<>(body, defaultHttpHeaders(), HttpStatus.OK);
    }

    public static <T extends ErrorsResponse> ApiResponse<T> badRequest(final T body) {
        return new ApiResponse<>(body, defaultHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    public static <T extends ErrorsResponse> ApiResponse<T> internalError(final T errorResponse) {
        return new ApiResponse<>(errorResponse, defaultHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static HttpHeaders defaultHttpHeaders() {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }
}