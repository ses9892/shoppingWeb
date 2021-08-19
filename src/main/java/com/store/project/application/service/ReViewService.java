package com.store.project.application.service;

import com.store.project.application.domain.dto.ReViewDto;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.ReView;
import com.store.project.application.request.RequestReComment;
import com.store.project.application.response.ResponseData;
import org.springframework.data.web.PagedResourcesAssembler;

public interface ReViewService {
    ResponseData insertReView(ReViewDto reViewDto);

    ResponseData selectReViewList(int size, int page, PagedResourcesAssembler<ReView> assembler);

    ResponseData searchReViewList(String title, int size, int page, PagedResourcesAssembler<ReView> assembler);

    ResponseData changeReView(ReViewDto reViewDto);

    ResponseData deleteReview(int idx);

    ResponseData insertReViewComment(RequestReComment comment);

    ResponseData selectReView(int idx);

    ResponseData deleteReViewComm(RequestReComment comment);

    ResponseData updateReViewComm(RequestReComment comment);
}
