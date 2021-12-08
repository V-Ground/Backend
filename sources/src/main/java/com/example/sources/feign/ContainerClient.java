package com.example.sources.feign;

import com.example.sources.domain.dto.feign.FindFileRequestData;
import com.example.sources.domain.dto.feign.FindFileResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(name = "student-container", url="url-placeholder")
public interface ContainerClient {

    @PostMapping(value = "/filesystem/find_file")
    FindFileResponseData existsFile(URI uri,
                                    @RequestParam Integer search_option,
                                    @RequestBody FindFileRequestData request);
}
