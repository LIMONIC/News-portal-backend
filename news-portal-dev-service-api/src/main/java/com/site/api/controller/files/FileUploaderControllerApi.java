package com.site.api.controller.files;

import com.site.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "Controller for file upload", tags = {"Controller for file upload"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {
    @ApiOperation(value = "API for uploading user profile", notes = "API for uploading user profile", httpMethod = "POST")
    @PostMapping("/uploadFace") // route of the method
    public GraceJSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;
}
