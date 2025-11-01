package com.example.sample_back.controller;

import com.example.sampleback.controller.SampleApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class sampleBackController implements SampleApi {

    @Override
    public ResponseEntity<Void> sampleGet() {
        System.out.println("sample");
        return ResponseEntity.ok().build();
    }

}
