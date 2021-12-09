package com.example.sources.feign;

import com.example.sources.domain.dto.feign.StatusResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(name = "student-container", url="url-placeholder")
public interface ContainerClient {

    @GetMapping(value = "/filesystem/find_file")
    StatusResponseData existsFile(URI uri,
                                  @RequestParam Integer search_option,
                                  @RequestParam String base_dir,
                                  @RequestParam String filename);

    @GetMapping(value = "/mouse_keyboard/keyboard")

}
