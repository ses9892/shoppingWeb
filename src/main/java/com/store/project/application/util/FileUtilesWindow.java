package com.store.project.application.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**파일 업로드를 위한 유틸 클래스*/
@Component
public class FileUtilesWindow {
    /**오늘날짜*/
    private final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    /** 업로드 경로*/
    private final String windowUploadPath = Paths.get("D:","shopImg").toString();

    /** 서버에 생성할 파일명을 처리할 랜덤 문자열 반환*/
    private final String getRandomString(){
        return UUID.randomUUID().toString().replace("-","");
    }

        /**넘어온 파일이 비어있으면 빈리스트 반환
         * @return*/
    public List<String> uploadFiles(MultipartFile[] files) {
//        if(files[0].getSize()<1){
//            return Collections.emptyList();
//        }
//        /** 파일정보를 담을 리스트*/
//        List<String> fileList = new ArrayList<>();
//
//        /** uploadPath에 해당하는 폴더가 존재하지 않을시 부모 폴더에 해당 폴더 생성*/
//        File dir = new File(windowUploadPath);
//        if(dir.exists()==false){
//            dir.mkdirs(); //security 사용
//        }
//
//        /**저장될 파일의 갯수만큼 실제이름,저장될이름등을 List에 담는다.*/
//        for (MultipartFile file : files) {
//            try{
//            /**확장자*/
//            String extenstion = FilenameUtils.getExtension(file.getOriginalFilename());
//            /**서버에 저장될 파일명((랜덤 문자열 + 확장자) 안써도됨 주석가능*/
//            String saveName = getRandomString()+"."+extenstion;
//
//            /**업로드 경로에saveName과 동일한 이름을 가진 파일 생성*/
//            File target = new File(windowUploadPath,saveName); // 업로드 경로 , 이름
//            file.transferTo(target);
//            FileDto fileDto = null;
//            if(extenstion.equalsIgnoreCase("pdf") || extenstion.equalsIgnoreCase("jpg") || extenstion.equalsIgnoreCase("png") || extenstion.equalsIgnoreCase("gif")){
//                fileDto = FileDto.builder()
//                        .createdAt(new Date())
//                        .saveName(saveName)
//                        .originalName(file.getOriginalFilename())
//                        .build();
//            }else{
//                break;
//                }
//            /**파일 정보저장*/
//            fileList.add(fileDto);
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return fileList;
//    }
        return null;
    }
}
