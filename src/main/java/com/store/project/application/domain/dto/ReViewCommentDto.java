package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.project.application.domain.entity.ReView;
import com.store.project.application.domain.entity.ReViewComment;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReViewCommentDto {

    private Long idx;

    private String comment;

    private Date date ;

    private String writer;

    private Long review_idx;

    public void SetReView_idx(ReViewComment reViewComment){
        this.review_idx = reViewComment.getReView().getIdx();
    }
}
