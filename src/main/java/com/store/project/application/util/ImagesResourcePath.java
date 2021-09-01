package com.store.project.application.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ImagesResourcePath {

    @Autowired
    Environment env;

    public String getImagesResourcesPath(String saveName){
        return env.getProperty("custom.path.resources-images-path")+saveName;
    }
}
