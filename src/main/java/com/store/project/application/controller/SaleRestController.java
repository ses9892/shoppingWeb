package com.store.project.application.controller;

import com.store.project.application.domain.dto.ReViewDto;
import com.store.project.application.domain.entity.ReView;
import com.store.project.application.request.RequestReComment;
import com.store.project.application.request.RequestReFundData;
import com.store.project.application.request.RequestSaleData;
import com.store.project.application.response.ResponseData;
import com.store.project.application.service.ProductService;
import com.store.project.application.service.ReViewService;
import com.store.project.application.service.StoreService;
import com.store.project.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;

@RestController
@Slf4j
public class SaleRestController {
    Environment env;
    UserService userService;
    StoreService storeService;
    ProductService productService;
    ReViewService reViewService;

    @Autowired
    public SaleRestController(Environment env, UserService userService, StoreService storeService, ProductService productService, ReViewService reViewService) {
        this.env = env;
        this.userService = userService;
        this.storeService = storeService;
        this.productService = productService;
        this.reViewService = reViewService;
    }


    @PostMapping(value = "api/v1/product/sale")
    public ResponseEntity<ResponseData> insertSale(@Valid @RequestBody RequestSaleData saleData){
        ResponseData responseData = productService.saleProduct(saleData);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @GetMapping(value = "api/v1/product/sale")
    public ResponseEntity<ResponseData> selectSale(@RequestParam(value = "idx",required = true) int idx){
        ResponseData responseData = productService.selectSale(idx);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @DeleteMapping(value = "api/v1/product/sale")
    public ResponseEntity<ResponseData> refoundProduct(@RequestBody RequestReFundData requestReFundData){
        ResponseData responseData = productService.refoundProduct(requestReFundData);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    private WebMvcLinkBuilder getLinkAddress(){
        return WebMvcLinkBuilder.linkTo(SaleRestController.class);
    }








}
