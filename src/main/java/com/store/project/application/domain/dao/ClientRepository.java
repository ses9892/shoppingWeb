package com.store.project.application.domain.dao;

import com.store.project.application.domain.dto.Role;
import com.store.project.application.domain.entity.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ClientRepository extends CrudRepository<Client,String>{

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Client a SET a.role = ?2 where a.userId = ?1")
    @Transactional
    public int updateRole(String userId, Role role);

    public Boolean existsByUserId(String userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Client a SET a.password = ?2 where a.userId = ?1")
    @Transactional
    public int updatePassword(String userId,String encodePassword);
}
