package com.example.CookMaster.app.store.repository;

import com.example.CookMaster.app.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    Store findByName(String storeName);
}
