package com.store.project.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.project.application.domain.entity.Product;
import com.store.project.application.domain.entity.ReView;
import com.store.project.application.domain.entity.ReViewComment;
import lombok.Data;
import org.springframework.hateoas.Link;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReViewDto {

    //리뷰할 상품의 idx
    @NotNull(message = "상품번호를 필수로 기입해주세요")
    @Min(value = 1,message = "상품번호는 최소 1 이상이어야 합니다.")
    private int idx;

    @NotBlank(message = "제목을 입력해 주세요")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요")
    private String content;

    private String writer;

    private String fileName;

    private String fileBase64;

    private Long product_idx;

    private List<Long> reViewComment_idx;

    private Link ReViewLink;

    private int totalComment;

    public void setProduct(Product product){
        product_idx = product.getIdx();
    }
    public void setReViewComment_idx(ReView reView){
        reViewComment_idx = new ArrayList<>();
        List<ReViewComment> reViewComment = reView.getReViewComment();
        for (ReViewComment r:reViewComment) {
            reViewComment_idx.add(r.getIdx());
        }
    }

}
