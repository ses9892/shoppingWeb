package com.store.project.application.domain.dao;

import com.store.project.application.domain.dto.ProductDto;
import com.store.project.application.domain.dto.Role;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.Stock;
import com.store.project.application.domain.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Product a SET a.stock = ?1 where a.idx=?2")
    public int updateStock(Stock stock, Long idx);

    @Transactional
    @Query(value = "SELECT p FROM Product p ORDER BY p.idx desc")
    Page<Product> findAllByOrderByP_idxDesc(Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = "select p from Product p WHERE p.pName like %?1%")
    Page<Product> findAllByPNameContainsOrderByPNameDesc(String pName,Pageable pageable);


//    @Query(value = "select p.p_idx from Product p where ")

}
