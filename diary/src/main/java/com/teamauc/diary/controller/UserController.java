package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Birth;
import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.Gender;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.exception.LoginException;
import com.teamauc.diary.exception.UnauthorizedException;
import com.teamauc.diary.service.JwtService;
import com.teamauc.diary.service.UserService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/user")
@Slf4j
public class UserController {

    private final JwtService  jwtService;
    private final UserService userService;

    // 회원 가입
    @PostMapping
    public MessageResponseDto registUser(@RequestBody @Valid RegistUserRequestDto request){

        User user = User.createUser(request.email,request.password,request.name,request.birth,request.gender,request.phoneNumber);

        userService.regist(user);

        return new MessageResponseDto("회원가입 완료");
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResultDto checkEmailDuplicated(@RequestBody CheckEmailDuplicatedRequestDto request) {
        User user = userService.searchUserByEmail(request.getEmail());
        Map map = new HashMap();
        map.put("duplicated", user!=null);
        return new ResultDto(map);
    }

    // 로그인
    @PostMapping("/login")
    public Map login(@RequestBody @Valid LoginRequestDto request) {

        User user = userService.login(request.getEmail(), request.getPassword());

        if (user == null) {
            throw new LoginException ("로그인에서 심각한 오류가 발생했습니다. 재가입 요망");
        }
        String token = jwtService.create("uid",user.getUid(),"access-token");

        Map map = new HashMap();
        map.put("access-token",token);
        map.put("uid",user.getUid());

        return map;
    }

    // 이메일로 사용자 검색
    @GetMapping("/search")
    public ResultDto searchUser(@RequestBody @Valid SearchUserRequestDto request) {

        List<SearchUserResponseDto> searchedUser = userService.searchAllUserByName(request.word)
                .stream().map(user-> new SearchUserResponseDto(user)).collect(Collectors.toList());
        return new ResultDto(searchedUser);
    }

    // 사용자 정보 조회
    @GetMapping("/{uid}")
    public ResultDto readUser(@PathVariable ("uid") String uid) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        boolean isMine = currentUid.equals(uid);

        if(!isMine) throw new UnauthorizedException("남의 신상정보를 보려고 하지마세요.");

        User user = userService.searchUserById(uid);

        return new ResultDto(new ReadUserResponseDto(
                uid,
                user.getEmail(),
                user.getName(),
                new Birth(user.getBirth().getBirthYear(), user.getBirth().getBirthMonth(), user.getBirth().getBirthDay()),
                user.getGender(),
                user.getPhoneNumber()));

    }

    // 사용자 정보 수정
    @PutMapping("/{uid}")
    public UpdateUserResponseDto updateUser(@PathVariable("uid")String uid, @RequestBody UpdateUserRequestDto request){

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        boolean isMine = currentUid.equals(uid);

        if(!isMine) throw new UnauthorizedException("남의 신상정보를 수정하려고 하지마세요.");

        User user = userService.searchUserById(uid);

        userService.update(uid,request.getName(),request.getBirth(),request.getGender(), request.getPhoneNumber());

        return new UpdateUserResponseDto(uid);
    }

    // 회원 탈퇴
    @DeleteMapping("/{uid}")
    public DeleteUserResponseDto deleteUser(@PathVariable("uid") String uid){

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        boolean isMine = currentUid.equals(uid);

        if(!isMine) throw new UnauthorizedException();

        User user = userService.searchUserById(uid);

        userService.delete(uid);
        return new DeleteUserResponseDto("회원 탈퇴가 완료되었습니다.");

    }


    @Data
    static class RegistUserRequestDto {

        @NotEmpty
        @Email(message = "이메일 형식을 확인해주세요")
        private String email;

        @NotEmpty
        @Size(min=8,max=20,message = "글자 수 제한(8~20자)을 확인해 주세요")
        private String password;

        @NotEmpty
        private String name;


        private Birth birth;

        private Gender gender;

        @NotEmpty
        @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
        private String phoneNumber;

    }

//    @Data
//    @AllArgsConstructor
//    static class RegistUserResponseDto {
//
//        private String uid;
//    }

    @Data
    @AllArgsConstructor
    static class ReadUserResponseDto{

        private String uid;

        private String email;

        private String name;

        private Birth birth;

        private Gender gender;

        private String phoneNumber;

    }

    @Data
    static class CheckEmailDuplicatedRequestDto {
        private String email;
    }

    @Data
    static class SearchUserRequestDto {
        @NotEmpty
        private String word;
    }

    @Data
    static class SearchUserResponseDto {
        private String uid;
        private String name;
        private Gender gender;

        public SearchUserResponseDto(User user){
            this.uid = user.getUid();
            this.name = user.getName();
            this.gender = user.getGender();
        }
    }

    @Data
    static class UpdateUserRequestDto {

        private String name;
        private Birth birth;
        private Gender gender;
        private String phoneNumber;

    }

    @Data
    static class LoginRequestDto {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class UpdateUserResponseDto {

        private String uid;
    }

    @Data
    @AllArgsConstructor
    static class DeleteUserResponseDto{
        private String msg;
    }

}
