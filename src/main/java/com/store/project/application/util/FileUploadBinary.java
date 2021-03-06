package com.store.project.application.util;

import com.store.project.application.Handling.exception.Upload.FileNotExistException;
import com.store.project.application.domain.dto.ReViewDto;
import com.store.project.application.domain.dto.RequestProduct;
import com.store.project.application.domain.entity.Product;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
@Component
public class FileUploadBinary {
//    private final String imgResourcePath
    private final String windowUploadPath = Paths.get("D:","shopImg").toString();
    private final String linuxUploadPath = "/home/ec2-user/Shop-data/img";
    String filebase64;
    String os;

    public FileUploadBinary() {
       this.os = System.getProperty("os.name").toLowerCase();
    }

    private final String getRandomString(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public FileInputStream fileInputStream;
    public ByteArrayOutputStream byteArrayOutputStream;


    public Boolean fileValidCheck(RequestProduct reProduct , Product product){
        File originFile = new File(product.getP_saveName());
        String originImgFileBase64 = this.Base64Encoding(originFile);
        if(reProduct.getFileBase64().equals(originImgFileBase64)){
            return false;
        }
        return true;
    }
    //return : 저장할 파일이름

    public String fileUpload(HashMap<String,String> hmap){
        //파일 있나없나 검사
        //폴더 없을시 폴더생성
        //HashMap Data
        String FileName = hmap.get("fileName");
        String FileBase64 = hmap.get("fileBase64");

        String extension = FilenameUtils.getExtension(FileName);
        //saveName + UUID 지정
        String saveName = getRandomString()+"."+extension;
        //fileBase64 decoding시작
        byte[] decodeBytes = Base64.getDecoder().decode(FileBase64);
        //Os 별 업로드
        File file =null;
        if(os.equals("windows 10")){
            this.MakePath(windowUploadPath);
            file = new File(windowUploadPath+"/"+saveName);
        }else{
            this.MakePath(linuxUploadPath);
            file = new File(linuxUploadPath+"/"+saveName);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodeBytes,0,decodeBytes.length);
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return saveName;
    }

    private void MakePath(String OsPath) {
        File dir = new File(OsPath);
        if(dir.exists()==false){
            dir.mkdirs(); //security 사용
        }
    }

    public String Base64Encoding(File file){
        String resultData = null;

        if(!file.exists()){
            throw new FileNotExistException("등록된 이미지 파일이 존재하지 않습니다.");
        }

        try{
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream();

            int len =0;
            byte[] buf = new byte[1024];

            while((len= fileInputStream.read(buf))!=-1){
                byteArrayOutputStream.write(buf,0,len);
            }
            byte[] fileArray = byteArrayOutputStream.toByteArray();
            resultData = new String(Base64.getEncoder().encode(fileArray));
        }catch (IOException e){
                e.printStackTrace();
        }
        return resultData;
    }

}
