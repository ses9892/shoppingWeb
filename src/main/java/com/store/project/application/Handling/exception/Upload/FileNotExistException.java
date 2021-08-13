package com.store.project.application.Handling.exception.Upload;

import com.store.project.application.response.ResponseDataStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileNotExistException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    public final static String code = ResponseDataStatus.NOT_FOUND;

    public FileNotExistException(String meg) {
        super(meg);
    }
}
