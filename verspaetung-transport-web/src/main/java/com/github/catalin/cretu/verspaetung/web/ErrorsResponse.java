package com.github.catalin.cretu.verspaetung.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsResponse {

    private List<ErrorView> errors;
}