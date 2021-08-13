package com.store.project.application.controller;

import com.store.project.application.domain.dto.ClientDto;
import com.store.project.application.domain.dto.StoreDto;
import com.store.project.application.domain.entity.Store;
import com.store.project.application.request.RequestClientInfoChange;
import com.store.project.application.request.RequestStoreInfo;
import com.store.project.application.request.RequestStoreInfoChange;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import com.store.project.application.service.StoreService;
import com.store.project.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
public class MainRestController {

    @Autowired
    Environment env;

    @Autowired
    Environment environment;

    @Autowired
    UserService userService;

    @Autowired
    StoreService storeService;

    @PostMapping(value = "/api/v1/register.do")
    public ResponseEntity<ResponseData> register(@RequestBody @Valid ClientDto clientDto){
        //register(clientDto)
       ResponseData responseData =  userService.register(clientDto);
        //로직....
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    @GetMapping(value = "/api/v1/duplication.do")
    public ResponseEntity<ResponseData> duplication(@RequestBody  ClientDto clientDto){
        //register(clientDto)
       ResponseData responseData =  userService.duplication(clientDto);
        //로직....
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    //정보 조회
    @GetMapping(value = "/api/v1/users.do")
    public ResponseEntity<ResponseData> myinfo(){
        ResponseData responseData =userService.myinfo();
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
    //유저 정보 수정
    @PutMapping(value = "/api/v1/users.do")
    public ResponseEntity<ResponseData> infoChange(@RequestBody RequestClientInfoChange requestClientInfoChange){
        ResponseData responseData =userService.infoChange(requestClientInfoChange);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    //유저 정보 삭제(회원탈퇴)
    @DeleteMapping(value = "/api/v1/users.do")
    public ResponseEntity<ResponseData> infoDelete(){
        ResponseData responseData =userService.infoDelete();
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    //상점등록
    @PostMapping(value = "/api/v1/store.do")
    public ResponseEntity<ResponseData> insertStore(@RequestBody RequestStoreInfo storeInfo){
        StoreDto storeDto = new ModelMapper().map(storeInfo,StoreDto.class);
        ResponseData responseData = storeService.insertStore(storeDto);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    //상점정보 조회
    @GetMapping(value = "/api/v1/store.do")
    public ResponseEntity<ResponseData> storeInfo(){
        ResponseData responseData = storeService.storeInfo();
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
    //개별 상점 정보 조회 추가할것


    //상점 정보 변경
    @PutMapping(value = "/api/v1/store.do")
    public ResponseEntity<ResponseData> storeInfoChange(@RequestBody RequestStoreInfoChange requestStoreInfoChange){
        ResponseData responseData = storeService.storeInfoChange(requestStoreInfoChange);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    //전체 상점 조회 (pageable)
    @GetMapping(value = "/api/v1/store/list")
    public ResponseEntity<ResponseData> storeList(@RequestParam("page") int page, PagedResourcesAssembler<Store> assembler){
        int size = Integer.parseInt(env.getProperty("shopping.store.size"));
        PageRequest pageRequest = PageRequest.of(page,size);
        ResponseData responseData = storeService.storeList(pageRequest,assembler,page);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
    //상점 이름 검색
    @GetMapping(value = "/api/v1/store/list/search")
    public ResponseEntity<ResponseData> storeSearch(HttpServletRequest request, PagedResourcesAssembler<Store> assembler){
        int size = Integer.parseInt(env.getProperty("shopping.store.size"));
        int page = Integer.parseInt(request.getParameter("page"));
        String name = request.getParameter("name");
        PageRequest pageRequest = PageRequest.of(page,size);

        ResponseData responseData = storeService.storeSearch(pageRequest,assembler,page,name);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }


}
