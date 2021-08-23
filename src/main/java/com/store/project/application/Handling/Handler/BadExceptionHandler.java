package com.store.project.application.Handling.Handler;

import com.store.project.application.Handling.exception.ReView.ReViewNotFoundException;
import com.store.project.application.Handling.exception.Upload.FileNotExistException;
import com.store.project.application.Handling.exception.Upload.FileNotUploadException;
import com.store.project.application.Handling.exception.WriterIsNotException;
import com.store.project.application.Handling.exception.client.AnonyMousNotException;
import com.store.project.application.Handling.exception.client.OwnerIsNotException;
import com.store.project.application.Handling.exception.client.UserDuplicateException;
import com.store.project.application.Handling.exception.client.UserNotFoundException;
import com.store.project.application.Handling.exception.product.ProductNotFoundException;
import com.store.project.application.Handling.exception.store.StoreDuplicationException;
import com.store.project.application.Handling.exception.store.StoreOverFlowPageException;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import com.store.project.application.response.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class BadExceptionHandler {

    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<ResponseData> UserDuplicationException(UserDuplicateException exception){
        return ResponseEntity.status(UserDuplicateException.HTTP_STATUS).body(ResponseData.builder()
                .message(exception.getMessage())
                .status(UserDuplicateException.HTTP_STATUS)
                .code(UserDuplicateException.code)
                .build()

        );
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseData> RuntimeException(RuntimeException exception){
        return ResponseEntity.status(UserDuplicateException.HTTP_STATUS).body(ResponseData.builder()
                .message(exception.getMessage())
                .status(UserDuplicateException.HTTP_STATUS)
                .code(UserDuplicateException.code)
                .build()
        );
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData> AccessDeniedException(AccessDeniedException exception){
        ResponseData responseData = ResponseData.builder()
                .status(HttpStatus.FORBIDDEN).code(ResponseDataStatus.FORBIDDEN)
                .message("Token Forbidden error (403)")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
    }
    @ExceptionHandler(AnonyMousNotException.class)
    public ResponseEntity<ResponseData> AnonyMousNotException(AnonyMousNotException exception){
        HashMap<String,Object> hmap = new HashMap<>();
        hmap.put("url","/");
        return ResponseEntity.status(UserDuplicateException.HTTP_STATUS).body(ResponseData.builder()
                .message(exception.getMessage())
                .status(AnonyMousNotException.HTTP_STATUS)
                .code(AnonyMousNotException.code)
                .item(hmap)
                .build()

        );
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseException> UserNotFoundException(UserNotFoundException exception){
        return ResponseEntity.status(UserNotFoundException.HTTP_STATUS).body(ResponseException.builder()
                .code(UserNotFoundException.code)
                .status(UserNotFoundException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build());
    }
    @ExceptionHandler(StoreDuplicationException.class)
    public ResponseEntity<ResponseException> StoreDuplicationException(StoreDuplicationException exception){
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(StoreDuplicationException.code)
                .status(StoreDuplicationException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .item(exception.store)
                .build());
    }
    @ExceptionHandler(StoreOverFlowPageException.class)
    public ResponseEntity<ResponseException> StoreOverFlowPageException(StoreOverFlowPageException exception){
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(StoreDuplicationException.code)
                .status(StoreDuplicationException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build());
    }
    @ExceptionHandler(FileNotUploadException.class)
    public ResponseEntity<ResponseException> FileNotUploadException(FileNotUploadException exception){
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(FileNotUploadException.code)
                .status(FileNotUploadException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build());
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ResponseException> ProductNotFoundException(ProductNotFoundException exception){
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(ProductNotFoundException.code)
                .status(ProductNotFoundException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build());
    }
    @ExceptionHandler(FileNotExistException.class)
    public ResponseEntity<ResponseException> FileNotExistException(FileNotExistException exception){
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(FileNotExistException.code)
                .status(FileNotExistException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(OwnerIsNotException.class)
    public ResponseEntity<ResponseException> OwnerIsNotException(OwnerIsNotException exception){
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(OwnerIsNotException.code)
                .status(OwnerIsNotException.HTTP_STATUS)
                .exception(exception.getClass().getName())
                .message(exception.getMessage())
                .build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        log.error(errorMessage);
//        printExceptionMessage(errorMessage);
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(ResponseDataStatus.BAD_REQUEST)
                .status(HttpStatus.BAD_REQUEST)
                .exception(e.getClass().getName())
                .message(errorMessage)
                .build());
    }
    @ExceptionHandler(ReViewNotFoundException.class)
    public Object ReViewNotFoundException(ReViewNotFoundException e) {
        String errorMessage = e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(ResponseDataStatus.BAD_REQUEST)
                .status(HttpStatus.BAD_REQUEST)
                .exception(e.getClass().getName())
                .message(errorMessage)
                .build());
    }
    @ExceptionHandler(WriterIsNotException.class)
    public Object WriterIsNotException(WriterIsNotException e) {
        String errorMessage = e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(StoreDuplicationException.HTTP_STATUS).body(ResponseException.builder()
                .code(ResponseDataStatus.BAD_REQUEST)
                .status(HttpStatus.BAD_REQUEST)
                .exception(e.getClass().getName())
                .message(errorMessage)
                .build());
    }
}
