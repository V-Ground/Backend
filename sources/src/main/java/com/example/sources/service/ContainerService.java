package com.example.sources.service;

import com.example.sources.domain.dto.feign.FindFileRequestData;
import com.example.sources.domain.dto.feign.FindFileResponseData;
import com.example.sources.feign.ContainerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class ContainerService {
    private final ContainerClient containerClient;

    public FindFileResponseData existFile() {
        URI uri = URI.create("http://localhost:8081/dusmmy");
        FindFileRequestData body = new FindFileRequestData("dir", "filename");
        return containerClient.existsFile(uri, 1, body);
    }
}
