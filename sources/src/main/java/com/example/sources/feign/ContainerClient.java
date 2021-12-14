package com.example.sources.feign;

import com.example.sources.domain.dto.feign.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URI;
import java.util.List;

@FeignClient(name = "student-container", url="url-placeholder")
public interface ContainerClient {

    @GetMapping(value = "/mouse_keyboard/keyboard")
    FeignStatusResData detectKeyboardHit(URI uri);

    @GetMapping(value = "/mouse_keyboard/mouse")
    FeignStatusResData detectMouseMove(URI uri);

    @PostMapping
    FeignBashResponseData executeRemoteCommand(URI uri, @RequestBody FeignCommandReqData requestBody);

    @GetMapping(value = "/filesystem/find_install")
    FeignInstallResData detectInstallation(URI uri, @RequestParam String programName);

    @GetMapping(value = "/filesystem/file_view")
    FeignFileResData getFileContent(URI uri, @RequestParam String filePath);

    @GetMapping(value = "/bash_history/non_realtime")
    FeignHistoryResData getBashHistory(URI uri, @RequestParam List<String> excludeKeyWords);
}
