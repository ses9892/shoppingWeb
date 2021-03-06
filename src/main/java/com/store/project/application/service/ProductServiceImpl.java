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
import com.store.project.application.request.RequestStockData;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import com.store.project.application.util.FileUploadBinary;
import com.store.project.application.util.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
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
    Environment env;

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
        //?????????
        HashMap<String,String> hmap = new HashMap<>();
        hmap.put("fileName",product.getFileName());
        hmap.put("fileBase64",product.getFileBase64());
        String saveName = fileUpload.fileUpload(hmap);
        String imgPath = this.getImagesResourcesPath(saveName);
        //????????????
        Product productEntity = new ModelMapper().map(product,Product.class);
        productEntity.setP_originalName(product.getFileName());
        productEntity.setP_saveName(imgPath);
        //????????????
        Optional<Store> store = storeRepository.findByClient_UserId(getUserId());
        productEntity.setStore(store.get());
        //???????????? + ?????? ??????
        Product saveEntity = productRepository.save(productEntity);
        entityManager.clear();
        //??????
        Stock stock = new Stock();
        if(product.getSt_ea()==null){ stock.setSt_ea(1); }else{stock.setSt_ea(Integer.parseInt(product.getSt_ea())); }
        stock.setSt_flag(true);
        stock.setProduct(saveEntity);
        stockRepository.save(stock);
        //???????????? ?????? ????????????
        entityManager.clear();
        saveEntity = productRepository.findById(saveEntity.getIdx()).get();
        ProductDto dto = new ModelMapper().map(saveEntity,ProductDto.class);
        dto.setStoreIdx(saveEntity);

        //??????
        ResponseData responseData = ResponseData.builder()
                                    .message("??????????????? ??????????????? ?????? ???????????????.")
                                    .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                                    .item(dto)
                                    .build();
        return responseData;
    }

    @Override
    public ResponseData selectProduct(int idx) {
        Optional<Product> productEntity = productRepository.findById((long) idx);

        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("?????? ???????????? ?????? ??? ????????????.");
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
        //?????????
        Pageable pageable = PageRequest.of(page,size);
        //?????? DESC ?????????
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
        //?????? ??????
        Optional<Product> productEntity = productRepository.findById((long) product.getP_idx());
        //?????? ?????? ?????? ??????
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("?????? ????????? ???????????? ????????? ?????????????????????");
        }
        //????????? ?????? ??????
        Product proEntity = productEntity.get();
        String store_userId = proEntity.getStore().getClient().getUserId();
        if(!store_userId.equals(getUserId())){
            throw new OwnerIsNotException("????????? ????????? ??????????????? ????????? ??? ????????????.");
        }
        // setter -> save
        this.productChange(proEntity,product);
        proEntity=productRepository.save(proEntity);
        ProductDto productDto = new ModelMapper().map(proEntity,ProductDto.class);
        productDto.setStoreIdx(proEntity);

        return ResponseData.builder()
                .message("?????? ????????? ?????? ???????????????.")
                .status(HttpStatus.OK).code(ResponseDataStatus.SUCCESS)
                .item(productDto)
                .build();
    }

    @Override
    public ResponseData productDataDelete(int idx) {
        //????????????
        Optional<Product> productEntity = productRepository.findById((long) idx);
        //null -> error
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("?????? ????????? ?????????????????? ???????????? ????????????");
        }
        Product product= productEntity.get();
        //?????? ????????? id <==> product??? ????????? Id??? ?????? ??????
        if(!product.getStore().getClient().getUserId().equals(getUserId())){
            throw new OwnerIsNotException();
        }
        //??????????????????
        productRepository.delete(product);
        //????????? Response???????????? ?????? ????????? ??????
        Optional<Store> StNameStore = storeRepository.findByStName(product.getStore().getStName());
        ResponseData responseData = ResponseData.builder()
                                    .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                                    .message("?????? ????????? ??????????????? ?????? ???????????????.")
                                    .build();
        return responseData;
    }

    @Override
    public ResponseData searchingProduct(String title, int size, int page, PagedResourcesAssembler<Product> assembler) {
        //????????? ????????? ???????????? ??????
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
        //??????????????????
        if(pagedModel.getPreviousLink().isPresent()){
            Link pre = Link.of("/api/v1/product/name="+title+"&page="+(page-1)).withRel("pre");
            hmap.put("pre",pre);
        }
        //?????? ?????? ??????
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


    //?????? ??????
    @Override
    public ResponseData saleProduct(RequestSaleData saleData) {
        //????????????
        Optional<Product> productEntity = productRepository.findById((long) saleData.getP_idx());
        //null -> error
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("?????? ????????? ?????????????????? ???????????? ????????????");
        }
        Product product = productEntity.get();
        //    Stock => ???????????? ?????? ??????
        if(!product.getStock().getSt_flag()) { throw new ProductNotSaleException(); } // fasle ??????
        //     EA check
        if(saleData.getEa()>product.getStock().getSt_ea()){ throw new ProductEaNotEnoughException(); }
        //sale Entity setting + save
        Sale save = this.saveSaleData(product,saleData);
        //???????????? ??????
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
        if(!sale.getClient().getUserId().equals(getUserId())){ throw new OwnerIsNotException("???????????? ??????????????? ????????? ?????????."); }
        if(sale.getCommit().equals("0")){throw new RuntimeException("?????? ?????? ????????? ???????????????."); }
        //???????????? + ??????/?????? ??????
        sale.setCommit("0");
        this.refundWithStockUpdate(sale);
        //?????? entity
        Refund refund = this.setRefund(requestReFundData, sale);
        sale.setRefund(refund);

        Sale savedSale = saleRepository.save(sale);
        RefundDto refundDto = new ModelMapper().map(savedSale.getRefund(),RefundDto.class);
        refundDto.setRefundData(savedSale.getRefund());
        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .message("???????????? : "+requestReFundData.getIdx()+"??? ????????? ??????????????? ?????????????????????.")
                .item(refundDto)
                .build();
    }

    @Override
    public ResponseData selectSale(int idx) {
        Optional<Sale> optionalSale = saleRepository.findById((long) idx);
        if(!optionalSale.isPresent()){ throw new SaleNotFoundException(); }
        Sale sale = optionalSale.get();
        if(!sale.getClient().getUserId().equals(getUserId())){ throw  new OwnerIsNotException("??????????????? ???????????? ??????????????? ????????????."); }
        SaleDto saleDto = new ModelMapper().map(sale,SaleDto.class);
        saleDto.setProductAndStoreAndClient(sale.getProduct());

        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .item(saleDto)
                .build();
    }

    @Override
    public ResponseData productStockUpdate(RequestStockData stockData, int idx) {
        //?????? ??????
        Optional<Product> productEntity = productRepository.findById((long) idx);
        //?????? ?????? ?????? ??????
        if(!productEntity.isPresent()){
            throw new ProductNotFoundException("?????? ????????? ???????????? ????????? ?????????????????????");
        }
        //????????? ?????? ??????
        String store_userId = productEntity.get().getStore().getClient().getUserId();
        if(!store_userId.equals(getUserId())){
            throw new OwnerIsNotException("????????? ????????? ??????????????? ????????? ??? ????????????.");
        }
        Stock stock = stockRepository.findByProductIdx((long)idx).get();
        int ea = stockData.getEa();
        // setter
        if(ea==0){
            stock.setSt_ea(0);
            stock.setSt_flag(false);
        }else{
            stock.setSt_ea(ea);
            stock.setSt_flag(stockData.getFlag());
        }
        //save
        Stock savedStock = stockRepository.save(stock);
        StockDto dto = new ModelMapper().map(savedStock,StockDto.class);
        dto.setProduct_idx(savedStock.getProduct().getIdx());

        return ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .message("?????? ????????? ?????? ???????????????.")
                .item(dto)
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
        //SecurityContext ????????? ?????? ??????
        Optional<String> currentUserName = SecurityUtil.getCurrentUserName();
        return currentUserName.get();
    }

    private void productChange(Product product, RequestProduct reProduct){
        if(reProduct.getPName()!=null){ product.setPName(reProduct.getPName()); }
        if(reProduct.getP_price()==null){ product.setP_price(Integer.parseInt(reProduct.getP_price())); }
        if(reProduct.getP_description()!=null){ product.setP_description(reProduct.getP_description());}
        //??????
        if(reProduct.getSt_flag()!=null){product.getStock().setSt_flag(reProduct.getSt_flag());}
        if(reProduct.getSt_ea()!=null){
            int ea = Integer.parseInt(reProduct.getSt_ea());
            if(ea == 0){
            product.getStock().setSt_flag(false);
            product.getStock().setSt_ea(0);
            }else{
                product.getStock().setSt_ea(Integer.parseInt(reProduct.getSt_ea()));
            }
        }else{
        }
        if(reProduct.getFileBase64()!=null){
            product.setP_originalName(reProduct.getFileName());
            HashMap<String,String> hmap = new HashMap<>();
            hmap.put("fileName",reProduct.getFileName());
            hmap.put("fileBase64",reProduct.getFileBase64());
            String saveName = fileUpload.fileUpload(hmap);
            String imagesResourcesPath = this.getImagesResourcesPath(saveName);
            product.setP_saveName(imagesResourcesPath);
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
        sale.setCategory("card");
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

    private String getImagesResourcesPath(String saveName){
        return env.getProperty("custom.path.resources-images-path")+saveName;
    }
}
