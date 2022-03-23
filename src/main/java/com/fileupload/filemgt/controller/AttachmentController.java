package com.fileupload.filemgt.controller;

import com.fileupload.filemgt.model.Attachment;
import com.fileupload.filemgt.model.ResponseData;
import com.fileupload.filemgt.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/services")
public class AttachmentController {

    private AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment=null;
        String downloadUrl="";
        attachment=attachmentService.saveAttachment(file);
        downloadUrl= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/services/download/")
                .path(attachment.getId())
                .toUriString();
        return new ResponseData(attachment.getFilename(),
                downloadUrl,
                attachment.getFiletype(),
                file.getSize()
        );
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment=null;
        attachment=attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +
                        attachment.getFilename() + "\"")
                .body(new ByteArrayResource(attachment.getData()));

    }
}
