package com.store.project.application.request;

import com.store.project.application.domain.entity.ReView;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class RequestReComment {

    @NotNull(message = "required ReView idx!!")
    @Min(value = 1,message = "r_idx > 0 !!")
    private int r_idx;

    @NotBlank(message = "댓글 내용을 입력해 주세요.")
    private String comment;

    private String writer;

}
