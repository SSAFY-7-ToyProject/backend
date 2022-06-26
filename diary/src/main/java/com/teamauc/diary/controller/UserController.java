package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Birth;
import com.teamauc.diary.domain.Gender;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public RegistUserResponseDto registUser(@RequestBody RegistUserRequestDto request){

        User user = User.createUser(request.email,request.password,request.name,request.birth,request.gender,request.phoneNumber);

        String email = userService.regist(user);

        return new RegistUserResponseDto(email);
    }

    @PutMapping("/{email}")
    public UpdateUserResponseDto updateUser(@PathVariable("email")String email, @RequestBody UpdateUserRequestDto request){

        userService.update(email,request.getName(),request.getBirth(),request.getGender(), request.getPhoneNumber());

        return new UpdateUserResponseDto(email);
    }

    @DeleteMapping("/{email}")
    public DeleteUserResponseDto deleteUser(@PathVariable("email") String email){

        userService.delete(email);
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

        @NotEmpty
        private Birth birth;

        @NotEmpty
        private Gender gender;

        @NotEmpty
        private String phoneNumber;

    }

    @Data
    @AllArgsConstructor
    static class RegistUserResponseDto {

        private String email;
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

        private String email;
    }

    @Data
    @AllArgsConstructor
    static class DeleteUserResponseDto{
        private String msg;
    }

}
