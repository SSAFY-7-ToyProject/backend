package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Birth;
import com.teamauc.diary.domain.Gender;
import com.teamauc.diary.domain.User;
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

@RestController
@RequiredArgsConstructor
@RequestMapping ("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public RegistUserResponseDto registUser(@RequestBody @Valid RegistUserRequestDto request){

        User user = User.createUser(request.email,request.password,request.name,request.birth,request.gender,request.phoneNumber);

        String uid = userService.regist(user);

        return new RegistUserResponseDto(uid);
    }

    @GetMapping("/{uid}")
    public ReadUserResponseDto readUser(@PathVariable ("uid") String uid) {

        User user = userService.SearchUserById(uid);

        return new ReadUserResponseDto(uid,user.getEmail(),user.getName(),user.getBirth(),user.getGender(),user.getPhoneNumber());

    }

    @PutMapping("/{uid}")
    public UpdateUserResponseDto updateUser(@PathVariable("uid")String uid, @RequestBody UpdateUserRequestDto request){

        userService.update(uid,request.getName(),request.getBirth(),request.getGender(), request.getPhoneNumber());

        return new UpdateUserResponseDto(uid);
    }

    @DeleteMapping("/{uid}")
    public DeleteUserResponseDto deleteUser(@PathVariable("uid") String uid){

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

    @Data
    @AllArgsConstructor
    static class RegistUserResponseDto {

        private String uid;
    }

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
    static class UpdateUserRequestDto {

        private String name;
        private Birth birth;
        private Gender gender;
        private String phoneNumber;

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
