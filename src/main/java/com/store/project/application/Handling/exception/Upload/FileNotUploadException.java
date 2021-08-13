package com.store.project.application.Handling.exception.Upload;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileNotUploadException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public FileNotUploadException(String meg) {
        super(meg);
    }
}
