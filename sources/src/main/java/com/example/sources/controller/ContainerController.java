package com.example.sources.controller;

import com.example.sources.domain.dto.feign.FindFileResponseData;
import com.example.sources.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/containers", produces = "application/json")
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping("/find")
    public ResponseEntity<FindFileResponseData> find() {
        return ResponseEntity.ok(containerService.existFile());
    }
}
