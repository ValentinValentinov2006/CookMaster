package com.example.CookMaster.app.store.service;

import com.example.CookMaster.app.store.model.Store;
import com.example.CookMaster.app.store.repository.StoreRepository;
import com.example.CookMaster.app.web.dto.CreateStoreRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StoreService {
    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }


    public void createStore(CreateStoreRequest createStoreRequest) {


        Store store = initializeStore(createStoreRequest);

        storeRepository.save(store);
        log.info("Store with %s is created".formatted(createStoreRequest.getStoreName()));

    }

    private static Store initializeStore(CreateStoreRequest createStoreRequest) {
        Store store =  Store.builder()
                 .name(createStoreRequest.getStoreName())
                 .address(createStoreRequest.getAddress())
                 .phone(createStoreRequest.getPhoneNumber())
                 .notes(createStoreRequest.getNotes())
                 .build();
        return store;
    }
}
