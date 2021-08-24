package com.store.project.application.util;

public class EmailHtmlCreate {

    public String emailSendBuffer(String encryptKey, String flag){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<!DOCTYPE html>");
        buffer.append("<html>");
        buffer.append("<head>");
        buffer.append("</head>");
        buffer.append("<body>");
        if(flag.equals("emailSend")) {
            buffer.append(" <div" +
                    "style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                    "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                    "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">Library</span><br />" +
                    "		<span style=\"color: #02b875\">메일인증</span> 안내입니다." +
                    "	</h1>\n" +
                    "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                    "		Library에 오신걸 환영합니다.<br />" +
                    "		아래 <b style=\"color: #02b875\">'암호'</b> 를 보시고 회원가입 이메일 인증을 완료해 주세요.<br />" +
                    "		감사합니다." +
                    "	</p>" +
                    "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                    "	target=\"_blank\">" +
                    "		<p" +
                    "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                    "			" + encryptKey + "</p>" +
                    "	</a>" +
                    "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                    " </div>");
        }else if(flag.equals("forgetPwd")){
            buffer.append(" <div" +
                    "style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                    "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                    "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">Library</span><br />" +
                    "		<span style=\"color: #02b875\">임시비밀번호</span> 안내입니다." +
                    "	</h1>\n" +
                    "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                    "		Shop몰에오신걸 환영합니다.<br />" +
                    "		아래 <b style=\"color: #02b875\">'임시암호'</b> 를 보시고 로그인하여 주십시오<br />" +
                    "		감사합니다." +
                    "	</p>" +
                    "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                    "	target=\"_blank\">" +
                    "		<p" +
                    "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                    "			" + encryptKey + "</p>" +
                    "	</a>" +
                    "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                    " </div>");
        }
        buffer.append("</body>");
        buffer.append("</html>");
        return buffer.toString();
    }
}
