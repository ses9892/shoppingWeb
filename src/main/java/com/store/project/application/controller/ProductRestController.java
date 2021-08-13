package com.store.project.application.controller;

import com.store.project.application.Handling.exception.Upload.FileNotUploadException;

import com.store.project.application.domain.dto.ProductDto;
import com.store.project.application.domain.dto.RequestProduct;

import com.store.project.application.domain.entity.Product;
import com.store.project.application.response.ResponseData;
import com.store.project.application.service.ProductService;
import com.store.project.application.service.StoreService;
import com.store.project.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class ProductRestController {

    @Autowired
    Environment env;

    @Autowired
    UserService userService;

    @Autowired
    StoreService storeService;

    @Autowired
    ProductService productService;


    //헤테우스로 생성한것 까지 같이 보는 url 까지 던져줘보자
//    @PostMapping("/api/v1/product")
//    public ResponseEntity<ResponseData> insertProduct(@RequestParam(value = "file",required = false) MultipartFile[] files ,ProductDto productDto){
//        ResponseData responseData = productService.insertProduct(files,productDto);
//
//        return null;
//    }
    @PostMapping("/api/v1/product")
    public ResponseEntity<ResponseData> insertProduct(@Valid @RequestBody RequestProduct product){
        if(product.getFileBase64()==null || product.getFileName()==null){
            throw new FileNotUploadException("이미지 파일이 정상적으로 업로드 되지 않았습니다.");
        }
        ResponseData responseData = productService.insertProduct(product);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping(value = "/api/v1/product")
    public ResponseEntity<ResponseData> selectProduct(@RequestParam("idx") int idx){
        ResponseData responseData = productService.selectProduct(idx);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping(value = "/api/v1/product/list")
    public ResponseEntity<ResponseData> productList(@RequestParam("page") int page,@RequestParam(value = "size",required = false,defaultValue = "1") int size
            , PagedResourcesAssembler<ProductDto> assembler){
        ResponseData responseData = productService.productList(size,page,assembler);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @PutMapping(value = "/api/v1/product")
    public ResponseEntity<ResponseData> productDataChange(@RequestBody RequestProduct product){
        ResponseData responseData = productService.productDataChange(product);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @DeleteMapping(value = "/api/v1/product")
    public ResponseEntity<ResponseData> productDataChange(@RequestParam("idx") int idx){
        ResponseData responseData = productService.productDataDelete(idx);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping(value = "/api/v1/product/search")
    public ResponseEntity<ResponseData> serachingProduct(@RequestParam(value = "name",required = false,defaultValue = "") String title,
                                                         @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                         @RequestParam(value = "page",required = false,defaultValue = "0") int page
            , PagedResourcesAssembler<Product> assembler){
        ResponseData responseData = productService.searchingProduct(title,size,page,assembler);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }


}
