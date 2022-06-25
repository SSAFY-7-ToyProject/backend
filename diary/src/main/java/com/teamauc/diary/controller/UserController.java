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

        log.info(request.getName()+"<<DKLADSFMKLAMFKLDAMSFLKMLKA");
        userService.update(email,request.getName(),request.getBirth(),request.getGender(), request.getPhoneNumber());

        return new UpdateUserResponseDto(email);
    }


    @Data
    static class RegistUserRequestDto {

        private String email;
        private String password;
        private String name;
        private Birth birth;
        private Gender gender;
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

}
