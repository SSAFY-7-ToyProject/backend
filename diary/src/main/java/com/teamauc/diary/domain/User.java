package com.teamauc.diary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamauc.diary.util.SHA256;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;


import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter

public class User {

    @Id
    @Column (name = "user_uid")
    @Setter (AccessLevel.NONE)
    private String uid;

    @Column (name = "user_email", unique = true)
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

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    // 생성 메소드 //

    public static User createUser(String email, String password, String name, Birth birth, Gender gender, String phoneNumber){

        User user = new User();

        SHA256 sha256 = new SHA256();

        try {
            user.uid = sha256.encrypt(email);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        user.email = email;
        user.password = password;
        user.name = name;
        user.birth = birth;
        user.gender = gender;
        user.phoneNumber = phoneNumber;

        return user;
    }



}
