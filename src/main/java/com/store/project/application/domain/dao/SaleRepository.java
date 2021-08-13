package com.store.project.application.domain.dao;

import com.store.project.application.domain.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale,Long> {

    Boolean existsByClient_UserIdAndProductIdx(String client_userId,Long product_idx);

}
