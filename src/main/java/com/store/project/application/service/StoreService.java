package com.store.project.application.service;

import com.store.project.application.domain.dto.StoreDto;
import com.store.project.application.domain.entity.Store;
import com.store.project.application.request.RequestStoreInfoChange;
import com.store.project.application.response.ResponseData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

public interface StoreService {
    ResponseData insertStore(StoreDto storeDto);

    ResponseData storeInfo();

    ResponseData storeInfoChange(RequestStoreInfoChange requestStoreInfoChange);

    ResponseData storeList(Pageable pageable, PagedResourcesAssembler<Store> assembler,int Reqpage);

    ResponseData storeSearch(PageRequest pageRequest, PagedResourcesAssembler<Store> assembler, int page, String name);
}
