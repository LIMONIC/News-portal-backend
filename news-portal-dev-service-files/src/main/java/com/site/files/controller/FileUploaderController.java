package com.site.files.controller;

import com.site.api.controller.files.FileUploaderControllerApi;
import com.site.api.controller.user.HelloControllerApi;
import com.site.files.service.UploaderService;
import com.site.grace.result.GraceJSONResult;
import com.site.grace.result.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploaderController implements FileUploaderControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    @Autowired
    private UploaderService uploaderService;

    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {

        String path = null;
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        if (file != null) {
            // Obtain file name
            String fileName = file.getOriginalFilename();

            // Check file name is empty
            if (StringUtils.isNotBlank(fileName)) {
                // Get file suffix
                String fileNameArr[] = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length - 1];
                // Filter out illegal suffix
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg") ) {
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
                // Perform file upload
                path = uploaderService.uploadFdfs(file, suffix);


            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
            }

        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        logger.info("path = " + path);
        return GraceJSONResult.ok(path);
    }
}
