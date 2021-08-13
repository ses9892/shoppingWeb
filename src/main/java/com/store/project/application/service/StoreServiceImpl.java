package com.store.project.application.service;

import com.store.project.application.Handling.exception.store.StoreDuplicationException;
import com.store.project.application.Handling.exception.store.StoreOverFlowPageException;
import com.store.project.application.domain.dao.ClientRepository;
import com.store.project.application.domain.dao.StoreRepository;
import com.store.project.application.domain.dto.Role;
import com.store.project.application.domain.dto.StoreDto;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.domain.entity.Store;
import com.store.project.application.request.RequestStoreInfoChange;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ClientRepository clientRepository;


    @Override
    public ResponseData insertStore(StoreDto storeDto) {
        String userId = getUserId();
        // 개설된 사업자인지 검사
        // null = 생성가능  , not null = 생성불가
        Optional.ofNullable(storeRepository.findById(storeDto.getS_idx()))
                .ifPresent(c->{
                    if(c.isPresent()){
                        throw new StoreDuplicationException("이미 고객님은 사업자 등록을 하셨습니다.",getUserId(),clientRepository,storeRepository);
                    }
                });
        // 생성진행
        Client client = storeSave(userId,storeDto);

        // null
        Optional<Store> savedStore = storeRepository.findByClient(client);
        StoreDto dto = new ModelMapper().map(savedStore.get(),StoreDto.class);
        dto.setClient_id(savedStore.get().getClient().getUserId());

        ResponseData responseData = ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(dto)
                .message("매장등록이 정상적으로 등록되었습니다.")
                .build();

        return responseData;
    }

    @Override
    public ResponseData storeInfo() {
        String userId = getUserId();
        Optional<Store> OpStore = storeRepository.findByClient_UserId(userId);
        Store store = OpStore.get();

        StoreDto storeDto = new ModelMapper().map(store,StoreDto.class);
        storeDto.setClient_id(store.getClient().getUserId());
        storeDto.setProductList(store.getProduct());


        ResponseData responseData = ResponseData.builder()
                    .status(HttpStatus.OK).code(ResponseDataStatus.SUCCESS)
                    .item(storeDto)
                    .build();

        return responseData;
    }

    @Override
    public ResponseData storeInfoChange(RequestStoreInfoChange requestStoreInfoChange) {
        String userId = getUserId();
        StoreDto storeDto = new ModelMapper().map(requestStoreInfoChange,StoreDto.class);

        Store store = storeRepository.findByClient_UserId(userId).get();
        store.setStBrand(storeDto.getStBrand());
        store.setStName(storeDto.getStName());
        store.setStDelivery(String.valueOf(storeDto.getStDelivery()));

        store = storeRepository.findByClient_UserId(userId).get();

        ResponseData responseData = ResponseData.builder()
                    .code(ResponseDataStatus.SUCCESS)
                    .status(HttpStatus.OK)
                    .message("정보 수정이 완료 되었습니다.")
                    .build();

        return responseData;
    }

    @Override
    public ResponseData storeList(Pageable pageable, PagedResourcesAssembler<Store> assembler,int Reqpage) {
        HashMap<String , Object> dataMap = new HashMap<>();
        int flag = 0;
        dataMap.put("flag",flag);
        dataMap.put("page",Reqpage);
        Page<Store> storePage = storeRepository.findAll(pageable);

        if(Reqpage>storePage.getTotalPages()-1){
            throw new StoreOverFlowPageException("해당 페이지를 찾을 수 없습니다.");
        }

        HashMap<String,Object> hmap = GetListPageable(dataMap,assembler,storePage);

        ResponseData responseData = ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(hmap).build();
        return responseData;
    }

    @Override
    public ResponseData storeSearch(PageRequest pageRequest, PagedResourcesAssembler<Store> assembler
            , int Reqpage, String name) {
        HashMap<String , Object> dataMap = new HashMap<>();
        int flag = 1;
        dataMap.put("flag",flag);
        dataMap.put("name",name);
        dataMap.put("page",Reqpage);

        Page<Store> storePage = storeRepository.findByStNameContainsIgnoreCase(name,pageRequest);
        // search -> size = 0 -> 검색결과없음 return
        if(storePage.getContent().size()==0){
            return ResponseData.builder()
                    .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                    .message("검색한 키워드의 상점명은 존재하지 않습니다.")
                    .item(storePage.getContent()).build();
        }
        HashMap<String,Object> hmap = GetListPageable(dataMap,assembler,storePage);
        ResponseData responseData = ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS).status(HttpStatus.OK)
                .item(hmap).build();
        return responseData;
    }

    //판매장 DB에 저장하는 메소드
    public Client storeSave(String userId,StoreDto storeDto){
        Store store = new ModelMapper().map(storeDto,Store.class);
        Optional<Client> client = clientRepository.findById(userId);
        store.setClient(client.get());
        storeRepository.save(store);
        clientRepository.updateRole(getUserId(), Role.SELLER);

        return client.get();
    }

    private String getUserId() {
        //SecurityContext 로그인 내역 확인
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        return userId;
    }

    //클래스화 하자
    private HashMap<String,Object> GetListPageable(HashMap<String,Object> dataMap,PagedResourcesAssembler<Store> assembler,Page storePage){
        HashMap<String, Object> hmap = new HashMap<>();
        PagedModel<EntityModel<Store>> pageResource = assembler.toModel(storePage);
        //flag : 0 -> findAll
        //flag : 1 -> stNameSearch
        if ((int)dataMap.get("page") > storePage.getTotalPages() - 1) {
            throw new StoreOverFlowPageException("해당 페이지를 찾을 수 없습니다.");
        }
        switch ((int)dataMap.get("flag")) {
            case 0:
                if (pageResource.getNextLink().isPresent()) {
                    pageResource.add(Link.of("/api/v1/store/list?page=" + storePage.getNumber() + 1).withRel("NextPage"));
                }
                if (pageResource.getPreviousLink().isPresent()) {
                    pageResource.add(Link.of("/api/v1/store/list?page=" + (storePage.getNumber() - 1)).withRel("PreviousPage"));
                }
            case 1:
                if (pageResource.getNextLink().isPresent()) {
                    pageResource.add(Link.of("/api/v1/store/list/search?name="+dataMap.get("name")+"&page=" + storePage.getNumber() + 1).withRel("NextPage"));
                }
                if (pageResource.getPreviousLink().isPresent()) {
                    pageResource.add(Link.of("/api/v1/store/list/search?name="+dataMap.get("name")+"&page=" + (storePage.getNumber() - 1)).withRel("PreviousPage"));
                }
        }
        // dto List set -> put
        List content = storePage.getContent();
        List list = this.setStoreDtoList(content);
        hmap.put("content", list);
        //Page put
        hmap.put("NextPage", pageResource.getLink("NextPage"));
        hmap.put("PreviousPage", pageResource.getLink("PreviousPage"));
        return hmap;
    }

    private List<StoreDto> setStoreDtoList(List<Store> storeList){
        List<StoreDto> dtoList = new ArrayList<>();
        for (Store s:storeList) {
            StoreDto dto =new ModelMapper().map(s,StoreDto.class);
            dto.setClient_id(s.getClient().getUserId());
            dto.setProductList(s.getProduct());
            dtoList.add(dto);
        }
        return dtoList;
    }

}
