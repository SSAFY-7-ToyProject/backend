package com.teamauc.diary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter

public class User {

    @Id
    @Column (name = "user_email")
    @Setter (AccessLevel.NONE)
    private String email;

    @Setter (AccessLevel.NONE)
    private String password;

    private String name;

    @Embedded
    private Birth birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column (name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "user") // Test 할 것 "" 안의 value가 의미하는 것?
    @JsonIgnore
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    // 생성 메소드 //

    public static User createUser(String email, String password, String name, Birth birth, Gender gender, String phoneNumber){

        User user = new User();

        user.email = email;
        user.password = password;
        user.name = name;
        user.birth = birth;
        user.gender = gender;
        user.phoneNumber = phoneNumber;

        return user;
    }



}
