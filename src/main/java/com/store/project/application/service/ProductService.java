package com.store.project.application.service;

import com.store.project.application.domain.dto.ProductDto;
import com.store.project.application.domain.dto.RequestProduct;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.request.RequestReFundData;
import com.store.project.application.request.RequestSaleData;
import com.store.project.application.request.RequestStockData;
import com.store.project.application.response.ResponseData;
import org.springframework.data.web.PagedResourcesAssembler;

public interface ProductService {
    ResponseData insertProduct(RequestProduct product);

    ResponseData selectProduct(int idx);

    ResponseData productList(int size, int page, PagedResourcesAssembler<ProductDto> assembler);

    ResponseData productDataChange(RequestProduct product);

    ResponseData productDataDelete(int idx);

    ResponseData searchingProduct(String title, int size, int page, PagedResourcesAssembler<Product> assembler);

    ResponseData saleProduct(RequestSaleData saleData);

    ResponseData refoundProduct(RequestReFundData requestReFundData);

    ResponseData selectSale(int idx);

    ResponseData productStockUpdate(RequestStockData stockData, int idx);
}
