package com.store.project.application.domain.dao;

import com.store.project.application.domain.entity.ReView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReViewRepository extends JpaRepository<ReView,Long> {

    Page<ReView> findAllByTitleContains(String title,Pageable pageable);

}
