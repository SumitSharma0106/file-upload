package com.fileupload.filemgt.service;

import com.fileupload.filemgt.model.Attachment;
import com.fileupload.filemgt.repository.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentServiceImpl implements AttachmentService{

    private AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new Exception("Filename contains invalid path sequence" + fileName);
            }
            Attachment attachment=new Attachment(fileName,file.getContentType(),file.getBytes());
            return attachmentRepository.save(attachment);
        }catch (Exception e){
            throw new Exception("Could not save the file : " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id :" + fileId));
    }
}
