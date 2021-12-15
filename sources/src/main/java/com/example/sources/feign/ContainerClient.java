package com.example.sources.feign;

import com.example.sources.domain.dto.feign.*;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@FeignClient(name = "student-container", url="url-placeholder")
public interface ContainerClient {

    @GetMapping(value = "/mouse_keyboard/keyboard")
    FeignStatusResData detectKeyboardHit(URI uri);

    @GetMapping(value = "/mouse_keyboard/mouse")
    FeignStatusResData detectMouseMove(URI uri);

    @PostMapping(value = "/command/execute/")
    FeignBashResponseData executeRemoteCommand(URI uri, @RequestBody FeignCommandReqData requestBody);

    @PostMapping(value = "/command/execute_script/",  consumes = "multipart/form-data")
    FeignBashResponseData executeRemoteScript(URI uri, @RequestPart MultipartFile scriptFile);

    @GetMapping(value = "/filesystem/find_install")
    FeignInstallResData detectInstallation(URI uri, @RequestParam String programName);

    @GetMapping(value = "/filesystem/file_view")
    FeignFileResData getFileContent(URI uri, @RequestParam String filePath);

    @GetMapping(value = "/bash_history/non_realtime")
    FeignHistoryResData getBashHistory(URI uri, @RequestParam List<String> excludeKeyWords);

    @PostMapping(value = "/filesystem/file_insert/", consumes = "multipart/form-data")
    FeignInsertFileResData insertFile(URI uri,
                    @RequestParam String filePath,
                    @RequestParam Integer insertOption,
                    @RequestPart MultipartFile inputFile);
}
