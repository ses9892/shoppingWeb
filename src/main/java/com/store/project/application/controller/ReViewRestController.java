package com.store.project.application.controller;

import com.store.project.application.domain.dto.ReViewDto;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.ReView;
import com.store.project.application.request.RequestReComment;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class ReViewRestController {
    Environment env;
    UserService userService;
    StoreService storeService;
    ProductService productService;
    ReViewService reViewService;

    @Autowired
    public ReViewRestController(Environment env, UserService userService, StoreService storeService, ProductService productService, ReViewService reViewService) {
        this.env = env;
        this.userService = userService;
        this.storeService = storeService;
        this.productService = productService;
        this.reViewService = reViewService;
    }


    @PostMapping(value = "api/v1/review")
    public ResponseEntity<ResponseData> insertReView(@Valid @RequestBody ReViewDto reViewDto){
        ResponseData responseData = reViewService.insertReView(reViewDto);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping(value = "api/v1/review")
    public ResponseEntity<ResponseData> selectReView(@RequestParam(value = "idx")int idx){
        ResponseData responseData = reViewService.selectReView(idx);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping(value = "api/v1/review/list")
    public ResponseEntity<ResponseData> selectReViewList(@RequestParam(value = "size",required = false,defaultValue = "10")int size,
                                                         @RequestParam(value = "page",required = false,defaultValue = "0") @Min(value = 0,message = "plz page is 0 with min") int page
            , PagedResourcesAssembler<ReView> assembler){

        ResponseData responseData = reViewService.selectReViewList(size,page,assembler);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @GetMapping(value = "api/v1/review/search")
    public ResponseEntity<ResponseData> searchReView(@RequestParam(value = "title",required = false,defaultValue = "") String title,
                                                     @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                     @RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                     PagedResourcesAssembler<ReView> assembler){
        ResponseData responseData = reViewService.searchReViewList(title,size,page,assembler);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @PutMapping(value = "api/v1/review")
    public ResponseEntity<ResponseData> changeReView(@RequestBody ReViewDto reViewDto){
        ResponseData responseData = reViewService.changeReView(reViewDto);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @DeleteMapping(value = "api/v1/review")
    public ResponseEntity<ResponseData> deleteReView(@RequestBody(required = false) HashMap<String,Object> hashMap){
        int idx = Integer.parseInt(""+hashMap.get("idx"));
        ResponseData responseData = reViewService.deleteReview(idx);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PostMapping(value = "api/v1/review-comment")
    public ResponseEntity<ResponseData> insertReViewComm(@Valid @RequestBody RequestReComment comment){
        ResponseData responseData = reViewService.insertReViewComment(comment);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @DeleteMapping(value = "api/v1/review-comment")
    public ResponseEntity<ResponseData> DeleteReViewComm(@RequestBody RequestReComment comment){
        ResponseData responseData = reViewService.deleteReViewComm(comment);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @PutMapping(value = "api/v1/review-comment")
    public ResponseEntity<ResponseData> UpdateReViewComm(@RequestBody RequestReComment comment){
        ResponseData responseData = reViewService.updateReViewComm(comment);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    private WebMvcLinkBuilder getLinkAddress(){
        return WebMvcLinkBuilder.linkTo(ReViewRestController.class);
    }








}
