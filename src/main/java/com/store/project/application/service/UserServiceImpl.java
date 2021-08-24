package com.store.project.application.service;

import com.store.project.application.Handling.exception.client.UserDuplicateException;
import com.store.project.application.Handling.exception.client.UserNotFoundException;
import com.store.project.application.domain.dao.ClientRepository;
import com.store.project.application.domain.dto.ClientDto;
import com.store.project.application.domain.dto.Role;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.request.RequestClientInfoChange;
import com.store.project.application.response.ResponseData;
import com.store.project.application.response.ResponseDataStatus;
import com.store.project.application.util.EmailHtmlCreate;
import com.store.project.application.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    //회원가입
    @Override
    public ResponseData register(ClientDto clientDto) {
        //중복검사
        Optional<Client> cl2 = clientRepository.findById(clientDto.getUserId());
        if(cl2.isPresent()){
            throw new UserDuplicateException("중복된 아이디 입니다.");
        }
        // Entitly 변환
        Client clientEntity = new ModelMapper().map(clientDto,Client.class);

        //암호화
        clientEntity.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        clientEntity.setRole(Role.USER);
        // 데이터 저장
        clientRepository.save(clientEntity);
        // Response setting
        ResponseData responseData = ResponseData.builder()
                .status(HttpStatus.OK)
                .code(ResponseDataStatus.SUCCESS)
                .message("회원가입이 완료 되었습니다.")
                .build();
        return responseData;
    }

    @Override
    public ResponseData duplication(ClientDto clientDto) {

//        Optional<Client> cl2 = clientRepository.findById(clientDto.getUserId());
        Boolean aBoolean = clientRepository.existsByUserId(clientDto.getUserId());
        if(aBoolean){
            throw new UserDuplicateException("중복된 아이디 입니다.");
        }
        ResponseData responseData = ResponseData.builder()
                .status(HttpStatus.OK)
                .code(ResponseDataStatus.SUCCESS)
                .message("사용가능한 아이디 입니다.")
                .build();
        return responseData;
    }

    @Override
    public ResponseData myinfo() {
//        String userId = getUserId();
        String userId = SecurityUtil.getCurrentUserName().get();
        //회원정보 가져오기
        Optional<Client> userEntity = clientRepository.findById(userId);
        Client client = userEntity.get();
        if(!userEntity.isPresent()){
            throw new UserNotFoundException("존재하지 않는 회원 아이디 입니다.");
        }
        ClientDto dto = new ModelMapper().map(client,ClientDto.class);
        dto.setS_idx(Math.toIntExact(userEntity.get().getStore().getS_idx()));
        dto.setSaleList(client);
        ResponseData responseData = ResponseData.builder()
                .code(ResponseDataStatus.SUCCESS)
                .status(HttpStatus.OK)
                .item(dto)
                .build();
        return responseData;
    }

    @Override
    public ResponseData infoChange(RequestClientInfoChange info) {
        String userId = getUserId();
        Optional<Client> userEntity = clientRepository.findById(userId);
        if(!userEntity.isPresent()){
            throw new UserNotFoundException("존재하지 않는 회원 아이디 입니다.");
        }
        Client user = userEntity.get();
        user.setPassword(passwordEncoder.encode(info.getPassword()));
        user.setName(info.getName());
        user.setAddress(info.getAddress());
        user.setPhone(info.getPhone());

        clientRepository.save(user);
        Optional<Client> userEntity2 = clientRepository.findById(userId);
        ClientDto dto = new ModelMapper().map(userEntity2.get(),ClientDto.class);
        dto.setS_idx(Math.toIntExact(userEntity2.get().getStore().getS_idx()));

        ResponseData responseData = ResponseData.builder()
                                    .status(HttpStatus.OK)
                                    .code(ResponseDataStatus.SUCCESS)
                                    .item(dto)
                                    .build();

        return responseData ;
    }

    @Override
    public ResponseData infoDelete() {
        String userId = getUserId();
        Optional<Client> userEntity = clientRepository.findById(userId);
        if(!userEntity.isPresent()){
            throw new UserNotFoundException("이미 탈퇴한 회원이거나,존재하지 않는 회원입니다.");
        }

        clientRepository.deleteById(userId);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("url","/api/v1/logout.do");
        ResponseData responseData = ResponseData.builder()
                .status(HttpStatus.OK)
                .code(ResponseDataStatus.SUCCESS)
                .message(userId+"를 정상적으로 탈퇴 완료하였습니다.")
                .item(hashMap)
                .build();
        return responseData;
    }

    @Override
    public ResponseData forgetPassword(String userId) {
        log.info("forgetPassword() run");
        Optional<Client> userEntity = clientRepository.findById(userId);
        if(!userEntity.isPresent()){
            throw new UserNotFoundException("존재하지 않는 회원 아이디 입니다.");
        }
        log.info("userId is get");
        String email = userEntity.get().getEmail();
        String flag = "forgetPwd";
        String savedPwd = this.certified_key();
        String encodePwd = passwordEncoder.encode(savedPwd);
        clientRepository.updatePassword(userId,encodePwd);
        log.info("저장된 비밀번호 : "+encodePwd);
        String html = new EmailHtmlCreate().emailSendBuffer(savedPwd,flag);
        try {
            mailService.sendMail(email,"[비밀번호 찾기 전송]",html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        log.info("메일전송");
        return ResponseData.builder()
                .status(HttpStatus.OK)
                .code(ResponseDataStatus.SUCCESS)
                .message("해당 아이디의 이메일로 임시비밀번호가 전송되었습니다.")
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("LoadUserByUSerName: "+userId);
        Optional<Client> Client = clientRepository.findById(userId);
        Client.orElseThrow(()->new InternalAuthenticationServiceException("해당 Id는 존재 하지 않습니다."));
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ClientDto dto = mapper.map(Client.get(),ClientDto.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(""+dto.getRole()));
        return new User(dto.getUserId(),dto.getPassword(),authorities);
    }
    private String getUserId() {
        //SecurityContext 로그인 내역 확인
        Optional<String> currentUserName = SecurityUtil.getCurrentUserName();
        return currentUserName.get();
    }

    private String certified_key() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;

        do {
            num = random.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        } while (sb.length() < 10);
        return sb.toString();
    }
}
