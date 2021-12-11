package com.example.sources.feign;

import com.example.sources.domain.dto.feign.*;
import com.example.sources.domain.dto.response.ContainerFileResData;
import com.example.sources.domain.dto.response.ContainerInstallResData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URI;

@FeignClient(name = "student-container", url="url-placeholder")
public interface ContainerClient {

    @GetMapping(value = "/mouse_keyboard/keyboard")
    StatusResData detectKeyboardHit(URI uri);

    @GetMapping(value = "/mouse_keyboard/mouse")
    StatusResData detectMouseMove(URI uri);

    @PostMapping("/command/execute")
    BashResponseData executeRemoteCommand(URI uri, @RequestBody CommandReqData requestBody);

    @GetMapping(value = "/filesystem/find_install")
    InstallResData detectInstallation(URI uri, @RequestParam String programName);

    @GetMapping(value = "filesystem/file_view")
    FileResData getFileContent(URI uri, @RequestParam String filePath);
}
