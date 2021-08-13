package com.store.project.application.domain.dao;

import com.store.project.application.domain.dto.Role;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.domain.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {

    Optional<Store> findByClient(Client client);

    @Transactional
    Optional<Store> findByClient_UserId(String userId);

    Page<Store> findByStNameContainsIgnoreCase(String StName, Pageable pageable);

    Optional<Store> findByStName(String StName);



}
