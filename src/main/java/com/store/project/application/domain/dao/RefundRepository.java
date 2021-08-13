package com.store.project.application.domain.dao;

import com.store.project.application.domain.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund,Long> {

}
