package com.store.project.application.Handling.exception.store;

import com.store.project.application.domain.dao.ClientRepository;
import com.store.project.application.domain.dao.StoreRepository;
import com.store.project.application.domain.entity.Store;
import com.store.project.application.response.ResponseDataStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StoreDuplicationException extends RuntimeException {

    public final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public final static String code = ResponseDataStatus.BAD_REQUEST;

    public Store store;
    StoreRepository storeRepository;
    ClientRepository clientRepository;

    public StoreDuplicationException(String meg) {
        super(meg);
    }

    public StoreDuplicationException(String meg,String userId, ClientRepository clientRepository, StoreRepository storeRepository) {
        super(meg);
        this.clientRepository = clientRepository;
        this.storeRepository = storeRepository;
        StoreCheck(userId);
    }

    public void StoreCheck(String userId){
//        clientRepository.findById("ses9892").get();
       store = storeRepository.findByClient(clientRepository.findById(userId).get()).get();
    }


}
