package com.store.project.application.service;

import com.store.project.application.Handling.exception.ReView.ReViewNotFoundException;
import com.store.project.application.Handling.exception.WriterIsNotException;
import com.store.project.application.Handling.exception.product.ProductNotFoundException;
import com.store.project.application.controller.ReViewRestController;
import com.store.project.application.domain.dao.*;
import com.store.project.application.domain.dto.ReViewCommentDto;
import com.store.project.application.domain.dto.ReViewDto;
import com.store.project.application.domain.dto.RequestProduct;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.ReView;
import com.store.project.application.domain.entity.ReViewComment;
import com.store.project.application.request.RequestReComment;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import com.store.project.application.util.FileUploadBinary;
import com.store.project.application.util.HateOasLoader;
import com.store.project.application.util.ImagesResourcePath;
import com.store.project.application.util.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ReViewServiceImpl implements ReViewService{
    @Autowired
    Environment env;
    StoreRepository storeRepository;
    ClientRepository clientRepository;
    FileUploadBinary fileUpload;
    ProductRepository productRepository;
    StockRepository stockRepository;
    EntityManager entityManager;
    ReViewRepository reViewRepository;
    ReViewCommentRepository reViewCommentRepository;
    SaleRepository saleRepository;
    ImagesResourcePath imagesResourcePath;
    @Autowired
    public ReViewServiceImpl(ReViewRepository reViewRepository, StoreRepository storeRepository,
                             ClientRepository clientRepository, FileUploadBinary fileUpload,
                             ProductRepository productRepository, StockRepository stockRepository,
                             EntityManager entityManager, ReViewCommentRepository reViewCommentRepository,
                             SaleRepository saleRepository, ImagesResourcePath imagesResourcePath) {
        this.storeRepository = storeRepository;
        this.clientRepository = clientRepository;
        this.fileUpload = fileUpload;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.entityManager = entityManager;
        this.reViewRepository=  reViewRepository;
        this.reViewCommentRepository = reViewCommentRepository;
        this.saleRepository = saleRepository;
        this.imagesResourcePath = imagesResourcePath;
    }

    @Override
    public ResponseData insertReView(ReViewDto reViewDto) {
        //Product is Not Found Check
        Product product = this.FoundProductCheck(Math.toIntExact(reViewDto.getIdx()));
        Boolean aBoolean = saleRepository.existsByClient_UserIdAndProductIdx(getUserId(), (long) reViewDto.getIdx());
        //??????????????? ????????????
        if(!aBoolean){
            throw new RuntimeException("??????????????? ??????????????? ???????????? ???????????? ????????? ??? ????????????.");
        }
        //????????? ??????????????? + ?????????
        String saveName = null;
        if(reViewDto.getFileBase64()==null || reViewDto.getFileName()==null){
        }else{
            HashMap<String,String> hmap = new HashMap<>();
            hmap.put("fileName",reViewDto.getFileName());
            hmap.put("fileBase64",reViewDto.getFileBase64());
            saveName = fileUpload.fileUpload(hmap);
        }
        //ReView Entity ?????? ??????
        reViewDto.setIdx(0);
        ReView reViewData = new ModelMapper().map(reViewDto,ReView.class);
        reViewData.setOriginalFileName(reViewData.getFileName());
        reViewData.setFileName(imagesResourcePath.getImagesResourcesPath(saveName));
        reViewData.setProduct(product);
        reViewData.setStore(product.getStore());
        reViewData.setWriter(getUserId());
        //Repository save
        ReView savedReView = reViewRepository.save(reViewData);
        // Response Mapper Transfer
        ModelMapper mapper = new ModelMapper();
        ReViewDto  dto = mapper.map(savedReView,ReViewDto.class);
        // Response

        ResponseData responseData = ResponseData.builder().code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                                    .message("???????????? ??????????????? ?????? ???????????????.")
                                    .item(dto).build();
        return responseData;
    }

    @Override
    public ResponseData selectReViewList(int size, int page, PagedResourcesAssembler<ReView> assembler) {
        String flag = "reviewList";
        HashMap<String,Object> responseHmap = new HashMap<>();
        //?????????
        Pageable pageable = PageRequest.of(page,size,Sort.by("idx").descending());
        // DESC LIST
        Page<ReView> pageReView = reViewRepository.findAll(pageable);
        PagedModel<EntityModel<ReView>> entityModels = assembler.toModel(pageReView);
        //HateOasLoader
        HateOasLoader loader = new HateOasLoader(size,page,flag,env);
        loader.setListHateOas(responseHmap,entityModels);
        // Page Setting (HashMap)
        responseHmap.put("totalPage",pageReView.getTotalPages());
        responseHmap.put("totalProduct",pageReView.getTotalElements());
        // setting + dto mapper
        List<ReViewDto> dto =new ArrayList<>();
        //HateOas self Link
        for(int i=0; i<pageReView.getContent().size(); i++){
            WebMvcLinkBuilder linkTo = linkTo(methodOn(ReViewRestController.class).selectReView(i+1));
            dto.add(new ModelMapper().map(pageReView.getContent().get(i),ReViewDto.class));
            dto.get(i).setReViewLink(linkTo.withSelfRel());
            dto.get(i).setTotalComment(pageReView.getContent().get(i).getReViewComment().size());
        }
//        for (ReView re:pageReView.getContent()) {
//            dto.add(new ModelMapper().map(re,ReViewDto.class));
//        }
        responseHmap.put("content",dto);
        //ResponseData
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(responseHmap)
                .build();
    }

    @Override
    public ResponseData searchReViewList(String title, int size, int page, PagedResourcesAssembler<ReView> assembler) {
        //????????? ????????? ???????????? ??????
        Boolean flag = title.trim().length() ==0 ? false : true;
        String HFlag= "reviewSearch";
        HashMap<String,Object> responseHmap = new HashMap<>();
        //Pageable
        Pageable  pageable = PageRequest.of(page,size,Sort.by("title").descending());
        //search (DESC LIST)
        Page<ReView> pageReView;
        if(flag){
            pageReView = reViewRepository.findAllByTitleContains(title,pageable);
        }else{
            pageReView = reViewRepository.findAll(pageable);
        }
        PagedModel<EntityModel<ReView>> entityModels = assembler.toModel(pageReView);
        //HateOasLoader
        HateOasLoader loader = new HateOasLoader(size,page,HFlag,title,env);
        loader.setSearchingHateOas(responseHmap,entityModels);
        // Page Setting (HashMap)
        responseHmap.put("totalPage",pageReView.getTotalPages());
        responseHmap.put("totalProduct",pageReView.getTotalElements());
        // setting + dto mapper
        List<ReViewDto> dto =new ArrayList<>();
        for (ReView re:pageReView.getContent()) {
            dto.add(new ModelMapper().map(re,ReViewDto.class));
        }
        responseHmap.put("content",dto);
        //ResponseData
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(responseHmap)
                .build();
    }

    @Override
    public ResponseData changeReView(ReViewDto reViewDto) {
        //?????????
        Optional<ReView> opReView = reViewRepository.findById((long) reViewDto.getIdx());
        if(!opReView.isPresent()){
            throw new ProductNotFoundException("?????? ???????????? ?????????????????? ???????????? ?????? ????????????.");
        }
        ReView reView  = opReView.get();
        //Writer Check
        this.ValidWriterCheck(reView.getWriter());
        //setter -> save
        this.reviewChangeData(reView,reViewDto);
        reView = reViewRepository.save(reView);
        //mapping Dto
        reViewDto = new ModelMapper().map(reView,ReViewDto.class);
        return ResponseData.builder()
                .message("?????? ????????? ?????? ???????????????.")
                .status(HttpStatus.OK).code(ResponseDataStatus.SUCCESS)
                .item(reViewDto)
                .build();
    }

    @Override
    public ResponseData deleteReview(int idx) {
        Optional<ReView> opReView = reViewRepository.findById((long) idx);
        if(!opReView.isPresent()){
            throw new ProductNotFoundException("?????? ???????????? ?????????????????? ???????????? ?????? ????????????.");
        }
        ReView reView  = opReView.get();
        this.ValidWriterCheck(reView.getWriter());
        reViewRepository.delete(reView);
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .message("?????? ???????????? ??????????????? ?????????????????????.")
                .build();
    }

    //???????????? ??????
    @Override
    public ResponseData insertReViewComment(RequestReComment comment) {
        //?????? ????????? ???????????? ??????.
        ReView reView = this.FoundReViewCheck(comment.getR_idx());
        // Request class =(mapping)> Entity
        ReViewComment reViewComment = new ModelMapper().map(comment,ReViewComment.class);
        reViewComment.setReView(reView);
        reViewComment.setWriter(getUserId());
        ReViewComment savedReComm = reViewCommentRepository.save(reViewComment);

        Optional<ReView> OpView = reViewRepository.findById((long) comment.getR_idx());
        ReViewDto reViewDto = new ModelMapper().map(OpView.get(),ReViewDto.class);
        reViewDto.setReViewComment_idx(OpView.get());

        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .message("????????? ??????????????? ?????????????????????.")
                .item(reViewDto)
                .build();
    }

    //???????????? ??????
    @Override
    public ResponseData selectReView(int idx) {
        ReView reView = this.FoundReViewCheck(idx);
        ReViewDto reViewDto = new ModelMapper().map(reView,ReViewDto.class);
        reViewDto.setReViewComment_idx(reView);
        reViewDto.setTotalComment(reView.getReViewComment().size());
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(reViewDto)
                .build();
    }

    //??????????????????
    @Override
    public ResponseData deleteReViewComm(RequestReComment comment) {
        ReViewComment reViewComment = this.ValidReViewComm(comment);
        reViewCommentRepository.delete(reViewComment);
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .message("????????? ??????????????? ?????????????????????.")
                .build();
    }
    //??????????????????
    @Override
    public ResponseData updateReViewComm(RequestReComment comment) {
        ReViewComment reViewComment = this.ValidReViewComm(comment);
        if(comment!=null){
        reViewComment.setComment(comment.getComment());
        }
        reViewComment.setDate(new Date());
        ReViewComment save = reViewCommentRepository.save(reViewComment);
        ReViewCommentDto reViewCommentDto = new ModelMapper().map(save,ReViewCommentDto.class);
        reViewCommentDto.SetReView_idx(save);
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(reViewCommentDto)
                .message("????????? ??????????????? ?????????????????????.")
                .build();
    }

    private ReViewComment ValidReViewComm(RequestReComment comment){
        Optional<ReViewComment> byId = reViewCommentRepository.findById((long) comment.getR_idx());
        if(!byId.isPresent()){ throw new RuntimeException("????????????????????? ????????? ?????? ?????????"); }
        ReViewComment reViewComment = byId.get();
        if(!reViewComment.getWriter().equals(getUserId())){
            throw new RuntimeException("?????? ???????????? ???????????? ????????? ????????? ??? ????????????.");
        }
        return reViewComment;
    }

    private Product FoundProductCheck(int idx){
        Optional<Product> byId = productRepository.findById((long) idx);
        if(!byId.isPresent()){
            throw new ProductNotFoundException("????????? ????????? ????????????????????? ?????????????????????.");
        }
        return byId.get();
    }

    private ReView FoundReViewCheck(int idx){
        Optional<ReView> byId = reViewRepository.findById((long) idx);
        if(!byId.isPresent()){
            throw new ReViewNotFoundException("????????? ???????????? ?????? ?????? ????????????.");
        }
        return byId.get();
    }

    private String getUserId() {
        //SecurityContext ????????? ?????? ??????
        Optional<String> currentUserName = SecurityUtil.getCurrentUserName();
        return currentUserName.get();
    }
    private void ValidWriterCheck(String writer){
        String userId = getUserId();
        if(!writer.equals(userId)){
            throw new WriterIsNotException("????????? ???????????? ???????????? ??????,???????????? ??? ????????????.");
        }
    }

    private void reviewChangeData(ReView reView , ReViewDto reViewDto){
        if(reViewDto.getTitle()!=null){ reView.setTitle(reViewDto.getTitle()); }
        if(reViewDto.getContent()!=null){ reView.setContent(reViewDto.getContent()); }
        //??????
        if(reViewDto.getFileBase64()!=null){
            reView.setOriginalFileName(reViewDto.getFileName());
            HashMap<String,String> hmap = new HashMap<>();
            hmap.put("fileName",reViewDto.getFileName());
            hmap.put("fileBase64",reViewDto.getFileBase64());
            String saveName = fileUpload.fileUpload(hmap);
            reView.setFileName(imagesResourcePath.getImagesResourcesPath(saveName));
        }

    }
}
