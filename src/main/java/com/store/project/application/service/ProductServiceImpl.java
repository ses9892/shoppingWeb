package com.store.project.application.service;

import com.store.project.application.Handling.exception.client.OwnerIsNotException;
import com.store.project.application.Handling.exception.product.ProductEaNotEnoughException;
import com.store.project.application.Handling.exception.product.ProductNotFoundException;
import com.store.project.application.Handling.exception.product.ProductNotSaleException;
import com.store.project.application.Handling.exception.sale.SaleNotFoundException;
import com.store.project.application.domain.dao.*;
import com.store.project.application.domain.dto.*;
import com.store.project.application.domain.entity.*;
import com.store.project.application.request.RequestReFundData;
import com.store.project.application.request.RequestSaleData;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import com.store.project.application.util.FileUploadBinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    FileUploadBinary fileUpload;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    RefundRepository refundRepository;


    @Override
    public ResponseData insertProduct(RequestProduct product) {
        //업로드
        HashMap<String,String> hmap = new HashMap<>();
        hmap.put("fileName",product.getFileName());
        hmap.put("fileBase64",product.getFileBase64());
        File file = fileUpload.fileUpload(hmap);

        String path = file.getPath();
        //상품저장
        Product productEntity = new ModelMapper().map(product,Product.class);
        productEntity.setP_originalName(product.getFileName());
        productEntity.setP_saveName(file.toString());
        //상점번호
        Optional<Store> store = storeRepository.findByClient_UserId(getUserId());
        productEntity.setStore(store.get());
        //상점번호 + 상품 저장
        Product saveEntity = productRepository.save(productEntity);
        entityManager.clear();
        //재고
        Stock stock = new Stock();
        if(product.getSt_ea()==0){ stock.setSt_ea(1); }else{stock.setSt_ea(product.getSt_ea()); }
        stock.setSt_flag(true);
        stock.setProduct(saveEntity);
        stockRepository.save(stock);
        //응답하기 위한 상품정보
        entityManager.clear();
        saveEntity = productRepository.findById(saveEntity.getIdx()).get();
        ProductDto dto = new ModelMapper().map(saveEntity,ProductDto.class);
        dto.setStoreIdx(saveEntity);

        //응답
        ResponseData responseData = ResponseData.builder()
                                    .message("상품등록이 성공적으로 완료 되었습니다.")
                                    .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                                    .item(dto)
                                    .build();
        return responseData;
    }

    @Override
    public ResponseData selectProduct(int idx) {
        Optional<Product> productEntity = productRepository.findById((long) idx);

        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("해당 게시글을 찾을 수 없습니다.");
        }
        Product product = productEntity.get();
        File file = new File(product.getP_saveName());
        String product_img = fileUpload.Base64Encoding(file);
        product.setP_originalName(product_img);
        ProductDto dto = new ModelMapper().map(product,ProductDto.class);
        dto.setStoreIdx(product);

        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(dto)
                .build();
    }

    @Override
    public ResponseData productList(int size, int page, PagedResourcesAssembler<ProductDto> assembler) {
        //페이징
        Pageable pageable = PageRequest.of(page,size);
        //전체 DESC 리스트
        Page<Product> productList = productRepository.findAllByOrderByP_idxDesc(pageable);

        //
        List<ProductDto> dtoList = this.listEntityToListDto(productList.getContent());

        Page<ProductDto> test = new PageImpl(dtoList,pageable,dtoList.size());
//        Page<ProductDto> PageDto = new ModelMapper().map(productsPageList,Page<ProductDto>);
//        for (int i =0;i< PageDto.getTotalElements();i++){
//            for (Product p : productsPageList.getContent() ) {
//                PageDto.getContent().get(i).setStoreIdx(p);
//            }
//        }
        PagedModel<EntityModel<ProductDto>> model = assembler.toModel(test);

        ResponseData responseData = ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(model)
                .build();
        return responseData;
    }

    @Override
    public ResponseData productDataChange(RequestProduct product) {
        //상품 조회
        Optional<Product> productEntity = productRepository.findById((long) product.getP_idx());
        //존재 삭제 여부 조회
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("해당 상품은 존재하지 않거나 삭제되었습니다");
        }
        Product proEntity = productEntity.get();

        // setter -> save
        this.productChange(proEntity,product);
        proEntity=productRepository.save(proEntity);
        ProductDto productDto = new ModelMapper().map(proEntity,ProductDto.class);
        productDto.setStoreIdx(proEntity);


        return ResponseData.builder()
                .message("상품 수정이 완료 되었습니다.")
                .status(HttpStatus.OK).code(ResponseDataStatus.SUCCESS)
                .item(productDto)
                .build();
    }

    @Override
    public ResponseData productDataDelete(int idx) {
        //상품조회
        Optional<Product> productEntity = productRepository.findById((long) idx);
        //null -> error
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("해당 상품을 삭제되었거나 존재하지 않습니다");
        }
        Product product= productEntity.get();
        //현재 로그인 id <==> product를 게시한 Id가 맞나 확인
        if(!product.getStore().getClient().getUserId().equals(getUserId())){
            throw new OwnerIsNotException();
        }
        //상품정보삭제
        productRepository.delete(product);
        //삭제후 Response하기위한 현재 상점의 정보
        Optional<Store> StNameStore = storeRepository.findByStName(product.getStore().getStName());
        ResponseData responseData = ResponseData.builder()
                                    .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                                    .message("해당 상품이 성공적으로 삭제 되었습니다.")
                                    .build();
        return responseData;
    }

    @Override
    public ResponseData searchingProduct(String title, int size, int page, PagedResourcesAssembler<Product> assembler) {
        //공백이 왓는지 안왓는지 검사
        Boolean flag = title.trim().length() ==0 ? false : true;
        //Pageable
            Pageable  pageable = PageRequest.of(page,size);
            //search
        Page<Product> product;
        if(flag){
            product = productRepository.findAllByPNameContainsOrderByPNameDesc(title,pageable);
        }else{
            product = productRepository.findAll(pageable);
        }
        //HATEOAS
        PagedModel<EntityModel<Product>> pagedModel = assembler.toModel(product);
        HashMap<String,Object> hmap = new HashMap();
        //이전링크여부
        if(pagedModel.getPreviousLink().isPresent()){
            Link pre = Link.of("/api/v1/product/name="+title+"&page="+(page-1)).withRel("pre");
            hmap.put("pre",pre);
        }
        //다음 링크 여부
        if(pagedModel.getNextLink().isPresent()){
            Link next = Link.of("/api/v1/product/name="+title+"&page="+(page+1)).withRel("next");
            hmap.put("next",next);
        }
        hmap.put("totalPage",product.getTotalPages());
        hmap.put("totalProduct",product.getTotalElements());
        hmap.put("content",product.getContent());
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(hmap)
                .build();
    }


    //상품 판매
    @Override
    public ResponseData saleProduct(RequestSaleData saleData) {
        //상품조회
        Optional<Product> productEntity = productRepository.findById((long) saleData.getP_idx());
        //null -> error
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("해당 상품을 삭제되었거나 존재하지 않습니다");
        }
        Product product = productEntity.get();
        //    Stock => 판매가능 여부 체크
        if(!product.getStock().getSt_flag()) { throw new ProductNotSaleException(); } // fasle 일시
        //     EA check
        if(saleData.getEa()>product.getStock().getSt_ea()){ throw new ProductEaNotEnoughException(); }
        //sale Entity setting + save
        Sale save = this.saveSaleData(product,saleData);
        //상품재고 변경
        Stock stock = product.getStock();
        stock.setSt_ea(stock.getSt_ea()-saleData.getEa());
        if(stock.getSt_ea()==0){ stock.setSt_flag(false);}
        product.setStock(stock);
        Product savedProduct = productRepository.save(product);
        //dto transfer, dto setting
        SaleDto saleDto = new ModelMapper().map(save,SaleDto.class);
        saleDto.setProductAndStoreAndClient(savedProduct);
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .item(saleDto)
                .build();
    }

    @Override
    public ResponseData refoundProduct(RequestReFundData requestReFundData) {
        Optional<Sale> optionalSale = saleRepository.findById((long) requestReFundData.getIdx());
        //Exception Handling
        if(!optionalSale.isPresent()){ throw new SaleNotFoundException(); }
        Sale sale = optionalSale.get();
        if(!sale.getClient().getUserId().equals(getUserId())){ throw new OwnerIsNotException("구매자와 불일치하는 아이디 입니다."); }
        if(sale.getCommit().equals("0")){throw new RuntimeException("이미 환불 완료된 상품입니다."); }
        //재고변경 + 환불/판매 변경
        sale.setCommit("0");
        this.refundWithStockUpdate(sale);
        //환불 entity
        Refund refund = this.setRefund(requestReFundData, sale);
        sale.setRefund(refund);

        Sale savedSale = saleRepository.save(sale);
        RefundDto refundDto = new ModelMapper().map(savedSale.getRefund(),RefundDto.class);
        refundDto.setRefundData(savedSale.getRefund());
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .message("판매번호 : "+requestReFundData.getIdx()+"번 상품의 환불요청이 완료되었습니다.")
                .item(refundDto)
                .build();
    }

    @Override
    public ResponseData selectSale(int idx) {
        Optional<Sale> optionalSale = saleRepository.findById((long) idx);
        if(!optionalSale.isPresent()){ throw new SaleNotFoundException(); }
        Sale sale = optionalSale.get();
        if(!sale.getClient().getUserId().equals(getUserId())){ throw  new OwnerIsNotException("판매내역은 구매자만 열람하실수 있습니다."); }
        SaleDto saleDto = new ModelMapper().map(sale,SaleDto.class);
        saleDto.setProductAndStoreAndClient(sale.getProduct());

        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .item(saleDto)
                .build();
    }

    private void refundWithStockUpdate(Sale sale) {
        int originalStockEA = sale.getProduct().getStock().getSt_ea();
        int refundProductEA = sale.getEa();
        sale.getProduct().getStock().setSt_ea(originalStockEA+refundProductEA);
        if(originalStockEA+refundProductEA>0){
            sale.getProduct().getStock().setSt_flag(true);
        }
    }

    // Util Method

    private String getUserId() {
        //SecurityContext 로그인 내역 확인
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        return userId;
    }

    private void productChange(Product product , RequestProduct reProduct){
        if(reProduct.getPName()!=null){ product.setPName(reProduct.getPName()); }
        if(reProduct.getP_price()!=0){ product.setP_price(reProduct.getP_price()); }
        if(reProduct.getP_description()!=null){ product.setP_description(reProduct.getP_description());}
        //재교
        if(reProduct.getSt_flag()!=null){product.getStock().setSt_flag(reProduct.getSt_flag());}
        if(reProduct.getSt_ea()!=product.getStock().getSt_ea()){ product.getStock().setSt_ea(reProduct.getSt_ea()); }
        if(reProduct.getFileBase64()!=null){
            product.setP_originalName(reProduct.getFileName());
            HashMap<String,String> hmap = new HashMap<>();
            hmap.put("fileName",reProduct.getFileName());
            hmap.put("fileBase64",reProduct.getFileBase64());
            product.setP_saveName(fileUpload.fileUpload(hmap).getPath());
        }

    }

    private List<ProductDto> listEntityToListDto(List<Product> products){
        List<ProductDto> list = new ArrayList<>();
        for (Product p:products) {
            ProductDto pro = new ModelMapper().map(p,ProductDto.class);
            pro.setStoreIdx(p);
            list.add(pro);
        }
        return list;
    }

    private Sale saveSaleData(Product product,RequestSaleData saleData){
        Sale sale = new ModelMapper().map(saleData,Sale.class);
        sale.setProduct(product);
        sale.setStore(product.getStore());
        sale.setClient(clientRepository.findById(getUserId()).get());
        sale.setCommit("1");
        return saleRepository.save(sale);
    }

    private Refund setRefund(RequestReFundData requestReFundData,Sale sale){
        Refund refund = new Refund();
        refund.setReason(requestReFundData.getReason());
        refund.setSale(sale);
        refund.setProduct(sale.getProduct());
        refund.setClient(sale.getClient());
        refund.setStore(sale.getStore());
        refund.setIdx((long) this.idxCreate());
        return refund;
    }

    private int idxCreate(){
        int randomIdx = 0;
        while (true){
            randomIdx= (int) (Math.random()*10000+1001);
            if(!refundRepository.existsById((long) randomIdx)){ break;}
        }
        return randomIdx;
    }
}
