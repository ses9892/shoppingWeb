package com.store.project.application.domain.dao;

import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {

    Optional<Stock> findByProduct(Product product);

}
