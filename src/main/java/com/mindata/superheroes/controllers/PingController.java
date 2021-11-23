package com.mindata.superheroes.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PingController {

    @GetMapping(value = "ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }
}
