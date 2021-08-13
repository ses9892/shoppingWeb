package com.store.project.application.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
public class HateOasLoader {

    private int size;

    private int page;

    private String title;

    private String flag;

    Environment env;

    public HateOasLoader(int size, int page,String flag,Environment env) {
        this.size = size;
        this.page = page;
        this.flag = flag;
        this.env = env;
    }

    public HateOasLoader(int size, int page,String flag, String title,Environment env) {
        this.size = size;
        this.page = page;
        this.flag = flag;
        this.title = title;
        this.env = env;
    }

    // 전체 List
    public void setListHateOas(HashMap<String,Object> hmap, PagedModel pagedModel){
        String requestLink = this.setEnvProperties();
        if(pagedModel.getPreviousLink().isPresent()){
            Link pre = Link.of(requestLink+(page-1)).withRel("pre");
            hmap.put("pre",pre);
        }
        if(pagedModel.getNextLink().isPresent()){
            Link next = Link.of(requestLink+(page+1)).withRel("next");
            hmap.put("next",next);
        }
    }
    // SearchList
    public void setSearchingHateOas(HashMap<String,Object> hmap, PagedModel pagedModel){
        String requestLink = this.setEnvProperties();
        if(pagedModel.getPreviousLink().isPresent()){
            Link pre = Link.of(requestLink+title+"&page="+(page-1)).withRel("pre");
            hmap.put("pre",pre);
        }
        if(pagedModel.getNextLink().isPresent()){
            Link next = Link.of(requestLink+title+"&page="+(page+1)).withRel("next");
            hmap.put("next",next);
        }
    }
    public String setEnvProperties(){
        switch (flag){
            case "reviewList":
                return env.getProperty("HateOas.review.list");
            case "reviewSearch":
                return env.getProperty("HateOas.review.search");
            case "productList":
                return env.getProperty("HateOas.product.list");
            case "productSearch":
                return env.getProperty("HateOas.product.search");
        }
        return null;
    }


}
